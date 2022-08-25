/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetServiceRequest;
import com.spectralogic.ds3client.commands.GetServiceResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.security.SignatureException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 *
 * This test is intended to be a sanity check to make sure that we can successfully ignore ssl certificate validation.
 *
 */
public class Insecure_Test {
    private static Ds3Client client;

    @BeforeClass
    public static void startup() throws IOException, SignatureException {
        client = Util.insecureFromEnv();
    }

    @AfterClass
    public static void teardown() throws IOException, SignatureException {
        client.close();
    }

    @Test
    public void getService() throws SignatureException, IOException{
        final GetServiceResponse response = client.getService(new GetServiceRequest());
        assertThat(response, is(notNullValue()));
    }
}
