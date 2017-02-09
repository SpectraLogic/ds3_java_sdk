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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.google.common.net.UrlEscapers;

public class ListMultiPartUploadsRequest extends AbstractRequest {

    // Variables
    
    private final String bucketName;

    private String delimiter;

    private String keyMarker;

    private int maxUploads;

    private String prefix;

    private String uploadIdMarker;

    // Constructor
    
    
    public ListMultiPartUploadsRequest(final String bucketName) {
        this.bucketName = bucketName;
        
        this.getQueryParams().put("uploads", null);
    }

    public ListMultiPartUploadsRequest withDelimiter(final String delimiter) {
        this.delimiter = delimiter;
        this.updateQueryParam("delimiter", delimiter);
        return this;
    }


    public ListMultiPartUploadsRequest withKeyMarker(final String keyMarker) {
        this.keyMarker = keyMarker;
        this.updateQueryParam("key_marker", keyMarker);
        return this;
    }


    public ListMultiPartUploadsRequest withMaxUploads(final int maxUploads) {
        this.maxUploads = maxUploads;
        this.updateQueryParam("max_uploads", maxUploads);
        return this;
    }


    public ListMultiPartUploadsRequest withPrefix(final String prefix) {
        this.prefix = prefix;
        this.updateQueryParam("prefix", prefix);
        return this;
    }


    public ListMultiPartUploadsRequest withUploadIdMarker(final String uploadIdMarker) {
        this.uploadIdMarker = uploadIdMarker;
        this.updateQueryParam("upload_id_marker", uploadIdMarker);
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


    public String getKeyMarker() {
        return this.keyMarker;
    }


    public int getMaxUploads() {
        return this.maxUploads;
    }


    public String getPrefix() {
        return this.prefix;
    }


    public String getUploadIdMarker() {
        return this.uploadIdMarker;
    }

}