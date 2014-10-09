package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SignatureException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class BucketIntegration_Test {

    private static Ds3Client client;

    @BeforeClass
    public static void startup() {
        client = Util.fromEnv();
    }

    @Test
    public void createBucket() throws IOException, SignatureException {
        final String bucketName = "test_create_bucket";
        client.putBucket(new PutBucketRequest(bucketName));

        HeadBucketResponse response = null;
        try {
            response = client.headBucket(new HeadBucketRequest(bucketName));
            assertThat(response.getStatus(), is(HeadBucketResponse.Status.EXISTS));
        }
        finally {
            if (response != null) {
                client.deleteBucket(new DeleteBucketRequest(bucketName));
            }
        }
    }

    @Test
    public void deleteBucket() throws IOException, SignatureException {
        final String bucketName = "test_delete_bucket";
        client.putBucket(new PutBucketRequest(bucketName));

        HeadBucketResponse response = client.headBucket(new HeadBucketRequest(bucketName));
        assertThat(response.getStatus(), is(HeadBucketResponse.Status.EXISTS));

        client.deleteBucket(new DeleteBucketRequest(bucketName));

        response = client.headBucket(new HeadBucketRequest(bucketName));
        assertThat(response.getStatus(), is(HeadBucketResponse.Status.DOESNTEXIST));
    }

    @Test
    public void emptyBucket() throws IOException, SignatureException {
        final String bucketName = "test_empty_bucket";

        try {
            client.putBucket(new PutBucketRequest(bucketName));

            final GetBucketResponse request = client.getBucket(new GetBucketRequest(bucketName));
            final ListBucketResult result = request.getResult();
            assertThat(result.getContentsList(), is(notNullValue()));
            assertTrue(result.getContentsList().isEmpty());
        }
        finally {
            client.deleteBucket(new DeleteBucketRequest(bucketName));
        }
    }

    @Test
    public void listContents() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "test_contents_bucket";

        try {
            client.putBucket(new PutBucketRequest(bucketName));
            Util.loadBookTestData(client, bucketName);

            final GetBucketResponse response = client.getBucket(new GetBucketRequest(bucketName));

            final ListBucketResult result = response.getResult();

            assertFalse(result.getContentsList().isEmpty());
            assertThat(result.getContentsList().size(), is(4));
        }
        finally {
            Util.deleteAllContents(client, bucketName);
        }
    }
}
