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
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class Metadata_Test {

    private static Ds3Client client;
    private static final String TEST_ENV_NAME = "metadata_test";
    private static TempStorageIds envStorageIds;

    @BeforeClass
    public static void startup() throws IOException, SignatureException {
        client = Util.fromEnv();
        final UUID dataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, true, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, dataPolicyId, client);
    }

    @AfterClass
    public static void teardown() throws IOException, SignatureException {
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
        client.close();
    }

    @Test
    public void singleMetadataField() throws IOException, SignatureException, XmlProcessingException {

        final Metadata metadata = processMetadataRequest("metadataBucket", ImmutableMultimap.of("name", "value"));

        final List<String> values = metadata.get("name");
        assertThat(values, is(notNullValue()));
        assertFalse(values.isEmpty());
        assertThat(values.size(), is(1));
        assertThat(values.get(0), is("value"));

    }

    @Test
    public void multipleMetadataFields() throws IOException, SignatureException, XmlProcessingException {

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

    /*
    //TODO There is currently a limitation in BP where it does not handle metadata values that have the same key.
    @Test
    public void multipleMetadataFieldsForSameKey() throws XmlProcessingException, SignatureException, IOException {

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
    public void multipleMetadataFieldsForSameKeyDifferentCase() throws XmlProcessingException, SignatureException, IOException {

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

    private static Metadata processMetadataRequest(final String bucketName, final ImmutableMultimap<String, String> metadata) throws IOException, SignatureException, XmlProcessingException {
        final Ds3ClientHelpers wrapper = Ds3ClientHelpers.wrap(client);
        wrapper.ensureBucketExists(bucketName);

        try {
            final Ds3Object obj = new Ds3Object("obj.txt", 1024);
            final PutBulkJobSpectraS3Response putResponse = client.putBulkJobSpectraS3(new PutBulkJobSpectraS3Request(bucketName, Lists.newArrayList(obj)));
            final PutObjectRequest request = new PutObjectRequest(
                    bucketName,
                    obj.getName(),
                    buildRandomChannel(1024),
                    putResponse.getResult().getJobId(),
                    0,
                    1024);

            for (final Map.Entry<String, String> entry : metadata.entries()) {
                request.withMetaData(entry.getKey(), entry.getValue());
            }

            final PutObjectResponse putObjResponse = client.putObject(request);

            assertThat(putObjResponse.getStatusCode(), is(200));

            final HeadObjectResponse response = client.headObject(new HeadObjectRequest(bucketName, obj.getName()));

            assertThat(response.getStatusCode(), is(200));

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
