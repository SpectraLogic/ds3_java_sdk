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

package com.spectralogic.ds3client;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.spectralogic.ds3client.networking.Headers;
import org.apache.http.Header;

import java.util.List;
import java.util.Set;

class HeadersImpl implements Headers {

    private final ImmutableMultimap<String, String> headers;


    HeadersImpl(final Header[] allHeaders){
        this.headers = toMultiMap(allHeaders);
    }

    private static ImmutableMultimap<String, String> toMultiMap(final Header[] headers){
        final ImmutableMultimap.Builder<String, String> builder = ImmutableMultimap.builder();

        for (final Header header : headers) {
            builder.put(header.getName().toLowerCase(), header.getValue());
        }

        return builder.build();
    }

    @Override
    public List<String> get(final String key) {
        return Lists.newArrayList(headers.get(key.toLowerCase()));
    }

    @Override
    public Set<String> keys() {
        return Sets.newHashSet(headers.keySet());
    }
}
