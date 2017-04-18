/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client;

import com.spectralogic.ds3client.models.common.Credentials;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Ds3ClientBuilder_Test {
    @Test
    public void createBasicClient() throws Exception {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.create("myEndPoint", new Credentials("foo","bar"));
        final Ds3ClientImpl client = (Ds3ClientImpl)builder.build();
        assertThat(client,is(notNullValue()));
    }

    @Test
    public void createBasicClientFromENV() throws Exception {
        try {
            final Ds3ClientBuilder builder = Ds3ClientBuilder.fromEnv();
            final Ds3ClientImpl client = (Ds3ClientImpl)builder.build();
            assertThat(client,is(notNullValue()));
        }catch (IllegalArgumentException e){
            Boolean wrongArgument = false;
            if (System.getenv("ENDPOINT") == null || System.getenv("ENDPOINT").isEmpty()) {
                wrongArgument = true;
            }
            else if (System.getenv("ACCESS_KEY") == null || System.getenv("ACCESS_KEY").isEmpty()) {
                wrongArgument = true;
            }
            else if (System.getenv("SECRET_KEY") == null || System.getenv("SECRET_KEY").isEmpty()) {
                wrongArgument = true;
            }
            assertTrue(wrongArgument);
        }
    }

    @Test
    public void isNotSecure() throws Exception {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.create("myEndPoint", new Credentials("foo","bar"));
        final Ds3ClientImpl client = (Ds3ClientImpl)builder.withHttps(false).build();
        assertThat(client.getNetClient().getConnectionDetails().isHttps(),is(false));
    }

    @Test
    public void defaultSecure() throws Exception {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.create("myEndPoint", new Credentials("foo","bar"));
        final Ds3ClientImpl client = (Ds3ClientImpl)builder.build();
        assertThat(client.getNetClient().getConnectionDetails().isHttps(),is(true));
    }

    @Test
    public void isSecure() throws Exception {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.create("myEndPoint", new Credentials("foo","bar"));
        final Ds3ClientImpl client = (Ds3ClientImpl)builder.withHttps(true).build();
        assertThat(client.getNetClient().getConnectionDetails().isHttps(),is(true));
    }

    @Test
    public void getClientEndpoint() throws Exception {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.create("myEndPoint", new Credentials("foo","bar"));
        final Ds3ClientImpl client = (Ds3ClientImpl)builder.build();
        assertThat(client.getNetClient().getConnectionDetails().getEndpoint(),is("myEndPoint"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void badEndpoint() throws Exception {
        Ds3ClientBuilder.create("", new Credentials("foo","bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void badCredentials() throws Exception {
        Ds3ClientBuilder.create("myEndPoint", new Credentials("","bar"));
    }
}

