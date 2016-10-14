/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.networking.Headers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockedHeaders implements Headers {
    private final Map<String, String> headerValues;

    public MockedHeaders(final Map<String, String> headerValues) {
        this.headerValues = normalizeHeaderValues(headerValues);
    }

    private static Map<String, String> normalizeHeaderValues(final Map<String, String> headerValues) {
        final Map<String, String> headers = new HashMap<>();
        for (final Map.Entry<String, String> entry : headerValues.entrySet()) {
            headers.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        return headers;
    }

    @Override
    public List<String> get(final String key) {
        return Lists.newArrayList(this.headerValues.get(key.toLowerCase()));
    }

    @Override
    public Set<String> keys() {
        return null;
    }
}
