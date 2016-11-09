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

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetTapesSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetTapesSpectraS3Response;
import com.spectralogic.ds3client.commands.spectrads3.RawImportTapeSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.RawImportTapeSpectraS3Response;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.Tape;
import com.spectralogic.ds3client.models.TapeState;
import com.spectralogic.ds3client.utils.Guard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.assumeVersion3_5plus;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;

public class ForeignLTFS_Test {

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String TEST_ENV_NAME = "foreign_ltfs_sdk_test";
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

    @Test
    public void foreignLTFS_Test() throws IOException {
        assumeVersion3_5plus(client);

        final GetTapesSpectraS3Request getTapesRequest = new GetTapesSpectraS3Request()
                .withState(TapeState.LTFS_WITH_FOREIGN_DATA);

        final GetTapesSpectraS3Response getTapesResponse = client.getTapesSpectraS3(getTapesRequest);
        final List<Tape> ltfsTapes = getTapesResponse.getTapeListResult().getTapes();
        assumeTrue(Guard.isNotNullAndNotEmpty(ltfsTapes));

        //Attempt to import the first of the foreign LTFS tapes
        final Tape ltfsTape = ltfsTapes.get(0);

        final String bucketName = "LTFSForeign";
        HELPERS.ensureBucketExists(bucketName, envDataPolicyId);

        final RawImportTapeSpectraS3Request importRequest = new RawImportTapeSpectraS3Request(
                bucketName,
                ltfsTape.getId());

        final RawImportTapeSpectraS3Response importResponse = client.rawImportTapeSpectraS3(importRequest);
        assertThat(importResponse.getTapeResult().getState(),
                either(is(TapeState.RAW_IMPORT_PENDING))
                        .or(is(TapeState.RAW_IMPORT_IN_PROGRESS)));
    }
}
