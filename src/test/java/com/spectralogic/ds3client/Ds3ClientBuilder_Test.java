package com.spectralogic.ds3client;


import com.spectralogic.ds3client.models.Credentials;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class Ds3ClientBuilder_Test {

    @Test
    public void createBasicClient() throws Exception {
        final Ds3Client.Builder builder = Ds3Client.builder("myEndPoint", new Credentials("foo","bar"));
        final Ds3Client client = builder.build();
        assertThat(client,is(notNullValue()));
    }

    @Test
    public void isNotSecure() throws Exception {
    	final Ds3Client.Builder builder = Ds3Client.builder("myEndPoint", new Credentials("foo","bar"));
        final Ds3Client client = builder.withHttpSecure(false).build();
        assertThat(client.getNetClient().getConnectionDetails().isSecure(),is(false));
    }

    @Test
    public void defaultSecure() throws Exception {
    	final Ds3Client.Builder builder = Ds3Client.builder("myEndPoint", new Credentials("foo","bar"));
        final Ds3Client client = builder.build();
        assertThat(client.getNetClient().getConnectionDetails().isSecure(),is(true));
    }

    @Test
    public void isSecure() throws Exception {
    	final Ds3Client.Builder builder = Ds3Client.builder("myEndPoint", new Credentials("foo","bar"));
        final Ds3Client client = builder.withHttpSecure(true).build();
        assertThat(client.getNetClient().getConnectionDetails().isSecure(),is(true));
    }

    @Test
    public void getClientEndpoint() throws Exception {
    	final Ds3Client.Builder builder = Ds3Client.builder("myEndPoint", new Credentials("foo","bar"));
        final Ds3Client client = builder.build();
        assertThat(client.getNetClient().getConnectionDetails().getEndpoint(),is("myEndPoint"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void badEndpoint() throws Exception {
        Ds3Client.builder("", new Credentials("foo","bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void badCredentials() throws Exception {
        Ds3Client.builder("myEndPoint", new Credentials("","bar"));
    }
}

