package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetServiceRequest;
import com.spectralogic.ds3client.commands.GetServiceResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.security.SignatureException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 *
 * This test is intended to be a sanity check to make sure that we can successfully ignore ssl certificate validation.
 *
 */
public class Insecure_Test {

    private static Ds3Client client;

    @BeforeClass
    public static void startup() {
        client = Util.insecureFromEnv();
    }

    @Test
    public void getService() throws SignatureException, IOException{
        final GetServiceResponse response = client.getService(new GetServiceRequest());

        assertThat(response, is(notNullValue()));
    }
}
