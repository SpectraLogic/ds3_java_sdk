/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.models.common;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.is;
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
