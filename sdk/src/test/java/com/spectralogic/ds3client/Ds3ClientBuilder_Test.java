/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.Ds3ClientImpl;
import org.junit.Test;

import com.spectralogic.ds3client.models.Credentials;

public class Ds3ClientBuilder_Test {
    @Test
    public void createBasicClient() throws Exception {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.create("myEndPoint", new Credentials("foo","bar"));
        final Ds3ClientImpl client = (Ds3ClientImpl)builder.build();
        assertThat(client,is(notNullValue()));
    }

    @Test
    public void isNotSecure() throws Exception {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.create("myEndPoint", new Credentials("foo","bar"));
        final Ds3ClientImpl client = (Ds3ClientImpl)builder.withHttpSecure(false).build();
        assertThat(client.getNetClient().getConnectionDetails().isSecure(),is(false));
    }

    @Test
    public void defaultSecure() throws Exception {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.create("myEndPoint", new Credentials("foo","bar"));
        final Ds3ClientImpl client = (Ds3ClientImpl)builder.build();
        assertThat(client.getNetClient().getConnectionDetails().isSecure(),is(true));
    }

    @Test
    public void isSecure() throws Exception {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.create("myEndPoint", new Credentials("foo","bar"));
        final Ds3ClientImpl client = (Ds3ClientImpl)builder.withHttpSecure(true).build();
        assertThat(client.getNetClient().getConnectionDetails().isSecure(),is(true));
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

