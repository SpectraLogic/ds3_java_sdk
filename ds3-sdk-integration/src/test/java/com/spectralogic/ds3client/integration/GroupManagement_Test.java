package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.models.BucketAclPermission;
import com.spectralogic.ds3client.models.VersioningLevel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.security.SignatureException;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static com.spectralogic.ds3client.integration.Util.fromEnv;
import static com.spectralogic.ds3client.integration.test.helpers.ABMTestHelper.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GroupManagement_Test {

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
    public void createDeleteGroup_Test() throws IOException, SignatureException {
        final String groupName = "create_delete_group_sdk_test";

        try {
            //Create the group
            final PutGroupSpectraS3Response groupResponse = createGroup(groupName, client);
            assertThat(groupResponse.getStatusCode(), is(201));
            assertThat(groupResponse.getGroupResult().getName(), is(groupName));

            //Verify that the group exists on the BP
            final GetGroupSpectraS3Response getGroup = client.getGroupSpectraS3(
                    new GetGroupSpectraS3Request(groupName));
            assertThat(getGroup.getGroupResult().getName(), is(groupName));
        } finally {
            //Delete the group
            deleteGroup(groupName, client);
        }
    }

    @Test
    public void createDataPolicyForGroup_Test() throws IOException, SignatureException {
        final String groupName = "create_data_policy_for_group_g";
        final String dataPolicyName = "create_data_policy_for_group_dp";
        UUID aclId = null;

        try {
            //Create the group
            final PutGroupSpectraS3Response createGroup = createGroup(groupName, client);
            assertThat(createGroup.getGroupResult().getName(), is(groupName));

            //Create the data policy
            final PutDataPolicySpectraS3Response createDataPolicy = createDataPolicyWithVersioning(
                    dataPolicyName,
                    VersioningLevel.KEEP_LATEST,
                    client);
            assertThat(createDataPolicy.getDataPolicyResult().getName(), is(dataPolicyName));

            //Create the data policy Acl for group
            final PutDataPolicyAclForGroupSpectraS3Response createAcl = createDataPolicyAclForGroup(
                    createDataPolicy.getDataPolicyResult().getId(),
                    createGroup.getGroupResult().getId(),
                    client);
            assertThat(createAcl.getStatusCode(), is(201));

            //Verify that Acl was created
            final GetDataPolicyAclsSpectraS3Response verifyAcl = client.getDataPolicyAclsSpectraS3(
                    new GetDataPolicyAclsSpectraS3Request()
                            .withDataPolicyId(createDataPolicy.getDataPolicyResult().getId())
                            .withGroupId(createGroup.getGroupResult().getId()));

            assertThat(verifyAcl.getDataPolicyAclListResult().getDataPolicyAcls().size(), is(1));
            aclId = verifyAcl.getDataPolicyAclListResult().getDataPolicyAcls().get(0).getId();

        } finally {
            deleteDataPolicyAclForGroup(aclId, client);
            deleteGroup(groupName, client);
            deleteDataPolicy(dataPolicyName, client);
        }
    }

    @Test
    public void createGroupGroupMember_Test() throws IOException, SignatureException {
        final String parentGroupName = "create_group_group_member_parent";
        final String childGroupName = "create_group_group_member_child";

        try {
            //Create the parent group
            final PutGroupSpectraS3Response createParent = createGroup(parentGroupName, client);
            assertThat(createParent.getStatusCode(), is(201));
            assertThat(createParent.getGroupResult().getName(), is(parentGroupName));

            //Create the child group
            final PutGroupSpectraS3Response createChild = createGroup(childGroupName, client);
            assertThat(createChild.getStatusCode(), is(201));
            assertThat(createChild.getGroupResult().getName(), is(childGroupName));

            //Create group-group member
            final UUID parentId = createParent.getGroupResult().getId();
            final UUID childId = createChild.getGroupResult().getId();

            final PutGroupGroupMemberSpectraS3Response groupGroup = client
                    .putGroupGroupMemberSpectraS3(new PutGroupGroupMemberSpectraS3Request(
                            parentId,
                            childId));
            assertThat(groupGroup.getStatusCode(), is(201));
            assertThat(groupGroup.getGroupMemberResult().getGroupId(), is(parentId));
            assertThat(groupGroup.getGroupMemberResult().getMemberGroupId(), is(childId));

            //Delete group-group member
            final DeleteGroupMemberSpectraS3Response deleteGroupGroup = client.deleteGroupMemberSpectraS3(
                    new DeleteGroupMemberSpectraS3Request(groupGroup.getGroupMemberResult().getId().toString()));
            assertThat(deleteGroupGroup.getStatusCode(), is(204));

            //Verify that group-group member was deleted
            final GetGroupMembersSpectraS3Response groupMembers = client
                    .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                            .withGroupId(parentId));
            assertThat(groupMembers.getGroupMemberListResult().getGroupMembers().size(), is(0));
        } finally {
            //Delete the groups
            deleteGroup(childGroupName, client);
            deleteGroup(parentGroupName, client);
        }
    }

    @Test
    public void createBucketAclForGroup_Test() throws IOException, SignatureException {
        final String bucketName = "create_bucket_acl_for_group_b";
        final String groupName = "create_bucket_acl_for_group_g";

        try {
            //Create bucket
            final PutBucketSpectraS3Response createBucket = client
                    .putBucketSpectraS3(new PutBucketSpectraS3Request(bucketName));
            assertThat(createBucket.getBucketResult().getName(), is(bucketName));

            //Create group
            final PutGroupSpectraS3Response createGroup = createGroup(groupName, client);
            assertThat(createGroup.getGroupResult().getName(), is(groupName));

            //Create acl between bucket and group
            final PutBucketAclForGroupSpectraS3Response createAcl = client
                    .putBucketAclForGroupSpectraS3(new PutBucketAclForGroupSpectraS3Request(
                            bucketName,
                            createGroup.getGroupResult().getId(),
                            BucketAclPermission.READ));

            //Verify that acl exists
            final GetBucketAclsSpectraS3Response getAcls = client
                    .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                            .withBucketId(bucketName)
                            .withGroupId(createGroup.getGroupResult().getId()));
            assertThat(getAcls.getBucketAclListResult().getBucketAcls().size(), is(1));
            assertThat(getAcls.getBucketAclListResult().getBucketAcls().get(0).getPermission(),
                    is(BucketAclPermission.READ));

            //Delete acl
            final DeleteBucketAclSpectraS3Response deleteAcl = client.deleteBucketAclSpectraS3(
                    new DeleteBucketAclSpectraS3Request(createAcl.getBucketAclResult().getId().toString()));
            assertThat(deleteAcl.getStatusCode(), is(204));

            //Verify the acl was deleted
            final GetBucketAclsSpectraS3Response verifyDelete = client
                    .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                            .withBucketId(bucketName)
                            .withGroupId(createGroup.getGroupResult().getId()));
            assertThat(verifyDelete.getBucketAclListResult().getBucketAcls().size(), is(0));
        } finally {
            deleteAllContents(client, bucketName);
            deleteGroup(groupName, client);
        }
    }
}
