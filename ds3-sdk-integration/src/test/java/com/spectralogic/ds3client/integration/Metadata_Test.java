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

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.HeadObjectRequest;
import com.spectralogic.ds3client.commands.HeadObjectResponse;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.PutObjectResponse;
import com.spectralogic.ds3client.commands.spectrads3.PutBulkJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.PutBulkJobSpectraS3Response;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import com.spectralogic.ds3client.utils.MetadataStringManipulation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class Metadata_Test {

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String TEST_ENV_NAME = "metadata_test";
    private static TempStorageIds envStorageIds;
    private static UUID envDataPolicyId;

    private static final String STRING_WITH_SYMBOLS = "1234567890-!@#$%^&*()_+`~[]\\{}|;':\"./<>?∞πϊφξ";
    private static final String STRING_WITH_SYMBOLS_UNICODE = "1234567890-!@#$%^&*()_+`~[]\\{}|;':\"./<>?\u221E\u03C0\u03CA\u03D5\u03BE";
    private static final String STRING_WITH_UPPERCASE_SYMBOLS = "A1234567890-!@#$%^&*()_+`~[]\\{}|;':\"./<>?∞πϊφξ";
    private static final String STRING_WITH_UPPERCASE_SYMBOLS_UNICODE = "\u00411234567890-!@#$%^&*()_+`~[]\\{}|;':\"./<>?∞πϊφξ";

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
    public void singleMetadataField() throws IOException {

        final Metadata metadata = processMetadataRequest("metadataBucket", ImmutableMultimap.of("name", "value"));

        final List<String> values = metadata.get("name");
        assertThat(values, is(notNullValue()));
        assertFalse(values.isEmpty());
        assertThat(values.size(), is(1));
        assertThat(values.get(0), is("value"));

    }

    @Test
    public void singleUppercaseMetadataField() throws IOException {
        final String metadataKey = "Name";
        final String metadataValue = "Value";

        final Metadata metadata = processMetadataRequest("metadataBucket", ImmutableMultimap.of(metadataKey, metadataValue));

        final List<String> values = metadata.get(metadataKey.toLowerCase());
        assertThat(values, is(notNullValue()));
        assertFalse(values.isEmpty());
        assertThat(values.size(), is(1));
        assertThat(values.get(0), is(metadataValue));

    }

    @Test
    public void multipleMetadataFields() throws IOException {

        final Metadata metadata = processMetadataRequest("metadataBucket", ImmutableMultimap.of("name", "value", "key2", "value2"));

        final Set<String> keys = metadata.keys();

        assertThat(keys.size(), is(2));
        assertTrue(keys.contains("name"));
        assertTrue(keys.contains("key2"));

        final List<String> values = metadata.get("name");
        assertThat(values, is(notNullValue()));
        assertFalse(values.isEmpty());
        assertThat(values.size(), is(1));
        assertThat(values.get(0), is("value"));

    }

    @Test
    public void escapedMetadataWithSpaces() throws IOException {
        final String stringWithSpaces = "percent encoded space";
        final String escapedWithSpaces = MetadataStringManipulation.toEncodedString(stringWithSpaces);

        final Metadata metadata = processMetadataRequest(
                "metadataBucket",
                ImmutableMultimap.of(escapedWithSpaces, escapedWithSpaces));

        final Set<String> keys = metadata.keys();

        assertThat(keys.size(), is(1));
        assertTrue(keys.contains(escapedWithSpaces));

        final List<String> values = metadata.get(escapedWithSpaces);
        assertThat(values, is(notNullValue()));
        assertFalse(values.isEmpty());
        assertThat(values.size(), is(1));
        assertThat(values.get(0), is(escapedWithSpaces));
    }

    @Test
    public void escapedMetadataWithSymbols() throws IOException {
        final String escapedWithSymbols = MetadataStringManipulation.toEncodedString(STRING_WITH_SYMBOLS);

        final Metadata metadata = processMetadataRequest(
                "metadataBucket",
                ImmutableMultimap.of(escapedWithSymbols, escapedWithSymbols));

        final Set<String> keys = metadata.keys();

        assertThat(keys.size(), is(1));
        //key is returned with all lower case from BP, which can be decoded
        //but does not match the encoded string which uses uppercase hex for percent encoding
        assertTrue(keys.contains(escapedWithSymbols.toLowerCase()));

        final List<String> values = metadata.get(escapedWithSymbols.toLowerCase());
        assertThat(values, is(notNullValue()));
        assertFalse(values.isEmpty());
        assertThat(values.size(), is(1));
        assertThat(values.get(0), is(escapedWithSymbols));
    }

    @Test
    public void metadataValueWithSpaces() throws IOException {
        final String key = "percent encoded space";
        final String value = "one two three";

        final Metadata metadata = processMetadataRequest(
                "metadataBucket",
                ImmutableMultimap.of(key, value));

        final Set<String> keys = metadata.keys();

        assertThat(keys.size(), is(1));
        assertTrue(keys.contains(key));

        final List<String> values = metadata.get(key);
        assertThat(values, is(notNullValue()));
        assertFalse(values.isEmpty());
        assertThat(values.size(), is(1));
        assertThat(values.get(0), is(value));
    }

    @Test
    public void metadataValueWithSymbols() throws IOException {

        final Metadata metadata = processMetadataRequest(
                "metadataBucket",
                ImmutableMultimap.of(STRING_WITH_SYMBOLS, STRING_WITH_SYMBOLS));

        final Set<String> keys = metadata.keys();

        assertThat(keys.size(), is(1));
        assertTrue(keys.contains(STRING_WITH_SYMBOLS));
        assertThat(STRING_WITH_SYMBOLS, is(STRING_WITH_SYMBOLS));

        final List<String> values = metadata.get(STRING_WITH_SYMBOLS);
        assertThat(values, is(notNullValue()));
        assertFalse(values.isEmpty());
        assertThat(values.size(), is(1));
        assertThat(values.get(0), is(STRING_WITH_SYMBOLS));
    }

    @Test
    public void metadataValueWithUnicodeSymbols() throws IOException {

        final Metadata metadata = processMetadataRequest(
                "metadataBucket",
                ImmutableMultimap.of(STRING_WITH_SYMBOLS_UNICODE, STRING_WITH_SYMBOLS_UNICODE));

        final Set<String> keys = metadata.keys();

        assertThat(keys.size(), is(1));
        assertTrue(keys.contains(STRING_WITH_SYMBOLS_UNICODE));
        assertThat(STRING_WITH_SYMBOLS_UNICODE, is(STRING_WITH_SYMBOLS_UNICODE));

        final List<String> values = metadata.get(STRING_WITH_SYMBOLS_UNICODE);
        assertThat(values, is(notNullValue()));
        assertFalse(values.isEmpty());
        assertThat(values.size(), is(1));
        assertThat(values.get(0), is(STRING_WITH_SYMBOLS_UNICODE));
    }

    @Test
    public void metadataValueWithUppercaseSymbols() throws IOException {

        final Metadata metadata = processMetadataRequest(
                "metadataBucket",
                ImmutableMultimap.of(STRING_WITH_UPPERCASE_SYMBOLS, STRING_WITH_UPPERCASE_SYMBOLS));

        final Set<String> keys = metadata.keys();

        assertThat(keys.size(), is(1));

        for (final String key : keys) {
            assertEquals(key.length(), STRING_WITH_UPPERCASE_SYMBOLS.length());
            assertEquals(0, StringUtils.indexOfDifference(key, STRING_WITH_UPPERCASE_SYMBOLS));
        }
    }

    @Test
    public void metadataValueWithUppercaseUnicodeSymbols() throws IOException {

        final Metadata metadata = processMetadataRequest(
                "metadataBucket",
                ImmutableMultimap.of(STRING_WITH_UPPERCASE_SYMBOLS_UNICODE, STRING_WITH_UPPERCASE_SYMBOLS_UNICODE));

        final Set<String> keys = metadata.keys();

        assertThat(keys.size(), is(1));

        for (final String key : keys) {
            assertEquals(key.length(), STRING_WITH_UPPERCASE_SYMBOLS_UNICODE.length());
            assertEquals(0, StringUtils.indexOfDifference(key, STRING_WITH_UPPERCASE_SYMBOLS_UNICODE));
        }
    }

    /*
    //TODO There is currently a limitation in BP where it does not handle metadata values that have the same key.
    @Test
    public void multipleMetadataFieldsForSameKey() throws IOException {

        final Metadata metadata = processMetadataRequest("metadataBucket", ImmutableMultimap.of("name", "value", "name", "value2"));

        final Set<String> keys = metadata.keys();

        assertThat(keys.size(), is(1));
        assertTrue(keys.contains("name"));

        final List<String> values = metadata.get("name");
        assertThat(values, is(notNullValue()));
        assertFalse(values.isEmpty());
        assertThat(values, hasItem("value"));
    }

    @Test
    public void multipleMetadataFieldsForSameKeyDifferentCase() throws IOException {

        final Metadata metadata = processMetadataRequest("metadataBucket", ImmutableMultimap.of("name", "value", "Name", "value2"));

        final Set<String> keys = metadata.keys();

        assertThat(keys.size(), is(1));
        assertTrue(keys.contains("name"));

        final List<String> values = metadata.get("name");
        assertThat(values, is(notNullValue()));
        assertFalse(values.isEmpty());
        assertThat(values, hasItem("value"));
    }
    */

    private static Metadata processMetadataRequest(final String bucketName, final ImmutableMultimap<String, String> metadata) throws IOException {
        HELPERS.ensureBucketExists(bucketName, envDataPolicyId);

        try {
            final Ds3Object obj = new Ds3Object("obj.txt", 1024);
            final PutBulkJobSpectraS3Response putResponse = client.putBulkJobSpectraS3(new PutBulkJobSpectraS3Request(bucketName, Lists.newArrayList(obj)));
            final PutObjectRequest request = new PutObjectRequest(
                    bucketName,
                    obj.getName(),
                    buildRandomChannel(1024),
                    putResponse.getMasterObjectList().getJobId().toString(),
                    0,
                    1024);

            for (final Map.Entry<String, String> entry : metadata.entries()) {
                request.withMetaData(entry.getKey(), entry.getValue());
            }

            final PutObjectResponse putObjResponse = client.putObject(request);

            assertThat(putObjResponse, is(notNullValue()));

            final HeadObjectResponse response = client.headObject(new HeadObjectRequest(bucketName, obj.getName()));

            assertThat(response.getStatus(), is(HeadObjectResponse.Status.EXISTS));

            return response.getMetadata();

        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    private static SeekableByteChannel buildRandomChannel(int length) throws IOException {

        final byte[] randomData = IOUtils.toByteArray(new RandomDataInputStream(System.currentTimeMillis(), length));
        final ByteBuffer randomBuffer = ByteBuffer.wrap(randomData);

        final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(length);
        channel.write(randomBuffer);

        return channel;
    }
}
