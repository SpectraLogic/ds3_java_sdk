package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.models.BucketObjectsApiBean;
import com.spectralogic.ds3client.models.PoolType;
import com.spectralogic.ds3client.models.StorageDomainMember;
import com.spectralogic.ds3client.models.VersioningLevel;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.util.List;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.*;
import static com.spectralogic.ds3client.integration.test.helpers.ABMTestHelper.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class AdvancedBucketManagement_Test {

    private static Ds3Client client;

    @BeforeClass
    public static void startup() {
        client = fromEnv();
    }

    @AfterClass
    public static void teardown() throws IOException {
        client.close();
    }

    @Test
    public void createDeleteDataPolicy() throws IOException, SignatureException {
        final String dataPolicyName = "ds3_java_sdk_create_delete_data_policy";

        try {
            //Create the data policy
            final CreateDataPolicySpectraS3Response createDataPolicy = createDataPolicyWithVersioning(
                    dataPolicyName,
                    VersioningLevel.KEEP_LATEST,
                    client);

            assertThat(createDataPolicy.getDataPolicyResult().getName(), is(dataPolicyName));
            assertThat(createDataPolicy.getDataPolicyResult().getVersioning(), is(VersioningLevel.KEEP_LATEST));

            //Verify that the policy exists on the black pearl
            final GetDataPolicySpectraS3Response dataPolicyResponse = client
                    .getDataPolicySpectraS3(new GetDataPolicySpectraS3Request(dataPolicyName));

            assertThat(dataPolicyResponse.getDataPolicyResult().getName(), is(dataPolicyName));
            assertThat(dataPolicyResponse.getDataPolicyResult().getVersioning(), is(VersioningLevel.KEEP_LATEST));
        } finally {
            //Delete the data policy
            deleteDataPolicy(dataPolicyName, client);
        }
    }

    @Test
    public void createDeleteEmptyStorageDomain() throws IOException, SignatureException {
        final String storageDomainName = "create_delete_empty_storage_domain";

        try {
            //Create a test storage domain
            final CreateStorageDomainSpectraS3Response storageDomainResponse = createStorageDomain(
                    storageDomainName,
                    client);

            assertThat(storageDomainResponse.getStatusCode(), is(201));
            assertThat(storageDomainResponse.getStorageDomainResult().getName(), is(storageDomainName));
        } finally {
            //Delete the test storage domain
            deleteStorageDomain(storageDomainName, client);
        }
    }

    @Test
    public void createDeletePoolPartition() throws IOException, SignatureException {
        final String poolPartitionName = "create_delete_pool_partition";

        try {
            //Create the pool partition
            final CreatePoolPartitionSpectraS3Response createPoolResponse = createPoolPartition(
                    poolPartitionName,
                    PoolType.ONLINE,
                    client);

            assertThat(createPoolResponse.getStatusCode(), is(201));
            assertThat(createPoolResponse.getPoolPartitionResult().getName(), is(poolPartitionName));

            //Get the pool partition to verify that was created on the BP
            final GetPoolPartitionSpectraS3Response getPoolResponse = client
                    .getPoolPartitionSpectraS3(new GetPoolPartitionSpectraS3Request(poolPartitionName));

            assertThat(getPoolResponse.getStatusCode(), is(200));
            assertThat(getPoolResponse.getPoolPartitionResult().getName(), is(poolPartitionName));
        } finally {
            //Delete the pool partition
            deletePoolPartition(poolPartitionName, client);
        }
    }

    @Test
    public void createPoolStorageDomainMember_Test() throws IOException, SignatureException {
        final String storageDomainName = "create_pool_storage_domain_member_sd";
        final String poolPartitionName = "create_pool_storage_domain_member_pp";
        try {
            //Create storage domain
            final CreateStorageDomainSpectraS3Response createStorageDomain = createStorageDomain(
                    storageDomainName,
                    client);

            //Create pool partition
            final CreatePoolPartitionSpectraS3Response createPoolPartition = createPoolPartition(
                    poolPartitionName,
                    PoolType.ONLINE,
                    client);

            //Create storage domain member linking pool partition to storage domain
            final CreatePoolStorageDomainMemberSpectraS3Response createMemberResponse = createPoolStorageDomainMember(
                    createStorageDomain.getStorageDomainResult().getId(),
                    createPoolPartition.getPoolPartitionResult().getId(),
                    client);
            assertThat(createMemberResponse.getStatusCode(), is(201));

            //Verify that the storage domain member exists
            final GetStorageDomainMembersSpectraS3Response getMembers = client.getStorageDomainMembersSpectraS3(
                    new GetStorageDomainMembersSpectraS3Request()
                            .withPoolPartitionId(createPoolPartition.getPoolPartitionResult().getId())
                            .withStorageDomainId(createStorageDomain.getStorageDomainResult().getId()));

            final List<StorageDomainMember> members = getMembers.getStorageDomainMemberListResult().getStorageDomainMember();
            assertThat(members.size(), is(1));
            assertThat(members.get(0).getPoolPartitionId(), is(createPoolPartition.getPoolPartitionResult().getId()));
            assertThat(members.get(0).getStorageDomainId(), is(createStorageDomain.getStorageDomainResult().getId()));

            //Delete the storage domain member
            deleteStorageDomainMember(createMemberResponse.getStorageDomainMemberResult().getId(), client);
        } finally {
            deletePoolPartition(poolPartitionName, client);
            deleteStorageDomain(storageDomainName, client);
        }
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

    @Test
    public void createBucketSpectraS3WithDataPolicy() throws IOException, SignatureException {
        final String bucketName = "create_bucket_spectra_s3";
        final String dataPolicyName = "create_bucket_spectra_s3_data_policy";
        final String storageDomainName = "create_bucket_spectra_s3_storage_domain";
        final String poolPartitionName = "create_bucket_spectra_s3_pool_partition";
        UUID storageDomainMemberId = null;
        UUID dataPersistenceRuleId = null;
        try {
            //Create data policy
            final CreateDataPolicySpectraS3Response dataPolicyResponse = createDataPolicyWithVersioning(
                    dataPolicyName,
                    VersioningLevel.KEEP_LATEST,
                    client);

            //Create storage domain
            final CreateStorageDomainSpectraS3Response storageDomainResponse = createStorageDomain(
                    storageDomainName,
                    client);

            //Create pool partition
            final CreatePoolPartitionSpectraS3Response poolPartitionResponse = createPoolPartition(
                    poolPartitionName,
                    PoolType.ONLINE,
                    client);

            //Create storage domain member linking pool partition to storage domain
            final CreatePoolStorageDomainMemberSpectraS3Response memberResponse = createPoolStorageDomainMember(
                    storageDomainResponse.getStorageDomainResult().getId(),
                    poolPartitionResponse.getPoolPartitionResult().getId(),
                    client);
            storageDomainMemberId = memberResponse.getStorageDomainMemberResult().getId();

            //create data persistence rule
            final CreateDataPersistenceRuleSpectraS3Response dataPersistenceResponse = createDataPersistenceRule(
                    dataPolicyResponse.getDataPolicyResult().getId(),
                    storageDomainResponse.getStorageDomainResult().getId(),
                    client);
            dataPersistenceRuleId = dataPersistenceResponse.getDataPersistenceRuleResult().getDataPolicyId();

            //Create bucket with data policy
            final CreateBucketSpectraS3Response bucketResponse = client
                    .createBucketSpectraS3(new CreateBucketSpectraS3Request(bucketName)
                            .withDataPolicyId(dataPolicyResponse.getDataPolicyResult().getId()));


            assertThat(bucketResponse.getStatusCode(), is(201));
            assertThat(bucketResponse.getBucketResult().getName(), is(bucketName));
        } finally {
            deleteAllContents(client, bucketName);
            deleteDataPersistenceRule(dataPersistenceRuleId, client);
            deleteDataPolicy(dataPolicyName, client);
            deleteStorageDomainMember(storageDomainMemberId, client);
            deleteStorageDomain(storageDomainName, client);
            deletePoolPartition(poolPartitionName, client);
        }
    }

    @Test
    public void putDuplicateObjectVersioningKeepLatest() throws IOException,
            SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "duplicate_object_versioning_keep_latest";
        final String dataPolicyName = "duplicate_object_versioning_keep_latest_dp";
        final String storageDomainName = "duplicate_object_versioning_keep_latest_sd";
        final String poolPartitionName = "duplicate_object_versioning_keep_latest_pp";

        UUID storageDomainMemberId = null;
        UUID dataPersistenceRuleId = null;
        try {
            //Create data policy
            final CreateDataPolicySpectraS3Response dataPolicyResponse = createDataPolicyWithVersioning(
                    dataPolicyName,
                    VersioningLevel.KEEP_LATEST,
                    client);

            //Create storage domain
            final CreateStorageDomainSpectraS3Response storageDomainResponse = createStorageDomain(
                    storageDomainName,
                    client);

            //Create pool partition
            final CreatePoolPartitionSpectraS3Response poolPartitionResponse = createPoolPartition(
                    poolPartitionName,
                    PoolType.ONLINE,
                    client);

            //Create storage domain member linking pool partition to storage domain
            final CreatePoolStorageDomainMemberSpectraS3Response memberResponse = createPoolStorageDomainMember(
                    storageDomainResponse.getStorageDomainResult().getId(),
                    poolPartitionResponse.getPoolPartitionResult().getId(),
                    client);
            storageDomainMemberId = memberResponse.getStorageDomainMemberResult().getId();

            //create data persistence rule
            final CreateDataPersistenceRuleSpectraS3Response dataPersistenceResponse = createDataPersistenceRule(
                    dataPolicyResponse.getDataPolicyResult().getId(),
                    storageDomainResponse.getStorageDomainResult().getId(),
                    client);
            dataPersistenceRuleId = dataPersistenceResponse.getDataPersistenceRuleResult().getDataPolicyId();

            //Create bucket with data policy
            client.createBucketSpectraS3(new CreateBucketSpectraS3Request(bucketName)
                            .withDataPolicyId(dataPolicyResponse.getDataPolicyResult().getId()));

            //Load the set of books and verify they exist
            loadBookTestData(client, bucketName);

            final GetBucketResponse response1 = client
                    .getBucket(new GetBucketRequest(bucketName));
            final BucketObjectsApiBean result1 = response1.getBucketObjectsApiBeanResult();
            assertFalse(result1.getObjects().isEmpty());
            assertThat(result1.getObjects().size(), is(4));

            //Load the set of books a second time, and verify no errors
            final GetBucketResponse response2 = client
                    .getBucket(new GetBucketRequest(bucketName));
            final BucketObjectsApiBean result2 = response2.getBucketObjectsApiBeanResult();
            assertFalse(result2.getObjects().isEmpty());
            assertThat(result2.getObjects().size(), is(4));

        } finally {
            deleteAllContents(client, bucketName);
            deleteDataPersistenceRule(dataPersistenceRuleId, client);
            deleteDataPolicy(dataPolicyName, client);
            deleteStorageDomainMember(storageDomainMemberId, client);
            deleteStorageDomain(storageDomainName, client);
            deletePoolPartition(poolPartitionName, client);
        }
    }

    @Test
    public void putDuplicateObjectVersioningNone() throws IOException,
            SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "duplicate_object_versioning_none";
        final String dataPolicyName = "duplicate_object_versioning_none_dp";
        final String storageDomainName = "duplicate_object_versioning_none_sd";
        final String poolPartitionName = "duplicate_object_versioning_none_pp";

        UUID storageDomainMemberId = null;
        UUID dataPersistenceRuleId = null;
        try {
            //Create data policy
            final CreateDataPolicySpectraS3Response dataPolicyResponse = createDataPolicyWithVersioning(
                    dataPolicyName,
                    VersioningLevel.NONE,
                    client);

            //Create storage domain
            final CreateStorageDomainSpectraS3Response storageDomainResponse = createStorageDomain(
                    storageDomainName,
                    client);

            //Create pool partition
            final CreatePoolPartitionSpectraS3Response poolPartitionResponse = createPoolPartition(
                    poolPartitionName,
                    PoolType.ONLINE,
                    client);

            //Create storage domain member linking pool partition to storage domain
            final CreatePoolStorageDomainMemberSpectraS3Response memberResponse = createPoolStorageDomainMember(
                    storageDomainResponse.getStorageDomainResult().getId(),
                    poolPartitionResponse.getPoolPartitionResult().getId(),
                    client);
            storageDomainMemberId = memberResponse.getStorageDomainMemberResult().getId();

            //create data persistence rule
            final CreateDataPersistenceRuleSpectraS3Response dataPersistenceResponse = createDataPersistenceRule(
                    dataPolicyResponse.getDataPolicyResult().getId(),
                    storageDomainResponse.getStorageDomainResult().getId(),
                    client);
            dataPersistenceRuleId = dataPersistenceResponse.getDataPersistenceRuleResult().getDataPolicyId();

            //Create bucket with data policy
            client.createBucketSpectraS3(new CreateBucketSpectraS3Request(bucketName)
                            .withDataPolicyId(dataPolicyResponse.getDataPolicyResult().getId()));

            //Load the set of books and verify they exist
            loadBookTestData(client, bucketName);

            final GetBucketResponse response1 = client
                    .getBucket(new GetBucketRequest(bucketName));
            final BucketObjectsApiBean result1 = response1.getBucketObjectsApiBeanResult();
            assertFalse(result1.getObjects().isEmpty());
            assertThat(result1.getObjects().size(), is(4));

            //Load the set of books a second time, and verify that exception is thrown
            try {
                loadBookTestData(client, bucketName);
                fail("Should have thrown a FailedRequestException when trying to put duplicate objects.");
            } catch (final FailedRequestException e) {
                assertTrue(409 == e.getStatusCode());
            }

        } finally {
            deleteAllContents(client, bucketName);
            deleteDataPersistenceRule(dataPersistenceRuleId, client);
            deleteDataPolicy(dataPolicyName, client);
            deleteStorageDomainMember(storageDomainMemberId, client);
            deleteStorageDomain(storageDomainName, client);
            deletePoolPartition(poolPartitionName, client);
        }
    }
}
