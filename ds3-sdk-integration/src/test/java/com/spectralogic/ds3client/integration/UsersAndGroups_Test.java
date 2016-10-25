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
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.BucketAclPermission;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.networking.FailedRequestException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SignatureException;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class UsersAndGroups_Test {

    private static final Logger LOG = LoggerFactory.getLogger(UsersAndGroups_Test.class);

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String BUCKET_NAME = "Users_And_Groups_Test";
    private static final String TEST_ENV_NAME = "UsersAndGroups_Test";
    private static TempStorageIds envStorageIds;
    private static UUID spectraUUID;
    private static UUID administratorsUUID;
    private static UUID dataPolicyId;

    @BeforeClass
    public static void startup() throws Exception {
        dataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, true, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, dataPolicyId, client);
        HELPERS.ensureBucketExists(BUCKET_NAME, dataPolicyId);
        setupBucket(dataPolicyId);
    }

    @AfterClass
    public static void teardown() throws IOException, SignatureException {
        deleteAllContents(client, BUCKET_NAME);
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
        client.close();
    }

    /**
     * Creates the test bucket with the specified data policy to prevent cascading test failure
     * when there are multiple data policies
     */
    private static void setupBucket(final UUID dataPolicy) {
        try {
            HELPERS.ensureBucketExists(BUCKET_NAME, dataPolicy);
            spectraUUID = client.getUserSpectraS3(
                    new GetUserSpectraS3Request("spectra")).getSpectraUserResult().getId();
            administratorsUUID = client.getGroupSpectraS3(
                    new GetGroupSpectraS3Request("Administrators")).getGroupResult().getId();
        } catch (final Exception e) {
            LOG.error("Setting up test environment failed: " + e.getMessage());
        }
    }

    @Test
    public void getUserWithUUID() throws IOException, SignatureException {

        final GetUserSpectraS3Response getSpectraResponse = client
                .getUserSpectraS3(new GetUserSpectraS3Request(spectraUUID));

        assertThat(getSpectraResponse.getSpectraUserResult().getName(), is("spectra"));
    }

    @Test
    public void getUserWithString() throws IOException, SignatureException {

        final GetUserSpectraS3Response getSpectraResponse = client
                .getUserSpectraS3(new GetUserSpectraS3Request("spectra"));

        assertThat(getSpectraResponse.getSpectraUserResult().getName(), is("spectra"));
    }

    @Test
    public void getUsers() throws IOException, SignatureException {

        final GetUsersSpectraS3Response getUsersResponse = client.getUsersSpectraS3(
                new GetUsersSpectraS3Request());

        assertThat(getUsersResponse.getSpectraUserListResult(), is(notNullValue()));
    }

    @Test
    public void getUsersWithName() throws IOException, SignatureException {

        final GetUsersSpectraS3Response getUsersResponse = client.getUsersSpectraS3(
                new GetUsersSpectraS3Request().withName("spectra"));

        assertThat(getUsersResponse.getSpectraUserListResult(), is(notNullValue()));
    }

    @Test
    public void getUsersWithAuthId() throws IOException, SignatureException {

        final GetUsersSpectraS3Response getUsersResponse = client.getUsersSpectraS3(
                new GetUsersSpectraS3Request().withAuthId("123456789"));

        assertThat(getUsersResponse.getSpectraUserListResult(), is(notNullValue()));
    }

    @Test
    public void getUsersWithDefaultDataPolicyId() throws IOException, SignatureException {

        final GetUsersSpectraS3Response getUsersResponse = client.getUsersSpectraS3(
                new GetUsersSpectraS3Request().withDefaultDataPolicyId(UUID.randomUUID()));

        assertThat(getUsersResponse.getSpectraUserListResult(), is(notNullValue()));
    }

    @Test
    public void getUsersWithLastPage() throws IOException, SignatureException {

        final GetUsersSpectraS3Response getUsersResponse = client.getUsersSpectraS3(
                new GetUsersSpectraS3Request().withLastPage(false));

        assertThat(getUsersResponse.getSpectraUserListResult(), is(notNullValue()));
    }

    @Test
    public void getUsersWithPageLength() throws IOException, SignatureException {

        final GetUsersSpectraS3Response getUsersResponse = client.getUsersSpectraS3(
                new GetUsersSpectraS3Request().withPageLength(1));

        assertThat(getUsersResponse.getSpectraUserListResult(), is(notNullValue()));
    }

    @Test
    public void getUsersWithPageOffset() throws IOException, SignatureException {

        final GetUsersSpectraS3Response getUsersResponse = client.getUsersSpectraS3(
                new GetUsersSpectraS3Request().withPageOffset(1));

        assertThat(getUsersResponse.getSpectraUserListResult(), is(notNullValue()));
    }

    @Test
    public void getUsersWithCombination() throws IOException, SignatureException {

        final GetUsersSpectraS3Response getUsersResponse = client.getUsersSpectraS3(
                new GetUsersSpectraS3Request().withAuthId("123").withDefaultDataPolicyId(UUID.randomUUID())
                        .withName("charles").withLastPage(true));

        assertThat(getUsersResponse.getSpectraUserListResult(), is(notNullValue()));
    }

    @Test
    public void ModifyUserDefaultDataPolicy() throws IOException, SignatureException {

        final GetUserSpectraS3Response getSpectraResponse = client
                .getUserSpectraS3(new GetUserSpectraS3Request(spectraUUID));
        final ModifyUserSpectraS3Response modifyUserSpectraS3Response = client
                .modifyUserSpectraS3(new ModifyUserSpectraS3Request(spectraUUID)
                        .withDefaultDataPolicyId(dataPolicyId));

        assertThat(modifyUserSpectraS3Response.getSpectraUserResult(), is(notNullValue()));
    }

    @Test
    public void ModifyUserName() throws IOException, SignatureException {

        final GetUserSpectraS3Response getSpectraResponse = client
                .getUserSpectraS3(new GetUserSpectraS3Request(spectraUUID));
        final String spectraName = getSpectraResponse.getSpectraUserResult()
                .getName();
        final ModifyUserSpectraS3Response modifyUserSpectraS3Response = client
                .modifyUserSpectraS3(new ModifyUserSpectraS3Request(spectraUUID)
                        .withName(spectraName));

        assertThat(modifyUserSpectraS3Response.getSpectraUserResult(), is(notNullValue()));
    }


    @Test
    public void ModifyUserNameAndDataPolicy() throws IOException, SignatureException {

        final GetUserSpectraS3Response getSpectraResponse = client
                .getUserSpectraS3(new GetUserSpectraS3Request(spectraUUID));
        final String spectraName = getSpectraResponse.getSpectraUserResult()
                .getName();
        final ModifyUserSpectraS3Response modifyUserSpectraS3Response = client
                .modifyUserSpectraS3(new ModifyUserSpectraS3Request(spectraUUID)
                        .withName(spectraName).withDefaultDataPolicyId(dataPolicyId));

        assertThat(modifyUserSpectraS3Response.getSpectraUserResult(), is(notNullValue()));
    }

    @Test
    public void RegenerateUserSecretKey() throws IOException, SignatureException {
        try {
            client.regenerateUserSecretKeySpectraS3(
                    new RegenerateUserSecretKeySpectraS3Request(UUID.randomUUID()));
            fail("The user for the above should not be found and throw exception.");
        } catch (final FailedRequestException e) {
            assertThat(e.getStatusCode(), is(404));
        }
    }

    @Test
    public void putGroup() throws IOException, SignatureException {
        PutGroupSpectraS3Response putGroupSpectraS3Response = null;
        try {
            putGroupSpectraS3Response = client
                    .putGroupSpectraS3(new PutGroupSpectraS3Request("test_group"));

            assertThat(putGroupSpectraS3Response.getGroupResult(), is(notNullValue()));

        } finally {
            if (putGroupSpectraS3Response != null) {
                client.deleteGroupSpectraS3(new DeleteGroupSpectraS3Request("test_group"));
            }
        }
    }

    @Test
    public void deleteGroup() throws IOException, SignatureException {
        client.putGroupSpectraS3(new PutGroupSpectraS3Request("test_group"));
        client.deleteGroupSpectraS3(new DeleteGroupSpectraS3Request("test_group"));
        //An error is thrown if the status code is not 204
    }

    @Test
    public void modifyGroup() throws IOException, SignatureException {
        UUID groupUUID = null;
        try {
            final PutGroupSpectraS3Response putGroupSpectraS3Response = client
                    .putGroupSpectraS3(new PutGroupSpectraS3Request("test_group"));
            groupUUID = putGroupSpectraS3Response.getGroupResult().getId();
            final ModifyGroupSpectraS3Response modifyGroupSpectraS3Response = client
                    .modifyGroupSpectraS3(new ModifyGroupSpectraS3Request("test_group").withName("new_name"));

            assertThat(modifyGroupSpectraS3Response.getGroupResult(), is(notNullValue()));

        } finally {
            if (groupUUID != null) {
                client.deleteGroupSpectraS3(new DeleteGroupSpectraS3Request(groupUUID.toString()));
            }
        }
    }

    @Test
    public void getGroup() throws IOException, SignatureException {
        final GetGroupSpectraS3Response getGroupSpectraS3Response = client
                .getGroupSpectraS3(new GetGroupSpectraS3Request("Everyone"));

        assertThat(getGroupSpectraS3Response.getGroupResult(), is(notNullValue()));
    }

    @Test
    public void getGroups() throws IOException, SignatureException {
        final GetGroupsSpectraS3Response getGroupsSpectraS3Response = client
                .getGroupsSpectraS3(new GetGroupsSpectraS3Request());

        assertThat(getGroupsSpectraS3Response.getGroupListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupsWithName() throws IOException, SignatureException {
        final GetGroupsSpectraS3Response getGroupsSpectraS3Response = client
                .getGroupsSpectraS3(new GetGroupsSpectraS3Request().withName("Everyone"));

        assertThat(getGroupsSpectraS3Response.getGroupListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupsWithBuiltin() throws IOException, SignatureException {
        final GetGroupsSpectraS3Response getGroupsSpectraS3Response = client
                .getGroupsSpectraS3(new GetGroupsSpectraS3Request().withBuiltIn(true));

        assertThat(getGroupsSpectraS3Response.getGroupListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupsWithCombination() throws IOException, SignatureException {
        final GetGroupsSpectraS3Response getGroupsSpectraS3Response = client
                .getGroupsSpectraS3(new GetGroupsSpectraS3Request()
                        .withBuiltIn(true).withName("Everyone"));

        assertThat(getGroupsSpectraS3Response.getGroupListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupsWithLastPage() throws IOException, SignatureException {
        final GetGroupsSpectraS3Response getGroupsSpectraS3Response = client
                .getGroupsSpectraS3(new GetGroupsSpectraS3Request().withLastPage(true));

        assertThat(getGroupsSpectraS3Response.getGroupListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupsWithPageLength() throws IOException, SignatureException {
        final GetGroupsSpectraS3Response getGroupsSpectraS3Response = client
                .getGroupsSpectraS3(new GetGroupsSpectraS3Request().withPageLength(1));

        assertThat(getGroupsSpectraS3Response.getGroupListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupsPageOffset() throws IOException, SignatureException {
        final GetGroupsSpectraS3Response getGroupsSpectraS3Response = client
                .getGroupsSpectraS3(new GetGroupsSpectraS3Request().withPageOffset(1));
        assertThat(getGroupsSpectraS3Response.getGroupListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupsWithPageStartMarker() throws IOException, SignatureException {
        try {
            client.getGroupsSpectraS3(new GetGroupsSpectraS3Request()
                    .withPageStartMarker(UUID.randomUUID()));
            fail("The above should not be found and throw exception.");
        } catch (final FailedRequestException e) {
            assertThat(e.getStatusCode(), is(410));
        }
    }

    @Test
    public void createGroupGroupMember() throws IOException, SignatureException {
        PutGroupSpectraS3Response putGroupSpectraS3Response = null;
        PutGroupGroupMemberSpectraS3Response putGroupGroupMemberSpectraS3Response = null;
        try {
            putGroupSpectraS3Response = client.putGroupSpectraS3(
                    new PutGroupSpectraS3Request("test_group"));
            putGroupGroupMemberSpectraS3Response  = client
                    .putGroupGroupMemberSpectraS3(new PutGroupGroupMemberSpectraS3Request("test_group", "Administrators"));

            assertThat(putGroupGroupMemberSpectraS3Response.getGroupMemberResult(), is(notNullValue()));

        } finally {
            if (putGroupGroupMemberSpectraS3Response != null) {
                client.deleteGroupMemberSpectraS3(
                        new DeleteGroupMemberSpectraS3Request(putGroupGroupMemberSpectraS3Response
                                .getGroupMemberResult().getId().toString()));
            }

            if (putGroupSpectraS3Response != null) {
                client.deleteGroupSpectraS3(new DeleteGroupSpectraS3Request("test_group"));
            }
        }
    }

    @Test
    public void deleteGroupGroupMember() throws IOException, SignatureException {
        PutGroupSpectraS3Response putGroupSpectraS3Response = null;
        PutGroupGroupMemberSpectraS3Response putGroupGroupMemberSpectraS3Response = null;
        try {
            putGroupSpectraS3Response = client.putGroupSpectraS3(
                    new PutGroupSpectraS3Request("test_group"));

            putGroupGroupMemberSpectraS3Response  = client
                    .putGroupGroupMemberSpectraS3(new PutGroupGroupMemberSpectraS3Request(
                            "test_group", "Administrators"));

            final DeleteGroupMemberSpectraS3Response deleteGroupMemberSpectraS3Response = client
                    .deleteGroupMemberSpectraS3(new DeleteGroupMemberSpectraS3Request(
                            putGroupGroupMemberSpectraS3Response.getGroupMemberResult().getId().toString()));
            //Will throw error if response code is not 204
            assertThat(deleteGroupMemberSpectraS3Response, is(notNullValue()));

        } catch (final Exception e) {
            if (putGroupGroupMemberSpectraS3Response != null) {
                client.deleteGroupMemberSpectraS3(
                        new DeleteGroupMemberSpectraS3Request(putGroupGroupMemberSpectraS3Response
                                .getGroupMemberResult().getId().toString()));
            } else {
                assert false;
            }
            throw e;
        } finally {
            if (putGroupSpectraS3Response != null) {
                client.deleteGroupSpectraS3(new DeleteGroupSpectraS3Request("test_group"));
            }
        }
    }

    @Test
    public void getGroupGroupMember() throws IOException, SignatureException {
        PutGroupSpectraS3Response putGroupSpectraS3Response = null;
        PutGroupGroupMemberSpectraS3Response putGroupGroupMemberSpectraS3Response = null;
        try {
            putGroupSpectraS3Response = client.putGroupSpectraS3(
                    new PutGroupSpectraS3Request("test_group"));

            putGroupGroupMemberSpectraS3Response  = client
                    .putGroupGroupMemberSpectraS3(new PutGroupGroupMemberSpectraS3Request(
                            "test_group", "Administrators"));

            final GetGroupMemberSpectraS3Response getGroupMemberSpectraS3Response = client
                    .getGroupMemberSpectraS3(new GetGroupMemberSpectraS3Request(
                            putGroupGroupMemberSpectraS3Response.getGroupMemberResult().getId().toString()));

            assertThat(getGroupMemberSpectraS3Response.getGroupMemberResult(), is(notNullValue()));

        } finally {
            if (putGroupGroupMemberSpectraS3Response != null) {
                client.deleteGroupMemberSpectraS3(
                        new DeleteGroupMemberSpectraS3Request(putGroupGroupMemberSpectraS3Response
                                .getGroupMemberResult().getId().toString()));
            }

            if (putGroupSpectraS3Response != null) {
                client.deleteGroupSpectraS3(new DeleteGroupSpectraS3Request("test_group"));
            }
        }
    }

    @Test
    public void getGroupGroupMembers() throws IOException, SignatureException {

        final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                    .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request());

        assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupGroupMembersWithUUID() throws IOException, SignatureException {

        final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                        .withGroupId(administratorsUUID));

        assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupGroupMembersWithStringID() throws IOException, SignatureException {

        final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                        .withGroupId("Administrators"));

        assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));
    }

    @Test
    public void getUserGroupMembersWithMemberUserIDString() throws IOException, SignatureException {

        final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                        .withMemberUserId("spectra"));

        assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));
    }

    @Test
    public void getUserGroupMembersWithMemberUserUUID() throws IOException, SignatureException {

        final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                        .withMemberUserId(spectraUUID));

        assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupGroupMembersWithGroupIDString() throws IOException, SignatureException {
        PutGroupSpectraS3Response putGroupSpectraS3Response = null;
        PutGroupGroupMemberSpectraS3Response putGroupGroupMemberSpectraS3Response = null;
        try {
            putGroupSpectraS3Response = client.putGroupSpectraS3(
                    new PutGroupSpectraS3Request("test_group"));

            putGroupGroupMemberSpectraS3Response  = client
                    .putGroupGroupMemberSpectraS3(new PutGroupGroupMemberSpectraS3Request(
                            "test_group", "Administrators"));

            final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                    .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                            .withMemberGroupId("test_group"));

            assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));

        } finally {
            if (putGroupGroupMemberSpectraS3Response != null) {
                client.deleteGroupMemberSpectraS3(
                        new DeleteGroupMemberSpectraS3Request(putGroupGroupMemberSpectraS3Response
                                .getGroupMemberResult().getId().toString()));
            }

            if (putGroupSpectraS3Response != null) {
                client.deleteGroupSpectraS3(new DeleteGroupSpectraS3Request("test_group"));
            }
        }
    }

    @Test
    public void getGroupGroupMembersWithPageLength() throws IOException, SignatureException {

        final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                        .withPageLength(1));

        assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupGroupMembersWithPageOffset() throws IOException, SignatureException {

        final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                        .withPageOffset(1));

        assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupGroupMembersWithLastPage() throws IOException, SignatureException {

        final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                        .withLastPage(true));

        assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupGroupMembersWithPageStartMarkerUUID() throws IOException, SignatureException {

        final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                        .withPageStartMarker(UUID.randomUUID()));

        assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupGroupMembersWithPageStartMarkerString() throws IOException, SignatureException {

        final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                        .withPageStartMarker(UUID.randomUUID().toString()));

        assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupGroupMembersWithCombination() throws IOException, SignatureException {

        final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                        .withMemberUserId(spectraUUID).withGroupId("Administrators"));

        assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));
    }

    @Test
    public void getGroupGroupMembersWithGroupUUID() throws IOException, SignatureException {
        PutGroupSpectraS3Response putGroupSpectraS3Response = null;
        PutGroupGroupMemberSpectraS3Response putGroupGroupMemberSpectraS3Response = null;
        try {
            putGroupSpectraS3Response = client.putGroupSpectraS3(
                    new PutGroupSpectraS3Request("test_group"));

            putGroupGroupMemberSpectraS3Response  = client
                    .putGroupGroupMemberSpectraS3(new PutGroupGroupMemberSpectraS3Request(
                            "test_group", "Administrators"));

            final GetGroupMembersSpectraS3Response getGroupMembersSpectraS3Response = client
                    .getGroupMembersSpectraS3(new GetGroupMembersSpectraS3Request()
                            .withMemberGroupId(putGroupSpectraS3Response.getGroupResult().getId()));

            assertThat(getGroupMembersSpectraS3Response.getGroupMemberListResult(), is(notNullValue()));

        } finally {
            if (putGroupGroupMemberSpectraS3Response != null) {
                client.deleteGroupMemberSpectraS3(
                        new DeleteGroupMemberSpectraS3Request(putGroupGroupMemberSpectraS3Response
                                .getGroupMemberResult().getId().toString()));
            }
            if (putGroupSpectraS3Response != null) {
                client.deleteGroupSpectraS3(new DeleteGroupSpectraS3Request("test_group"));
            }
        }
    }

    @Test
    public void putUserGroupMember() throws IOException, SignatureException {
        PutGroupSpectraS3Response putGroupSpectraS3Response = null;
        PutUserGroupMemberSpectraS3Response putUserGroupMemberSpectraS3Response = null;
        try {
            putGroupSpectraS3Response = client.putGroupSpectraS3(
                    new PutGroupSpectraS3Request("test_group"));

            putUserGroupMemberSpectraS3Response = client
                    .putUserGroupMemberSpectraS3(new PutUserGroupMemberSpectraS3Request("test_group", "spectra"));

            assertThat(putUserGroupMemberSpectraS3Response.getGroupMemberResult(), is(notNullValue()));

        } finally {
            if (putUserGroupMemberSpectraS3Response != null) {
                client.deleteGroupMemberSpectraS3(
                        new DeleteGroupMemberSpectraS3Request(putUserGroupMemberSpectraS3Response
                                .getGroupMemberResult().getId().toString()));
            }
            if (putGroupSpectraS3Response != null) {
                client.deleteGroupSpectraS3(new DeleteGroupSpectraS3Request("test_group"));
            }
        }
    }

    @Test
    public void putBucketAclForUser() throws IOException, SignatureException {
        PutBucketAclForUserSpectraS3Response putBucketAclForUserSpectraS3Response = null;
        try {
            putBucketAclForUserSpectraS3Response = client.putBucketAclForUserSpectraS3(
                    new PutBucketAclForUserSpectraS3Request(BUCKET_NAME, BucketAclPermission.DELETE,
                            spectraUUID));

            assertThat(putBucketAclForUserSpectraS3Response.getBucketAclResult(), is(notNullValue()));
        } finally {
            if (putBucketAclForUserSpectraS3Response != null) {
                client.deleteBucketAclSpectraS3(new DeleteBucketAclSpectraS3Request(
                        putBucketAclForUserSpectraS3Response.getBucketAclResult().getId().toString()));
            }
        }
    }

    @Test
    public void putBucketAclForGroup() throws IOException, SignatureException {
        PutBucketAclForGroupSpectraS3Response putBucketAclForGroupSpectraS3Response = null;
        try {
            putBucketAclForGroupSpectraS3Response = client.putBucketAclForGroupSpectraS3(
                    new PutBucketAclForGroupSpectraS3Request(BUCKET_NAME, "Administrators",
                            BucketAclPermission.DELETE));

            assertThat(putBucketAclForGroupSpectraS3Response.getBucketAclResult(), is(notNullValue()));

        } finally {
            if (putBucketAclForGroupSpectraS3Response != null) {
                client.deleteBucketAclSpectraS3(new DeleteBucketAclSpectraS3Request(
                        putBucketAclForGroupSpectraS3Response.getBucketAclResult().getId().toString()));
            }
        }
    }

    @Test
    public void putGlobalBucketAclForUserUUID() throws IOException, SignatureException {
        PutGlobalBucketAclForUserSpectraS3Response putGlobalBucketAclForUserSpectraS3Response = null;
        try {
            putGlobalBucketAclForUserSpectraS3Response = client.putGlobalBucketAclForUserSpectraS3(
                    new PutGlobalBucketAclForUserSpectraS3Request(BucketAclPermission.DELETE,
                            spectraUUID));

            assertThat(putGlobalBucketAclForUserSpectraS3Response.getBucketAclResult(), is(notNullValue()));

        } finally {
            if (putGlobalBucketAclForUserSpectraS3Response != null) {
                client.deleteBucketAclSpectraS3(new DeleteBucketAclSpectraS3Request(
                        putGlobalBucketAclForUserSpectraS3Response.getBucketAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void putGlobalBucketAclForUserString() throws IOException, SignatureException {
        PutGlobalBucketAclForUserSpectraS3Response putGlobalBucketAclForUserSpectraS3Response = null;
        try {
            putGlobalBucketAclForUserSpectraS3Response = client.putGlobalBucketAclForUserSpectraS3(
                    new PutGlobalBucketAclForUserSpectraS3Request(BucketAclPermission.DELETE,
                            "spectra"));

            assertThat(putGlobalBucketAclForUserSpectraS3Response.getBucketAclResult(), is(notNullValue()));

        } finally {
            if (putGlobalBucketAclForUserSpectraS3Response != null) {
                client.deleteBucketAclSpectraS3(new DeleteBucketAclSpectraS3Request(
                        putGlobalBucketAclForUserSpectraS3Response.getBucketAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void putGlobalBucketAclForGroupString() throws IOException, SignatureException {
        PutGlobalBucketAclForGroupSpectraS3Response putGlobalBucketAclForGroupSpectraS3Response = null;
        try {
            putGlobalBucketAclForGroupSpectraS3Response = client.putGlobalBucketAclForGroupSpectraS3(
                    new PutGlobalBucketAclForGroupSpectraS3Request("Administrators",
                            BucketAclPermission.DELETE));

            assertThat(putGlobalBucketAclForGroupSpectraS3Response.getBucketAclResult(), is(notNullValue()));

        } finally {
            if (putGlobalBucketAclForGroupSpectraS3Response != null) {
                client.deleteBucketAclSpectraS3(new DeleteBucketAclSpectraS3Request(
                        putGlobalBucketAclForGroupSpectraS3Response.getBucketAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void putGlobalBucketAclForGroupUUID() throws IOException, SignatureException {
        PutGlobalBucketAclForGroupSpectraS3Response putGlobalBucketAclForGroupSpectraS3Response = null;
        try {
            putGlobalBucketAclForGroupSpectraS3Response = client.putGlobalBucketAclForGroupSpectraS3(
                    new PutGlobalBucketAclForGroupSpectraS3Request(administratorsUUID,
                            BucketAclPermission.DELETE));

            assertThat(putGlobalBucketAclForGroupSpectraS3Response.getBucketAclResult(), is(notNullValue()));

        } finally {
            if (putGlobalBucketAclForGroupSpectraS3Response != null) {
                client.deleteBucketAclSpectraS3(new DeleteBucketAclSpectraS3Request(
                        putGlobalBucketAclForGroupSpectraS3Response.getBucketAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void deleteBucketAcl() throws IOException, SignatureException {
        final PutBucketAclForUserSpectraS3Response putBucketAclForUserSpectraS3Response = client.putBucketAclForUserSpectraS3(
                new PutBucketAclForUserSpectraS3Request(BUCKET_NAME, BucketAclPermission.DELETE, spectraUUID));

        client.deleteBucketAclSpectraS3(new DeleteBucketAclSpectraS3Request(
                putBucketAclForUserSpectraS3Response.getBucketAclResult().getId().toString()));
        //An error is thrown if response code is not 204
    }

    @Test
    public void getBucketAcl() throws IOException, SignatureException {
        PutBucketAclForUserSpectraS3Response putBucketAclForUserSpectraS3Response = null;
        try {
            putBucketAclForUserSpectraS3Response = client.putBucketAclForUserSpectraS3(
                    new PutBucketAclForUserSpectraS3Request(BUCKET_NAME, BucketAclPermission.DELETE,
                            spectraUUID));
            final GetBucketAclSpectraS3Response getBucketAclSpectraS3Response = client
                    .getBucketAclSpectraS3(new GetBucketAclSpectraS3Request(
                            putBucketAclForUserSpectraS3Response.getBucketAclResult().getId().toString()));

            assertThat(getBucketAclSpectraS3Response.getBucketAclResult(), is(notNullValue()));

        } finally {
            if (putBucketAclForUserSpectraS3Response != null) {
                client.deleteBucketAclSpectraS3(new DeleteBucketAclSpectraS3Request(
                        putBucketAclForUserSpectraS3Response.getBucketAclResult().getId().toString()));
            }
        }
    }

    @Test
    public void getBucketAcls() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request());

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithUserUUID() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request().withUserId(spectraUUID));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithUserIDString() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request().withUserId("spectra"));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithGroupUUID() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                        .withGroupId(administratorsUUID));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithGroupIDString() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                        .withGroupId("Administrators"));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithPermission() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                        .withPermission(BucketAclPermission.OWNER));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithPageOffset() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                        .withPageOffset(1));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithPageLength() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                        .withPageLength(1));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithLastPage() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                        .withLastPage(true));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithPageStartMarkerUUID() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                        .withPageStartMarker(UUID.randomUUID()));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithPageStartMarkerString() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                        .withPageStartMarker(UUID.randomUUID().toString()));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithBucketIDString() throws IOException, SignatureException {
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                        .withBucketId(BUCKET_NAME));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void getBucketAclsWithCombination() throws IOException, SignatureException {

        final UUID bucketUUID = client.getBucketSpectraS3(
                new GetBucketSpectraS3Request(BUCKET_NAME)).getBucketResult().getId();
        final GetBucketAclsSpectraS3Response getBucketAclsSpectraS3Response = client
                .getBucketAclsSpectraS3(new GetBucketAclsSpectraS3Request()
                        .withBucketId(bucketUUID.toString()).withUserId(spectraUUID));

        assertThat(getBucketAclsSpectraS3Response.getBucketAclListResult(), is(notNullValue()));
    }

    @Test
    public void putGlobalDataPolicyAclForUserUUID() throws IOException, SignatureException {
        PutGlobalDataPolicyAclForUserSpectraS3Response putGlobalDataPolicyAclForUserSpectraS3Response
                = null;
        try {
            putGlobalDataPolicyAclForUserSpectraS3Response = client
                    .putGlobalDataPolicyAclForUserSpectraS3(
                    new PutGlobalDataPolicyAclForUserSpectraS3Request(spectraUUID));

            assertThat(putGlobalDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult(), is(notNullValue()));

        } finally {
            if (putGlobalDataPolicyAclForUserSpectraS3Response != null) {
                client.deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(
                        putGlobalDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void putGlobalDataPolicyAclForUserString() throws IOException, SignatureException {
        PutGlobalDataPolicyAclForUserSpectraS3Response putGlobalDataPolicyAclForUserSpectraS3Response = null;
        try {
            putGlobalDataPolicyAclForUserSpectraS3Response = client.putGlobalDataPolicyAclForUserSpectraS3(
                    new PutGlobalDataPolicyAclForUserSpectraS3Request("spectra"));

            assertThat(putGlobalDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult(), is(notNullValue()));

        } finally {
            if (putGlobalDataPolicyAclForUserSpectraS3Response != null) {
                client.deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(
                        putGlobalDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void putGlobalDataPolicyAclForGroupUUID() throws IOException, SignatureException {
        PutGlobalDataPolicyAclForGroupSpectraS3Response putGlobalDataPolicyAclForGroupSpectraS3Response
                = null;
        try {
            putGlobalDataPolicyAclForGroupSpectraS3Response = client
                    .putGlobalDataPolicyAclForGroupSpectraS3(
                    new PutGlobalDataPolicyAclForGroupSpectraS3Request(administratorsUUID));

            assertThat(putGlobalDataPolicyAclForGroupSpectraS3Response.getDataPolicyAclResult(), is(notNullValue()));

        } finally {
            if (putGlobalDataPolicyAclForGroupSpectraS3Response != null) {
                client.deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(
                        putGlobalDataPolicyAclForGroupSpectraS3Response.getDataPolicyAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void putGlobalDataPolicyAclForGroupString() throws IOException, SignatureException {
        PutGlobalDataPolicyAclForGroupSpectraS3Response putGlobalDataPolicyAclForGroupSpectraS3Response
                = null;
        try {
            putGlobalDataPolicyAclForGroupSpectraS3Response = client
                    .putGlobalDataPolicyAclForGroupSpectraS3(
                    new PutGlobalDataPolicyAclForGroupSpectraS3Request("Administrators"));

            assertThat(putGlobalDataPolicyAclForGroupSpectraS3Response.getDataPolicyAclResult(), is(notNullValue()));

        } finally {
            if (putGlobalDataPolicyAclForGroupSpectraS3Response != null) {
                client.deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(
                        putGlobalDataPolicyAclForGroupSpectraS3Response.getDataPolicyAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void deleteDataPolicyAcl() throws IOException, SignatureException {
        PutGlobalDataPolicyAclForUserSpectraS3Response putGlobalDataPolicyAclForUserSpectraS3Response
                = null;
        try {
            putGlobalDataPolicyAclForUserSpectraS3Response = client.putGlobalDataPolicyAclForUserSpectraS3(
                    new PutGlobalDataPolicyAclForUserSpectraS3Request("spectra"));

            final DeleteDataPolicyAclSpectraS3Response deleteDataPolicyAclSpectraS3Response = client
                    .deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(
                    putGlobalDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult()
                            .getId().toString()));

            //An error will be thrown if response code is not 204
            assertThat(deleteDataPolicyAclSpectraS3Response, is(notNullValue()));

        } catch (final Exception e) {
            if (putGlobalDataPolicyAclForUserSpectraS3Response != null) {
                client.deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(
                        putGlobalDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult()
                                .getId().toString()));
            }
            throw e;
        }
    }

    @Test
    public void getDataPolicyAcl() throws IOException, SignatureException {
        PutGlobalDataPolicyAclForUserSpectraS3Response putGlobalDataPolicyAclForUserSpectraS3Response
                = null;
        try {
            putGlobalDataPolicyAclForUserSpectraS3Response = client
                    .putGlobalDataPolicyAclForUserSpectraS3(
                    new PutGlobalDataPolicyAclForUserSpectraS3Request("spectra"));

            final GetDataPolicyAclSpectraS3Response getDataPolicyAclSpectraS3Response = client
                    .getDataPolicyAclSpectraS3(new GetDataPolicyAclSpectraS3Request(
                            putGlobalDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult()
                                    .getId().toString()));

            assertThat(getDataPolicyAclSpectraS3Response.getDataPolicyAclResult(), is(notNullValue()));

        } finally {
            if (putGlobalDataPolicyAclForUserSpectraS3Response != null) {
                client.deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(
                        putGlobalDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void putUserDataPolicyAclWithUUID() throws IOException, SignatureException {
        PutDataPolicyAclForUserSpectraS3Response putDataPolicyAclForUserSpectraS3Response = null;
        try {
            putDataPolicyAclForUserSpectraS3Response = client.putDataPolicyAclForUserSpectraS3(
                    new PutDataPolicyAclForUserSpectraS3Request(dataPolicyId, spectraUUID));

              assertThat(putDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult(), is(notNullValue()));

        } finally {
            if (putDataPolicyAclForUserSpectraS3Response != null) {
                client.deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(
                        putDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void putUserDataPolicyAclWithString() throws IOException, SignatureException {
        PutDataPolicyAclForUserSpectraS3Response putDataPolicyAclForUserSpectraS3Response = null;
        try {
            putDataPolicyAclForUserSpectraS3Response = client.putDataPolicyAclForUserSpectraS3(
                    new PutDataPolicyAclForUserSpectraS3Request(dataPolicyId.toString(),
                            spectraUUID.toString()));

            assertThat(putDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult(), is(notNullValue()));

        } finally {
            if (putDataPolicyAclForUserSpectraS3Response != null) {
                client.deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(
                        putDataPolicyAclForUserSpectraS3Response.getDataPolicyAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void putGroupDataPolicyAclWithUUID() throws IOException, SignatureException {
        PutDataPolicyAclForGroupSpectraS3Response putDataPolicyAclForGroupSpectraS3Response = null;
        try {
            putDataPolicyAclForGroupSpectraS3Response = client.putDataPolicyAclForGroupSpectraS3(
                    new PutDataPolicyAclForGroupSpectraS3Request(dataPolicyId, administratorsUUID));

            assertThat(putDataPolicyAclForGroupSpectraS3Response.getDataPolicyAclResult(), is(notNullValue()));

        } finally {
            if (putDataPolicyAclForGroupSpectraS3Response != null) {
                client.deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(
                        putDataPolicyAclForGroupSpectraS3Response.getDataPolicyAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void putGroupDataPolicyAclWithString() throws IOException, SignatureException {
        PutDataPolicyAclForGroupSpectraS3Response putDataPolicyAclForGroupSpectraS3Response = null;
        try {
            putDataPolicyAclForGroupSpectraS3Response = client.putDataPolicyAclForGroupSpectraS3(
                    new PutDataPolicyAclForGroupSpectraS3Request(dataPolicyId.toString(),
                            administratorsUUID.toString()));

            assertThat(putDataPolicyAclForGroupSpectraS3Response.getDataPolicyAclResult(), is(notNullValue()));

        } finally {
            if (putDataPolicyAclForGroupSpectraS3Response != null) {
                client.deleteDataPolicyAclSpectraS3(new DeleteDataPolicyAclSpectraS3Request(
                        putDataPolicyAclForGroupSpectraS3Response.getDataPolicyAclResult()
                                .getId().toString()));
            }
        }
    }

    @Test
    public void getDataPolicyAcls() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request());

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithUserUUID() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withUserId(spectraUUID));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithUserString() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withUserId("spectra"));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithGroupUUID() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withGroupId(administratorsUUID));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithGroupString() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withGroupId("Administrators"));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithDataPolicyUUID() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withDataPolicyId(dataPolicyId));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithDataPolicyUUIDString() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withDataPolicyId(dataPolicyId.toString()));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithPageOffset() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withPageOffset(1));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithPageLength() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withPageLength(1));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithPageStartMarkerString() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withPageStartMarker(UUID.randomUUID().toString()));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithPageStartMarkerUUID() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withPageStartMarker(UUID.randomUUID()));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithLastPage() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withLastPage(true));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }

    @Test
    public void getDataPolicyAclsWithCombination() throws IOException, SignatureException {

        final GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3Response = client
                .getDataPolicyAclsSpectraS3(new GetDataPolicyAclsSpectraS3Request()
                        .withUserId(spectraUUID).withDataPolicyId(dataPolicyId));

        assertThat(getDataPolicyAclsSpectraS3Response.getDataPolicyAclListResult(), is(notNullValue()));
    }
}