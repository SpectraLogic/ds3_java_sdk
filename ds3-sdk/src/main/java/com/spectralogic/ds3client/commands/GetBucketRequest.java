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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.google.common.net.UrlEscapers;

public class GetBucketRequest extends AbstractRequest {

    // Variables
    
    private final String bucketName;

    private String delimiter;

    private String marker;

    private int maxKeys;

    private String prefix;

    // Constructor
    
    public GetBucketRequest(final String bucketName) {
        this.bucketName = bucketName;
            }

    public GetBucketRequest withDelimiter(final String delimiter) {
        this.delimiter = delimiter;
        this.updateQueryParam("delimiter", delimiter);
        return this;
    }

    public GetBucketRequest withMarker(final String marker) {
        this.marker = marker;
        this.updateQueryParam("marker", UrlEscapers.urlFragmentEscaper().escape(marker).replace('+', ' '));
        return this;
    }

    public GetBucketRequest withMaxKeys(final int maxKeys) {
        this.maxKeys = maxKeys;
        this.updateQueryParam("max_keys", Integer.toString(maxKeys));
        return this;
    }

    public GetBucketRequest withPrefix(final String prefix) {
        this.prefix = prefix;
        this.updateQueryParam("prefix", UrlEscapers.urlFragmentEscaper().escape(prefix).replace('+', ' '));
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/" + this.bucketName;
    }
    
    public String getBucketName() {
        return this.bucketName;
    }


    public String getDelimiter() {
        return this.delimiter;
    }


    public String getMarker() {
        return this.marker;
    }


    public int getMaxKeys() {
        return this.maxKeys;
    }


    public String getPrefix() {
        return this.prefix;
    }

}