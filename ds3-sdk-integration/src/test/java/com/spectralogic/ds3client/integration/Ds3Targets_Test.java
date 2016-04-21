package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.BucketAclPermission;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.VersioningLevel;
import com.spectralogic.ds3client.helpers.FileObjectPutter;
import com.spectralogic.ds3client.models.DataIsolationLevel;
import com.spectralogic.ds3client.models.DataPersistenceRuleType;
import com.spectralogic.ds3client.models.DataReplicationRuleType;
import com.spectralogic.ds3client.models.Ds3Target;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.common.Credentials;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Objects;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static com.spectralogic.ds3client.integration.test.helpers.ABMTestHelper.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Ds3Targets_Test {

    private static Ds3Client client;
    private static final String TEST_ENV_NAME = "ds3_targets_test";
    private static TempStorageIds envStorageIds;
    private static UUID dataPolicyId;

    final String targetClientURL = "sm4u-26.eng.sldomain.com";
    final static String targetClientName = "sm4u-26";
    final String targetClientAccessKey = "c3BlY3RyYQ==";
    final String targetClientSecretKey = "YRjsJjsh";
    private static String targetUUIDString;

    @BeforeClass
    public static void startup() throws IOException, SignatureException {
        client = Util.fromEnv();
        dataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, false, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, dataPolicyId, client);
    }

    @Before
    public void ensureDS3TargetExists() throws IOException, SignatureException {
        final GetDs3TargetsSpectraS3Response getDs3TargetsSpectraS3Response = client
                .getDs3TargetsSpectraS3(new GetDs3TargetsSpectraS3Request());

        targetUUIDString = null;
        for (final Ds3Target ds3Target : getDs3TargetsSpectraS3Response.getDs3TargetListResult().getDs3Targets()){
            if (Objects.equals(ds3Target.getName(), targetClientName)){
                targetUUIDString = ds3Target.getId().toString();
                return;
            }
        }

        final RegisterDs3TargetSpectraS3Response registerDs3TargetSpectraS3Response = client
                .registerDs3TargetSpectraS3(new RegisterDs3TargetSpectraS3Request(
                        targetClientAccessKey, targetClientSecretKey, targetClientURL, targetClientName)
                        .withDataPathHttps(false));
        targetUUIDString = registerDs3TargetSpectraS3Response.getDs3TargetResult().getId().toString();
        return;
    }

    @AfterClass
    public static void teardown() throws IOException, SignatureException {
        client.deleteDs3TargetSpectraS3(new DeleteDs3TargetSpectraS3Request(targetClientName));
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
        client.close();
    }

    @Test
    public void registerDS3Target_Test() throws IOException, SignatureException {
        client.deleteDs3TargetSpectraS3(new DeleteDs3TargetSpectraS3Request(targetClientName));

        final RegisterDs3TargetSpectraS3Response registerDs3TargetSpectraS3Response = client
                .registerDs3TargetSpectraS3(new RegisterDs3TargetSpectraS3Request(
                        targetClientAccessKey, targetClientSecretKey, targetClientURL, targetClientName)
                        .withDataPathHttps(false));

        assertThat(registerDs3TargetSpectraS3Response.getResponse().getStatusCode(), is (200));
    }

    @Test
    public void VerifyDS3TargetByName_Test() throws IOException, SignatureException {
        final VerifyDs3TargetSpectraS3Response verifyDs3TargetSpectraS3Response = client
                .verifyDs3TargetSpectraS3(new VerifyDs3TargetSpectraS3Request(targetClientName));
        assertThat(verifyDs3TargetSpectraS3Response.getStatusCode(), is(200));
    }

    @Test
    public void VerifyDS3TargetByUUID_Test() throws IOException, SignatureException {
        final VerifyDs3TargetSpectraS3Response verifyDs3TargetSpectraS3ResponseUUID = client
                .verifyDs3TargetSpectraS3(new VerifyDs3TargetSpectraS3Request(targetUUIDString));
        assertThat(verifyDs3TargetSpectraS3ResponseUUID.getStatusCode(), is(200));
    }

    @Test
    public void PutDataReplicationRule_Test() throws IOException, SignatureException {
        final PutDataReplicationRuleSpectraS3Response putDataReplicationRuleSpectraS3Response = client
                .putDataReplicationRuleSpectraS3(new PutDataReplicationRuleSpectraS3Request(
                dataPolicyId.toString(), targetClientName, DataReplicationRuleType.PERMANENT));
        assertThat(putDataReplicationRuleSpectraS3Response.getStatusCode(), is(200));
    }
}
