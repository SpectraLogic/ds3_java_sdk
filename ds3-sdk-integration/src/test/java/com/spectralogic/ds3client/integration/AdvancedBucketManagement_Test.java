package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.models.BucketObjectsApiBean;
import com.spectralogic.ds3client.models.DataIsolationLevel;
import com.spectralogic.ds3client.models.DataPersistenceRuleType;
import com.spectralogic.ds3client.models.VersioningLevel;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SignatureException;

import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static com.spectralogic.ds3client.integration.Util.fromEnv;
import static com.spectralogic.ds3client.integration.Util.loadBookTestData;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AdvancedBucketManagement_Test {

    private static final Logger LOG = LoggerFactory.getLogger(Smoke_Test.class);

    private static Ds3Client client;

    @BeforeClass
    public static void startup() {
        client = fromEnv();
    }

    @AfterClass
    public static void teardown() throws IOException {
        client.close();
    }

    private static CreateDataPolicySpectraS3Response createDataPolicyWithVersioning(
            final String dataPolicyName,
            final VersioningLevel versioningLevel) throws IOException, SignatureException {
        //Check if data policy already exists
        try {
            client.getDataPolicySpectraS3(new GetDataPolicySpectraS3Request(dataPolicyName));
            fail("Data policy already exists, terminating to prevent conflict: " + dataPolicyName);
        } catch (final IOException e) {
            //Pass: expected data policy to not exist
        }

        //Create the data policy
        return client.createDataPolicySpectraS3(new CreateDataPolicySpectraS3Request(dataPolicyName)
                .withVersioning(versioningLevel));
    }

    private static void deleteDataPolicy(final String dataPolicyName) throws IOException, SignatureException {
        //Delete the data policy
        final DeleteDataPolicySpectraS3Response deleteDataPolicy = client
                .deleteDataPolicySpectraS3(new DeleteDataPolicySpectraS3Request(dataPolicyName));
        assertThat(deleteDataPolicy.getStatusCode(), is(204));

        //Verify that the data policy was deleted
        try {
            client.getDataPolicySpectraS3(new GetDataPolicySpectraS3Request(dataPolicyName));
            fail("Data policy was not deleted as expected: " + dataPolicyName);
        } catch (final IOException e) {
            //Pass: expected data policy to not exist
        }
    }

    @Test
    public void createDeleteDataPolicy() throws IOException, SignatureException {
        final String dataPolicyName = "ds3_java_sdk_create_delete_data_policy";

        try {
            //Create the data policy
            final CreateDataPolicySpectraS3Response createDataPolicy = createDataPolicyWithVersioning(
                    dataPolicyName,
                    VersioningLevel.KEEP_LATEST);

            assertThat(createDataPolicy.getDataPolicyResult().getName(), is(dataPolicyName));
            assertThat(createDataPolicy.getDataPolicyResult().getVersioning(), is(VersioningLevel.KEEP_LATEST));

            //Verify that the policy exists on the black pearl
            final GetDataPolicySpectraS3Response dataPolicyResponse = client
                    .getDataPolicySpectraS3(new GetDataPolicySpectraS3Request(dataPolicyName));

            assertThat(dataPolicyResponse.getDataPolicyResult().getName(), is(dataPolicyName));
            assertThat(dataPolicyResponse.getDataPolicyResult().getVersioning(), is(VersioningLevel.KEEP_LATEST));
        } finally {
            //Delete the data policy
            deleteDataPolicy(dataPolicyName);
        }
    }

    @Test
    public void createDeleteEmptyStorageDomain() throws IOException, SignatureException {
        final String storageDomainName = "create_delete_empty_storage_domain";

        //Create a test storage domain
        final CreateStorageDomainSpectraS3Response storageDomainResponse = client
                .createStorageDomainSpectraS3(new CreateStorageDomainSpectraS3Request(storageDomainName));

        assertThat(storageDomainResponse.getStatusCode(), is(201));
        assertThat(storageDomainResponse.getStorageDomainResult().getName(), is(storageDomainName));

        //Delete the test storage domain
        final DeleteStorageDomainSpectraS3Response deleteResponse = client
                .deleteStorageDomainSpectraS3(new DeleteStorageDomainSpectraS3Request(storageDomainName));

        assertThat(deleteResponse.getStatusCode(), is(204));
    }

    @Test
    public void createBucketSpectraS3() throws IOException, SignatureException {
        final String bucketName = "create_bucket_spectra_s3";
        try {
            final CreateBucketSpectraS3Response response = client
                    .createBucketSpectraS3(new CreateBucketSpectraS3Request(bucketName));

            assertThat(response.getStatusCode(), is(201));
            assertThat(response.getBucketResult().getName(), is(bucketName));
        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    //TODO refactor
    @Test
    public void createBucketSpectraS3WithDataPolicy() throws IOException, SignatureException {
        final String bucketName = "create_bucket_spectra_s3";
        final String dataPolicyName = "create_bucket_spectra_s3_data_policy";
        final String storageDomainName = "create_bucket_spectra_s3_storage_domain";
        try {
            //Create data policy
            final CreateDataPolicySpectraS3Response dataPolicyResponse = createDataPolicyWithVersioning(
                    dataPolicyName,
                    VersioningLevel.KEEP_LATEST);

            //Create storage domain
            final CreateStorageDomainSpectraS3Response storageDomainResponse = client
                    .createStorageDomainSpectraS3(new CreateStorageDomainSpectraS3Request(storageDomainName));

            assertThat(storageDomainResponse.getStatusCode(), is(201));

            //create data persistence rule
            final CreateDataPersistenceRuleSpectraS3Response dataPersistenceResponse = client
                    .createDataPersistenceRuleSpectraS3(new CreateDataPersistenceRuleSpectraS3Request(
                            dataPolicyResponse.getDataPolicyResult().getId(),
                            DataIsolationLevel.STANDARD,
                            storageDomainResponse.getStorageDomainResult().getId(),
                            DataPersistenceRuleType.PERMANENT));

            assertThat(dataPersistenceResponse.getStatusCode(), is(201));

            //Create bucket with data policy
            final CreateBucketSpectraS3Response bucketResponse = client
                    .createBucketSpectraS3(new CreateBucketSpectraS3Request(bucketName)
                            .withDataPolicyId(dataPolicyResponse.getDataPolicyResult().getId()));


            assertThat(bucketResponse.getStatusCode(), is(201));
            assertThat(bucketResponse.getBucketResult().getName(), is(bucketName));
        } finally {
            try {
                deleteAllContents(client, bucketName);
            } catch (final IOException|SignatureException e) {
                //Pass
            }
            try {
                client.deleteStorageDomainSpectraS3(new DeleteStorageDomainSpectraS3Request(storageDomainName));
            } catch (final Exception e) {
                //Pass
            }
            deleteDataPolicy(dataPolicyName);
        }
    }

    @Test
    public void putDuplicateObjectVersioningKeepLatest() throws IOException,
            SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "duplicate_object_versioning_keep_latest";
        final String dataPolicyName = "put_duplicate_object_versioning_keep_latest";

        try {
            //Create data policy with versioning level KEEP_LATEST
            final CreateDataPolicySpectraS3Response dataPolicyResponse =
                    createDataPolicyWithVersioning(dataPolicyName, VersioningLevel.KEEP_LATEST);
            assertThat(dataPolicyResponse.getDataPolicyResult().getVersioning(), is(VersioningLevel.KEEP_LATEST));

            //Create bucket using the keep latest data policy
            client.createBucketSpectraS3(new CreateBucketSpectraS3Request(bucketName)
                    .withDataPolicyId(dataPolicyResponse.getDataPolicyResult().getId()));
            loadBookTestData(client, bucketName);

            final GetBucketResponse response = client
                    .getBucket(new GetBucketRequest(bucketName));
            final BucketObjectsApiBean result = response.getBucketObjectsApiBeanResult();
            assertFalse(result.getObjects().isEmpty());
            assertThat(result.getObjects().size(), is(4));

            //Load duplicate objects and verify no error
            loadBookTestData(client, bucketName);
            final GetBucketResponse duplicateResponse = client
                    .getBucket(new GetBucketRequest(bucketName));
            final BucketObjectsApiBean duplicateResult = duplicateResponse.getBucketObjectsApiBeanResult();
            assertFalse(duplicateResult.getObjects().isEmpty());
            assertThat(duplicateResult.getObjects().size(), is(4));
        } finally {
            try {
                deleteAllContents(client, bucketName);
            } catch (final IOException|SignatureException e) {
                //Pass
            }
            deleteDataPolicy(dataPolicyName);
        }
    }
}
