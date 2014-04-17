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

package com.spectralogic.ds3client.models;

import com.spectralogic.ds3client.HttpVerb;

public class SignatureDetails {
    final private HttpVerb verb;
    final private String contentMd5;
    final private String contentType;
    final private String date;
    final private String canonicalizedAmzHeaders;
    final private String canonicalizedResource;
    final private Credentials userCredentials;

    public SignatureDetails(final HttpVerb verb, final String contentMd5,
                            final String contentType, final String date,
                            final String canonicalizedAmzHeaders,
                            final String canonicalizedResource,
                            final Credentials userCredentials) {
        this.verb = verb;
        this.contentMd5 = contentMd5;
        this.contentType = contentType;
        this.date = date;
        this.canonicalizedAmzHeaders = canonicalizedAmzHeaders;
        this.canonicalizedResource = canonicalizedResource;
        this.userCredentials = userCredentials;
    }

    public HttpVerb getVerb() {
        return verb;
    }

    public String getContentMd5() {
        return contentMd5;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDate() {
        return date;
    }

    public String getCanonicalizedAmzHeaders() {
        return canonicalizedAmzHeaders;
    }

    public String getCanonicalizedResource() {
        return canonicalizedResource;
    }

    public Credentials getCredentials() {
        return userCredentials;
    }
}
