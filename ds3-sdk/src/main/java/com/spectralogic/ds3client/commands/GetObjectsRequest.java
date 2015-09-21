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

package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

import java.util.UUID;

public class GetObjectsRequest extends AbstractRequest {

    private String bucketName;
    private String objectName;
    private int pageLength;
    private int pageOffset;
    private long version;
    private UUID objectId;
    private ObjectType objectType;

    public enum ObjectType { DATA, FOLDER }

    public GetObjectsRequest() {}

    public GetObjectsRequest withBucket(final String bucketName) {
        this.bucketName = bucketName;
        this.getQueryParams().put("bucket_id", this.bucketName);
        return this;
    }

    public GetObjectsRequest withObjectId(final UUID objectId) {
        this.objectId = objectId;
        this.getQueryParams().put("id", objectId.toString());
        return this;
    }

    public GetObjectsRequest withObjectName(final String objectName) {
        this.objectName = objectName;
        this.getQueryParams().put("name", objectName);
        return this;
    }

    public GetObjectsRequest withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.getQueryParams().put("page_length", Integer.toString(pageLength));
        return this;
    }

    public GetObjectsRequest withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.getQueryParams().put("page_offset", Integer.toString(pageOffset));
        return this;
    }

    public GetObjectsRequest withType(ObjectType objectType) {
        this.objectType = objectType;
        this.getQueryParams().put("type", objectType.toString().toLowerCase());
        return this;
    }

    public GetObjectsRequest withVersion(final long version) {
        this.version = version;
        this.getQueryParams().put("version", Long.toString(version));
        return this;
    }

    public String getBucket() { return this.bucketName; }

    public UUID getObjectId() { return this.objectId; }

    public String getObjectName() { return this.objectName; }

    public int getPageLength() { return this.pageLength; }

    public int getPageOffset() { return this.pageOffset; }

    public ObjectType getObjectType() { return this.objectType; }

    public long getVersion() { return this.version; }

    @Override
    public String getPath() { return "/_rest_/object/"; }

    @Override
    public HttpVerb getVerb() { return HttpVerb.GET; }
}
