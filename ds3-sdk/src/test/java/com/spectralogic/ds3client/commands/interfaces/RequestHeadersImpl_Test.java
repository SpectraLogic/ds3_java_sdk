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

package com.spectralogic.ds3client.commands.interfaces;

import com.google.common.collect.Multimap;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class RequestHeadersImpl_Test {

    private static RequestHeaders getTestRequestHeaders() {
        final RequestHeaders rh = new RequestHeadersImpl();
        rh.put("Key One", "Val+One");
        rh.put("Key One", "Val+Two");
        rh.put("Key Two", "Val+Three");
        return rh;
    }

    @Test (expected = NullPointerException.class)
    public void putNullTest() {
        final RequestHeaders rh = getTestRequestHeaders();
        rh.put(null, null);
    }

    @Test (expected = NullPointerException.class)
    public void putNullValueTest() {
        final RequestHeaders rh = getTestRequestHeaders();
        rh.put("Key", null);
    }

    @Test (expected = NullPointerException.class)
    public void putNullKeyTest() {
        final RequestHeaders rh = getTestRequestHeaders();
        rh.put(null, "Value");
    }

    @Test
    public void putPercentEncodingTest() {
        final RequestHeaders rh = getTestRequestHeaders();
        rh.put("Key With Space", "Val With Space");
        final Multimap<String, String> result = rh.getMultimap();
        assertTrue(result.containsEntry("Key%20With%20Space", "Val%20With%20Space"));
    }

    @Test (expected = NullPointerException.class)
    public void getNullTest() {
        final RequestHeaders rh = getTestRequestHeaders();
        rh.get(null);
    }

    @Test
    public void getTest() {
        final RequestHeaders rh = getTestRequestHeaders();
        assertThat(rh.get("").size(), is(0));
        assertThat(rh.get("Key One").size(), is(2));
        assertThat(rh.get("Key Two").size(), is(1));
        assertThat(rh.get("DoesNotExist").size(), is(0));
    }

    @Test
    public void sizeTest() {
        assertThat(getTestRequestHeaders().size(), is(3));
    }

    @Test (expected = NullPointerException.class)
    public void containsKeyNullStringTest() {
        assertFalse(getTestRequestHeaders().containsKey(null));
    }

    @Test
    public void containsKeyTest() {
        final RequestHeaders rh = getTestRequestHeaders();

        System.out.println(rh.keySet());

        assertFalse(rh.containsKey(""));
        assertFalse(rh.containsKey("DoesNotExist"));

        assertTrue(rh.containsKey("Key One"));
        assertTrue(rh.containsKey("Key Two"));
    }

    @Test (expected = NullPointerException.class)
    public void removeAllNullTest() {
        final RequestHeaders rh = getTestRequestHeaders();
        assertThat(rh.removeAll(null).size(), is(0));
    }

    @Test
    public void removeAllTest() {
        final RequestHeaders rh = getTestRequestHeaders();

        assertThat(rh.removeAll("").size(), is(0));
        assertThat(rh.size(), is(3));

        assertThat(rh.removeAll("DoesNotExist").size(), is(0));
        assertThat(rh.size(), is(3));

        assertThat(rh.removeAll("Key One").size(), is(2));
        assertThat(rh.size(), is(1));

        assertThat(rh.removeAll("Key Two").size(), is(1));
        assertThat(rh.size(), is(0));
    }

    @Test
    public void entriesTest() {
        final Collection<Map.Entry<String, String>> result = getTestRequestHeaders().entries();
        assertThat(result.size(), is(3));

        for (final Map.Entry<String, String> entry : result) {
            switch (entry.getKey()) {
                case "Key%20One":
                    assertThat(entry.getValue(), either(is("Val%2BOne")).or(is("Val%2BTwo")));
                    return;
                case "Key%20Two":
                    assertThat(entry.getValue(), is("Val%2BThree"));
                    return;
                default:
                    fail();
            }
        }
    }

    @Test
    public void getMultimapTest() {
        final Multimap result = getTestRequestHeaders().getMultimap();
        assertThat(result.size(), is(3));

        assertTrue(result.containsEntry("Key%20One", "Val%2BOne"));
        assertTrue(result.containsEntry("Key%20One", "Val%2BTwo"));
        assertTrue(result.containsEntry("Key%20Two", "Val%2BThree"));
    }

    @Test
    public void keySetTest() {
        final Set<String> result = getTestRequestHeaders().keySet();
        assertThat(result.size(), is(2));
        assertThat(result, hasItem("Key One"));
        assertThat(result, hasItem("Key Two"));
    }
}
