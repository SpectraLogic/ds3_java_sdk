package com.spectralogic.ds3client;


import com.spectralogic.ds3client.models.Credentials;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class Credential_Test {

    @Test
    public void validCredentials() {
        final Credentials credentials = new Credentials("myId!", "key");
        assertThat(credentials.isValid(), is(true));
    }

    @Test
    public void missingValues() {
        final Credentials credentials = new Credentials(null, null);
        assertThat(credentials.isValid(), is(false));
    }

    @Test
    public void emptyValues() {
        final Credentials credentials = new Credentials("","");
        assertThat(credentials.isValid(), is(false));
    }

    @Test
    public void mixedMissingEmpty() {
        final Credentials credentials = new Credentials(null,"");
        assertThat(credentials.isValid(), is(false));
    }

    @Test
    public void mixedMissingAndValid() {
        final Credentials credentials = new Credentials(null,"asdf");
        assertThat(credentials.isValid(), is(false));
    }

    @Test
    public void mixedEmptyAndValid() {
        final Credentials credentials = new Credentials("","asdf");
        assertThat(credentials.isValid(), is(false));
    }

    @Test
    public void validGetters() {
        final Credentials credentials = new Credentials("foo","bar");
        assertThat(credentials.getClientId(), is("foo"));
        assertThat(credentials.getKey(), is("bar"));
    }

}
