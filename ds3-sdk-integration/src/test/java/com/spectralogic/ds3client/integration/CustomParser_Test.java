/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.integration;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.parsers.interfaces.Function;
import com.spectralogic.ds3client.commands.parsers.interfaces.GetObjectParserConfiguration;
import com.spectralogic.ds3client.commands.parsers.utils.ResponseParserUtils;
import com.spectralogic.ds3client.commands.spectrads3.GetBulkJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetBulkJobSpectraS3Response;
import com.spectralogic.ds3client.exceptions.ContentLengthNotMatchException;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.channelbuilders.ObjectInputStreamBuilder;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.utils.IOUtils;
import org.junit.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.UUID;

import static com.spectralogic.ds3client.commands.parsers.utils.ResponseParserUtils.getSizeFromHeaders;
import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class CustomParser_Test {

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String TEST_ENV_NAME = "custom_parser_test";
    private static final String BUCKET_NAME = "custom_parser_bucket";
    private static final String OBJ_NAME = "stream_object_test.txt";
    private static final String OBJ_CONTENT = "Content of stream object";
    private static final byte[] OBJ_BYTES = OBJ_CONTENT.getBytes();
    private static TempStorageIds envStorageIds;
    private static UUID envDataPolicyId;

    @BeforeClass
    public static void startup() throws IOException {
        envDataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, false, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, envDataPolicyId, client);
    }

    @AfterClass
    public static void teardown() throws IOException {
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
        client.close();
    }

    @Before
    public void beforeRunningTestMethod() throws IOException {
        //Create a bucket and transfer object to BP using ObjectInputStreamBuilder
        HELPERS.ensureBucketExists(BUCKET_NAME, envDataPolicyId);

        final Ds3Object obj = new Ds3Object(OBJ_NAME, OBJ_BYTES.length);
        final Ds3ClientHelpers.Job job = HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(obj));

        job.transfer(new ObjectInputStreamBuilder() {

            @Override
            public InputStream buildInputStream(final String key) {
                return new ByteArrayInputStream(OBJ_BYTES);
            }
        });
    }

    @After
    public void afterRunningTestMethod() throws IOException {
        deleteAllContents(client, BUCKET_NAME);
    }

    @Test
    public void getObjectCustomParser_Test() throws IOException, URISyntaxException {
        final Ds3Object object = new Ds3Object(OBJ_NAME);
        final GetBulkJobSpectraS3Response getBulkJobSpectraS3Response = client
                .getBulkJobSpectraS3(new GetBulkJobSpectraS3Request(BUCKET_NAME, Lists.newArrayList(object)));

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final GetObjectRequest request = new GetObjectRequest(
                BUCKET_NAME,
                OBJ_NAME,
                getBulkJobSpectraS3Response.getMasterObjectList().getJobId(),
                0,
                stream);

        client.getObject(request, new Function<GetObjectParserConfiguration, GetObjectResponse>() {
            @Override
            public GetObjectResponse apply(GetObjectParserConfiguration getObjectParserConfiguration) {
                final WebResponse webResponse = getObjectParserConfiguration.getWebResponse();
                if (ResponseParserUtils.validateStatusCode(webResponse.getStatusCode(), 200, 206)) {
                    final  long objectSize = getSizeFromHeaders(webResponse.getHeaders());
                    try (final InputStream responseStream = webResponse.getResponseStream()) {
                        final long totalBytes = IOUtils.copy(
                                responseStream,
                                getObjectParserConfiguration.getDestinationChannel(),
                                getObjectParserConfiguration.getBufferSize(),
                                getObjectParserConfiguration.getObjectName(),
                                false);
                        getObjectParserConfiguration.getDestinationChannel().close();

                        if (objectSize != -1 && totalBytes != objectSize) {
                            throw new ContentLengthNotMatchException(getObjectParserConfiguration.getObjectName(), objectSize, totalBytes);
                        }

                    } catch (final IOException e) {
                        fail();
                    }
                }
                return null;
            }
        });
        assertThat(stream.toByteArray(), is(OBJ_BYTES));
    }
}
