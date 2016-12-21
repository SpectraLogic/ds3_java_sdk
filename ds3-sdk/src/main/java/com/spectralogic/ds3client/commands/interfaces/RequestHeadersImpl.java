/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.spectralogic.ds3client.networking.NetworkClientImpl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.spectralogic.ds3client.utils.MetadataStringManipulation.toDecodedString;
import static com.spectralogic.ds3client.utils.MetadataStringManipulation.toEncodedString;

/**
 * Implementation of request headers which percent encodes key and values.
 */
public class RequestHeadersImpl implements RequestHeaders {

    private final Multimap<String, String> headers = TreeMultimap.create();

    /**
     * Puts the percent encoded key/value pair into headers
     */
    @Override
    public void put(final String key, final String value) {
        headers.put(toEncodedString(key), toEncodedString(value));
    }

    /**
     * Retrieves the non-percent encoded values for the key
     * @param key non-percent encoded header key
     */
    @Override
    public Collection<String> get(final String key) {
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (final String value : headers.get(toEncodedString(key))) {
            builder.add(toDecodedString(value));
        }
        return builder.build();
    }

    /**
     * Retrieves the size of the headers multimap
     */
    @Override
    public int size() {
        return headers.size();
    }

    /**
     * Checks if the headers contains a percent-encoded version of the key
     * @param key non-percent encoded header key
     */
    @Override
    public boolean containsKey(final String key) {
        return headers.containsKey(toEncodedString(key));
    }

    /**
     * Removes all instances of the percent encoded key from headers
     * @param key non-percent encoded header key
     */
    @Override
    public Collection<String> removeAll(final String key) {
        return headers.removeAll(toEncodedString(key));
    }

    /**
     * Retrieves the percent-encoded header entries. Used in {@link NetworkClientImpl}
     */
    @Override
    public Collection<Map.Entry<String, String>> entries() {
        return headers.entries();
    }

    /**
     * Retrieves the percent-encoded multimap. Used in {@link NetworkClientImpl}
     */
    @Override
    public Multimap<String, String> getMultimap() {
        return headers;
    }

    /**
     * Retrieves the non-percent encoded key set. Used in unit testing.
     */
    @Override
    public Set<String> keySet() {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        for (final String key: headers.keySet()) {
            builder.add(toDecodedString(key));
        }
        return builder.build();
    }
}
