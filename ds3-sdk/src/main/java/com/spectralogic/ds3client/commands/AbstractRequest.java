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

package com.spectralogic.ds3client.commands;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.spectralogic.ds3client.models.Checksum;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractRequest implements Ds3Request {

    private final Multimap<String, String> headers = buildDefaultHeaders();
    private final Map<String, String> queryParams = new HashMap<>();
    
    private static Multimap<String, String> buildDefaultHeaders() {
        final Multimap<String, String> headers = TreeMultimap.create();
        headers.put("Naming-Convention", "s3");
        return headers;
    }

    @Override
    public ContentType getContentType() {
        return ContentType.APPLICATION_XML;
    }

    @Override
    public InputStream getStream() {
        return null;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public Checksum getChecksum() {
        return Checksum.none();
    }

    @Override
    public final Map<String, String> getQueryParams() {
        return this.queryParams;
    }

    @Override
    public final Multimap<String, String> getHeaders() {
        return this.headers;
    }

    final void updateQueryParam(final String name, final String param) {
        if(param == null) {
            this.queryParams.remove(name);
        }
        else {
            this.queryParams.put(name, param);
        }
    }
}
