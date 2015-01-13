package com.spectralogic.ds3client.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SignatureException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.DeleteBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.commands.HeadBucketRequest;
import com.spectralogic.ds3client.commands.HeadBucketResponse;
import com.spectralogic.ds3client.commands.PutBucketRequest;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

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
			assertThat(response.getStatus(),
					is(HeadBucketResponse.Status.EXISTS));
		} finally {
			if (response != null) {
				client.deleteBucket(new DeleteBucketRequest(bucketName));
			}
		}
	}

	@Test
	public void deleteBucket() throws IOException, SignatureException {
		final String bucketName = "test_delete_bucket";
		client.putBucket(new PutBucketRequest(bucketName));

		HeadBucketResponse response = client.headBucket(new HeadBucketRequest(
				bucketName));
		assertThat(response.getStatus(), is(HeadBucketResponse.Status.EXISTS));

		client.deleteBucket(new DeleteBucketRequest(bucketName));

		response = client.headBucket(new HeadBucketRequest(bucketName));
		assertThat(response.getStatus(),
				is(HeadBucketResponse.Status.DOESNTEXIST));
	}

	@Test
	public void emptyBucket() throws IOException, SignatureException {
		final String bucketName = "test_empty_bucket";

		try {
			client.putBucket(new PutBucketRequest(bucketName));

			final GetBucketResponse request = client
					.getBucket(new GetBucketRequest(bucketName));
			final ListBucketResult result = request.getResult();
			assertThat(result.getContentsList(), is(notNullValue()));
			assertTrue(result.getContentsList().isEmpty());
		} finally {
			client.deleteBucket(new DeleteBucketRequest(bucketName));
		}
	}

	@Test
	public void listContents() throws IOException, SignatureException,
			XmlProcessingException, URISyntaxException {
		final String bucketName = "test_contents_bucket";

		try {
			client.putBucket(new PutBucketRequest(bucketName));
			Util.loadBookTestData(client, bucketName);

			final GetBucketResponse response = client
					.getBucket(new GetBucketRequest(bucketName));

			final ListBucketResult result = response.getResult();

			assertFalse(result.getContentsList().isEmpty());
			assertThat(result.getContentsList().size(), is(4));
		} finally {
			Util.deleteAllContents(client, bucketName);
		}
	}

	@Test
	public void negativeDeleteNonEmptyBucket() throws IOException,
			SignatureException, XmlProcessingException, URISyntaxException {
		final String bucketName = "negative_test_delete_non_empty_bucket";

		try {
			// Create bucket and put objects (4 book .txt files) to it
			client.putBucket(new PutBucketRequest(bucketName));
			Util.loadBookTestData(client, bucketName);

			final GetBucketResponse get_response = client
					.getBucket(new GetBucketRequest(bucketName));
			final ListBucketResult get_result = get_response.getResult();
			assertFalse(get_result.getContentsList().isEmpty());
			assertThat(get_result.getContentsList().size(), is(4));

			// Attempt to delete bucket and catch expected
			// FailedRequestException
			try {
				client.deleteBucket(new DeleteBucketRequest(bucketName));
				fail("Should have thrown a FailedRequestException when trying to delete a non-empty bucket.");
			} catch (FailedRequestException e) {
				assertTrue(409 == e.getStatusCode());
			}
		} finally {
			Util.deleteAllContents(client, bucketName);
		}
	}

	@Test
	public void negativeCreateBucketNameConflict() throws SignatureException,
			IOException {
		final String bucketName = "negative_test_create_bucket_duplicate_name";

		client.putBucket(new PutBucketRequest(bucketName));

		// Attempt to create a bucket with a name conflicting with an existing
		// bucket
		try {
			client.putBucket(new PutBucketRequest(bucketName));
			fail("Should have thrown a FailedRequestException when trying to create a bucket with a duplicate name.");
		} catch (FailedRequestException e) {
			assertTrue(409 == e.getStatusCode());
		} finally {
			client.deleteBucket(new DeleteBucketRequest(bucketName));
		}
	}
	
	@Test
	public void negativeDeleteNonExistentBucket() throws IOException,
			SignatureException {
		final String bucketName = "negative_test_delete_non_existent_bucket";

		// Attempt to delete bucket and catch expected FailedRequestException
		try {
			client.deleteBucket(new DeleteBucketRequest(bucketName));
			fail("Should have thrown a FailedRequestException when trying to delete a non-existent bucket.");
		} catch (FailedRequestException e) {
			assertTrue(404 == e.getStatusCode());
		}
	}

	@Test
	public void negativePutDuplicateObject() throws SignatureException,
			IOException, XmlProcessingException, URISyntaxException {
		final String bucketName = "negative_test_put_duplicate_object";

		try {
			client.putBucket(new PutBucketRequest(bucketName));
			Util.loadBookTestData(client, bucketName);

			final GetBucketResponse response = client
					.getBucket(new GetBucketRequest(bucketName));
			final ListBucketResult result = response.getResult();
			assertFalse(result.getContentsList().isEmpty());
			assertThat(result.getContentsList().size(), is(4));

			try {
				Util.loadBookTestData(client, bucketName);
				fail("Should have thrown a FailedRequestException when trying to put duplicate objects.");
			} catch (FailedRequestException e) {
				assertTrue(409 == e.getStatusCode());
			}
		} finally {
			Util.deleteAllContents(client, bucketName);
		}
	}
}
