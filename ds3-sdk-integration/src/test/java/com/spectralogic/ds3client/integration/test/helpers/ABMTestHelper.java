package com.spectralogic.ds3client.integration.test.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SignatureException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
    public static PutDataPolicySpectraS3Response createDataPolicyWithVersioning(
            final String dataPolicyName,
            final VersioningLevel versioningLevel,
            final Ds3Client client) throws IOException, SignatureException {
        return createDataPolicyWithVersioningAndCrcRequired(
                dataPolicyName,
                versioningLevel,
                null,
                client);
    }

    /**
     * Creates a data policy with the specified name and versioning level and checksum, if a
     * policy with the same name does not currently exist. If a policy already
     * exists with the specified name, an error is thrown.
     */
    public static PutDataPolicySpectraS3Response createDataPolicyWithVersioningAndCrcRequired(
            final String dataPolicyName,
            final VersioningLevel versioningLevel,
            final ChecksumType.Type checksumType,
            final Ds3Client client) throws IOException, SignatureException {
        //Check if data policy already exists
        try {
            client.getDataPolicySpectraS3(new GetDataPolicySpectraS3Request(dataPolicyName));
            fail("Data policy already exists, terminating to prevent conflict: " + dataPolicyName);
        } catch (final IOException e) {
            //Pass: expected data policy to not exist
        }

        if (checksumType == null) {
            //Create the data policy with versioning
            return client.putDataPolicySpectraS3(new PutDataPolicySpectraS3Request(dataPolicyName)
                    .withVersioning(versioningLevel));
        }
        //Create the data policy with versioning and checksum
        return client.putDataPolicySpectraS3(new PutDataPolicySpectraS3Request(dataPolicyName)
                .withVersioning(versioningLevel)
                .withEndToEndCrcRequired(true)
                .withChecksumType(checksumType));
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
            client.getDataPolicySpectraS3(new GetDataPolicySpectraS3Request(dataPolicyName));
            fail("Data policy was not deleted as expected: " + dataPolicyName);
        } catch (final IOException e) {
            //Pass: expected data policy to not exist
        }
    }

    /**
     * Creates a pool partition with the specified name and pool type, if a
     * partition with the same name does not currently exist. If a partition
     * already exists with the specified name, an error is thrown.
     */
    public static PutPoolPartitionSpectraS3Response createPoolPartition(
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
        return client.putPoolPartitionSpectraS3(new PutPoolPartitionSpectraS3Request(
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
            client.getPoolPartitionSpectraS3(new GetPoolPartitionSpectraS3Request(poolPartitionName));
            fail("Pool partition was not deleted as expected: " + poolPartitionName);
        } catch (final IOException e) {
            //Pass: expected pool partition to not exist
        }
    }

    /**
     * Creates a storage domain if one does not already exist with the specified name. If a
     * storage domain already exists with the specified name, an error is thrown.
     */
    public static PutStorageDomainSpectraS3Response createStorageDomain(
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
        return client.putStorageDomainSpectraS3(new PutStorageDomainSpectraS3Request(storageDomainName));
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
            client.getStorageDomainSpectraS3(new GetStorageDomainSpectraS3Request(storageDomainName));
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
    public static PutPoolStorageDomainMemberSpectraS3Response createPoolStorageDomainMember(
            final UUID storageDomainId,
            final UUID poolPartitionId,
            final Ds3Client client) throws IOException, SignatureException {
        //Check if storage domain member already exists between specified storage domain and pool partition
        try {
            final GetStorageDomainMembersSpectraS3Response getMembers = client.getStorageDomainMembersSpectraS3(
                    new GetStorageDomainMembersSpectraS3Request()
                            .withPoolPartitionId(poolPartitionId.toString())
                            .withStorageDomainId(storageDomainId.toString()));
            assertThat(getMembers.getStorageDomainMemberListResult().getStorageDomainMembers().size(), is(0));
        } catch (final IOException e) {
            //Pass: expected storage domain member to not exist
        }

        //Create the storage domain
        return client.putPoolStorageDomainMemberSpectraS3(new PutPoolStorageDomainMemberSpectraS3Request(
                poolPartitionId.toString(),
                storageDomainId.toString()));
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
                .deleteStorageDomainMemberSpectraS3(
                        new DeleteStorageDomainMemberSpectraS3Request(memberId.toString()));
        assertThat(deleteMember.getStatusCode(), is(204));

        //Verify that the storage domain member was deleted
        try {
            client.getStorageDomainMemberSpectraS3(
                    new GetStorageDomainMemberSpectraS3Request(memberId.toString()));
            fail("Storage domain member was not deleted as expected: " + memberId.toString());
        } catch (final IOException e) {
            //Pass: expected storage domain member to not exist
        }
    }

    /**
     * Creates a data persistence rule to link the specified data policy and storage domain,
     * if said rule does not already exist.
     */
    public static PutDataPersistenceRuleSpectraS3Response createDataPersistenceRule(
            final UUID dataPolicyId,
            final UUID storageDomainId,
            final Ds3Client client) throws IOException, SignatureException {
        //Check if data persistence rule already exists
        final GetDataPersistenceRulesSpectraS3Response response = client.getDataPersistenceRulesSpectraS3(
                new GetDataPersistenceRulesSpectraS3Request()
                        .withDataPolicyId(dataPolicyId.toString())
                        .withStorageDomainId(storageDomainId.toString()));
        assertThat(response.getDataPersistenceRuleListResult().getDataPersistenceRules().size(), is(0));

        //Create the data persistence rule
        return client.putDataPersistenceRuleSpectraS3(new PutDataPersistenceRuleSpectraS3Request(
                dataPolicyId.toString(),
                DataIsolationLevel.STANDARD,
                storageDomainId.toString(),
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
            client.getDataPersistenceRuleSpectraS3(
                    new GetDataPersistenceRuleSpectraS3Request(dataPersistenceRuleId.toString()));
            fail("Data persistence rule was not deleted as expected: " + dataPersistenceRuleId.toString());
        } catch (final IOException e) {
            //Pass: expected data persistence rule to not exist
        }
    }

    /**
     * Creates a group with the specified name and, if a group with the same
     * name does not currently exist. If a group exists with the specified
     * name, an error is thrown.
     */
    public static PutGroupSpectraS3Response createGroup(
            final String groupName,
            final Ds3Client client) throws IOException, SignatureException {
        //Check if group already exists
        try {
            final GetGroupSpectraS3Response response = client.getGroupSpectraS3(
                    new GetGroupSpectraS3Request(groupName));
            assertThat(response.getGroupResult(), is(nullValue()));
        } catch (final IOException e) {
            //Pass: expected group to not exist
        }

        //Create the group
        return client.putGroupSpectraS3(new PutGroupSpectraS3Request(groupName));
    }

    /**
     * Deletes a group with the specified name, and verifies that said
     * group was deleted. If the group was not properly deleted, then
     * an error is thrown.
     */
    public static void deleteGroup(
            final String groupName,
            final Ds3Client client) throws IOException, SignatureException {
        //Delete the group
        final DeleteGroupSpectraS3Response deleteResponse = client.deleteGroupSpectraS3(
                new DeleteGroupSpectraS3Request(groupName));
        assertThat(deleteResponse.getStatusCode(), is(204));

        //Verify that the group was deleted
        try {
            client.getGroupSpectraS3(new GetGroupSpectraS3Request(groupName));
            fail("Group was not deleted as expected: " + groupName);
        } catch (final IOException e) {
            //Pass: expected group to not exist
        }
    }

    /**
     * Creates a data policy acl for group to link the specified data policy and group,
     * if said rule does not already exist.
     */
    public static PutDataPolicyAclForGroupSpectraS3Response createDataPolicyAclForGroup(
            final UUID dataPolicyId,
            final UUID groupId,
            final Ds3Client client) throws IOException, SignatureException {
        //Check if data policy Acl for group already exists
        final GetDataPolicyAclsSpectraS3Response response = client.getDataPolicyAclsSpectraS3(
                new GetDataPolicyAclsSpectraS3Request()
                        .withDataPolicyId(dataPolicyId.toString())
                        .withGroupId(groupId.toString()));
        assertThat(response.getDataPolicyAclListResult().getDataPolicyAcls().size(), is(0));

        //Create the data policy Acl
        return client.putDataPolicyAclForGroupSpectraS3(new PutDataPolicyAclForGroupSpectraS3Request(
                dataPolicyId.toString(),
                groupId.toString()));
    }

    /**
     * Deletes a data policy Acl for group with the specified ID, and verifies that said
     * acl was deleted. If the acl was not properly deleted, then an error is thrown.
     */
    public static void deleteDataPolicyAclForGroup(
            final UUID aclId,
            final Ds3Client client) throws IOException, SignatureException {
        if (aclId == null) {
            LOG.error("Error: data policy Acl Id was null");
            return;
        }
        //Delete the acl
        final DeleteDataPolicyAclSpectraS3Response deleteAcl = client
                .deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(aclId.toString()));
        assertThat(deleteAcl.getStatusCode(), is(204));

        //Verify that the Acl was deleted
        try {
            client.getDataPolicyAclSpectraS3(new GetDataPolicyAclSpectraS3Request(aclId.toString()));
            fail("Data Policy Acl for Group was not deleted as expected: " + aclId.toString());
        } catch (final IOException e) {
            //Pass: expected data policy acl to not exist
        }
    }
}
