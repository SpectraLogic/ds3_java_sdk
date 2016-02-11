package com.spectralogic.ds3client.integration.test.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.models.DataIsolationLevel;
import com.spectralogic.ds3client.models.DataPersistenceRuleType;
import com.spectralogic.ds3client.models.PoolType;
import com.spectralogic.ds3client.models.VersioningLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SignatureException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * This class provides utilities for testing the ABM commands
 */
public final class ABMTestHelper {

    final private static Logger LOG = LoggerFactory.getLogger(ABMTestHelper.class);

    /**
     * Creates a data policy with the specified name and versioning level, if a
     * policy with the same name does not currently exist. If a policy already
     * exists with the specified name, an error is thrown.
     */
    public static CreateDataPolicySpectraS3Response createDataPolicyWithVersioning(
            final String dataPolicyName,
            final VersioningLevel versioningLevel,
            final Ds3Client client) throws IOException, SignatureException {
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

    /**
     * Deletes a data policy with the specified name, and verifies that said policy
     * was deleted. If the policy was not properly deleted, then an error is thrown.
     */
    public static void deleteDataPolicy(
            final String dataPolicyName,
            final Ds3Client client) throws IOException, SignatureException {
        //Delete the data policy
        final DeleteDataPolicySpectraS3Response deleteDataPolicy = client
                .deleteDataPolicySpectraS3(new DeleteDataPolicySpectraS3Request(dataPolicyName));
        assertThat(deleteDataPolicy.getStatusCode(), is(204));

        //Verify that the data policy was deleted
        try {
            final GetDataPolicySpectraS3Response response = client
                    .getDataPolicySpectraS3(new GetDataPolicySpectraS3Request(dataPolicyName));
            fail("Data policy was not deleted as expected: " + dataPolicyName);
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            //Pass: expected data policy to not exist
        }
    }

    /**
     * Creates a pool partition with the specified name and pool type, if a
     * partition with the same name does not currently exist. If a partition
     * already exists with the specified name, an error is thrown.
     */
    public static CreatePoolPartitionSpectraS3Response createPoolPartition(
            final String poolPartitionName,
            final PoolType poolType,
            final Ds3Client client) throws IOException, SignatureException {
        //Check if pool partition already exists
        try {
            client.getPoolPartitionSpectraS3(new GetPoolPartitionSpectraS3Request(poolPartitionName));
            fail("Pool partition already exists, terminating to prevent conflict: " + poolPartitionName);
        } catch (final IOException e) {
            //Pass: expected pool partition to not exist
        }

        //Create the pool partition
        return client.createPoolPartitionSpectraS3(new CreatePoolPartitionSpectraS3Request(
                poolPartitionName,
                poolType));
    }

    /**
     * Deletes a data policy with the specified name, and verifies that said policy
     * was deleted. If the policy was not properly deleted, then an error is thrown.
     */
    public static void deletePoolPartition(
            final String poolPartitionName,
            final Ds3Client client) throws IOException, SignatureException {
        //Delete the pool partition
        final DeletePoolPartitionSpectraS3Response deletePoolPartition = client
                .deletePoolPartitionSpectraS3(new DeletePoolPartitionSpectraS3Request(poolPartitionName));
        assertThat(deletePoolPartition.getStatusCode(), is(204));

        //Verify that the pool partition was deleted
        try {
            final GetPoolPartitionSpectraS3Response response = client
                    .getPoolPartitionSpectraS3(new GetPoolPartitionSpectraS3Request(poolPartitionName));
            fail("Pool partition was not deleted as expected: " + poolPartitionName);
        } catch (final IOException e) {
            //Pass: expected pool partition to not exist
        }
    }

    /**
     * Creates a storage domain if one does not already exist with the specified name. If a
     * storage domain already exists with the specified name, an error is thrown.
     */
    public static CreateStorageDomainSpectraS3Response createStorageDomain(
            final String storageDomainName,
            final Ds3Client client) throws IOException, SignatureException {
        //Check if storage domain already exists
        try {
            client.getStorageDomainSpectraS3(new GetStorageDomainSpectraS3Request(storageDomainName));
            fail("Storage domain already exists, terminating to prevent conflict: " + storageDomainName);
        } catch (final IOException e) {
            //Pass: expected storage domain to not exist
        }

        //Create the storage domain
        return client.createStorageDomainSpectraS3(new CreateStorageDomainSpectraS3Request(storageDomainName));
    }

    /**
     * Deletes a storage domain with the specified name, and verifies that said storage
     * domain was deleted. If the domain was not properly deleted, then an error is thrown.
     */
    public static void deleteStorageDomain(
            final String storageDomainName,
            final Ds3Client client) throws IOException, SignatureException {
        //Delete the storage domain
        final DeleteStorageDomainSpectraS3Response deleteStorageDomain = client
                .deleteStorageDomainSpectraS3(new DeleteStorageDomainSpectraS3Request(storageDomainName));
        assertThat(deleteStorageDomain.getStatusCode(), is(204));

        //Verify that the storage domain was deleted
        try {
            final GetStorageDomainSpectraS3Response response = client
                    .getStorageDomainSpectraS3(new GetStorageDomainSpectraS3Request(storageDomainName));
            fail("Storage domain was not deleted as expected: " + storageDomainName);
        } catch (final IOException e) {
            //Pass: expected storage domain to not exist
        }
    }

    /**
     * Creates a storage domain member if one does not already exist between the specified
     * storage domain and pool partition. If a storage domain member already exists,an
     * error is thrown.
     */
    public static CreatePoolStorageDomainMemberSpectraS3Response createPoolStorageDomainMember(
            final UUID storageDomainId,
            final UUID poolPartitionId,
            final Ds3Client client) throws IOException, SignatureException {
        //Check if storage domain member already exists between specified storage domain and pool partition
        try {
            final GetStorageDomainMembersSpectraS3Response getMembers = client.getStorageDomainMembersSpectraS3(
                    new GetStorageDomainMembersSpectraS3Request()
                            .withPoolPartitionId(poolPartitionId)
                            .withStorageDomainId(storageDomainId));
            assertThat(getMembers.getStorageDomainMemberListResult().getStorageDomainMember().size(), is(0));
        } catch (final IOException e) {
            //Pass: expected storage domain member to not exist
        }

        //Create the storage domain
        return client.createPoolStorageDomainMemberSpectraS3(new CreatePoolStorageDomainMemberSpectraS3Request(
                poolPartitionId,
                storageDomainId));
    }

    /**
     * Deletes a storage domain member with the specified ID, and verifies that said storage
     * domain member was deleted. If the member was not properly deleted, then an error is thrown.
     */
    public static void deleteStorageDomainMember(
            final UUID memberId,
            final Ds3Client client) throws IOException, SignatureException {
        if (memberId == null) {
            LOG.error("Error: member Id was null");
            return;
        }
        //Delete the storage domain member
        final DeleteStorageDomainMemberSpectraS3Response deleteMember = client
                .deleteStorageDomainMemberSpectraS3(new DeleteStorageDomainMemberSpectraS3Request(memberId.toString()));
        assertThat(deleteMember.getStatusCode(), is(204));

        //Verify that the storage domain member was deleted
        try {
            final GetStorageDomainMemberSpectraS3Response response = client
                    .getStorageDomainMemberSpectraS3(new GetStorageDomainMemberSpectraS3Request(memberId.toString()));
            fail("Storage domain member was not deleted as expected: " + memberId.toString());
        } catch (final IOException e) {
            //Pass: expected storage domain member to not exist
        }
    }

    /**
     * Creates a data persistence rule to link the specified data policy and storage domain,
     * if said rule does not already exist.
     */
    public static CreateDataPersistenceRuleSpectraS3Response createDataPersistanceRule(
            final UUID dataPolicyId,
            final UUID storageDomainId,
            final Ds3Client client) throws IOException, SignatureException {
        //Check if data persistence rule already exists
        final GetDataPersistenceRulesSpectraS3Response response = client.getDataPersistenceRulesSpectraS3(
                new GetDataPersistenceRulesSpectraS3Request()
                        .withDataPolicyId(dataPolicyId)
                        .withStorageDomainId(storageDomainId));
        assertThat(response.getDataPersistenceRuleListResult().getDataPersistenceRule().size(), is(0));

        //Create the data persistence rule
        return client.createDataPersistenceRuleSpectraS3(new CreateDataPersistenceRuleSpectraS3Request(
                dataPolicyId,
                DataIsolationLevel.STANDARD,
                storageDomainId,
                DataPersistenceRuleType.PERMANENT));
    }

    /**
     * Deletes a data persistence rule with the specified ID, and verifies that said data
     * persistence rule was deleted. If the rule was not properly deleted, then an error is thrown.
     */
    public static void deleteDataPersistenceRule(
            final UUID dataPersistenceRuleId,
            final Ds3Client client) throws IOException, SignatureException {
        if (dataPersistenceRuleId == null) {
            LOG.error("Error: data persistence Id was null");
            return;
        }

        //Delete the data persistence rule
        final DeleteDataPersistenceRuleSpectraS3Response deleteResponse = client.deleteDataPersistenceRuleSpectraS3(
                new DeleteDataPersistenceRuleSpectraS3Request(dataPersistenceRuleId.toString()));
        assertThat(deleteResponse.getStatusCode(), is(204));

        //Verify that the data persistence rule was deleted
        try {
            final GetDataPersistenceRuleSpectraS3Response response = client.getDataPersistenceRuleSpectraS3(
                    new GetDataPersistenceRuleSpectraS3Request(dataPersistenceRuleId.toString()));
            fail("Data persistence rule was not deleted as expected: " + dataPersistenceRuleId.toString());
        } catch (final IOException e) {
            //Pass: expected data persistence rule to not exist
        }
    }
}
