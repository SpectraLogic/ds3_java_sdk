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


import com.spectralogic.ds3client.HttpVerb;

public class GetBucketRequest extends AbstractRequest {

    final private String bucket;
    private String nextMarker = null;
    private String prefix = null;
    private String delimiter = null;
    private int maxKeys = 0;

    /**
     * @param bucket The name of the bucket that will have it's objects listed.
     */
    public GetBucketRequest(final String bucket) {
        this.bucket = bucket;
    }

    /**
     * If a GetBucketRequest has been paginated this method is used to get the next set of objects.
     * @param nextMarker The marker specified in {@link GetBucketResponse#getResult()}
     * @return The current request object.
     */
    public GetBucketRequest withNextMarker(final String nextMarker) {
        this.nextMarker = nextMarker;
        this.getQueryParams().put("marker", nextMarker);
        return this;
    }

    /**
     * Use the prefix method for getting a list of 'directories' without getting the objects within that directory.  For
     * example to get the root level directories the prefix should be set to '/'
     * @param prefix The prefix to filter the objects for.
     * @return The current request object.
     */
    public GetBucketRequest withPrefix(final String prefix) {
        this.prefix = prefix;
        this.getQueryParams().put("prefix", prefix);
        return this;
    }

    /**
     * Limit how many objects will be returned in a request before pagination is enforced.
     * @param maxKeys The number of objects to request in a single request from DS3.
     * @return The current request object.
     */
    public GetBucketRequest withMaxKeys(final int maxKeys) {
        this.maxKeys = maxKeys;
        this.getQueryParams().put("max-keys", Integer.toString(maxKeys));
        return this;
    }

    public GetBucketRequest withDelimiter(final String delimiter) {
        this.delimiter = delimiter;
        this.getQueryParams().put("delimiter", delimiter);
        return this;
    }
    
    public String getBucket() {
        return this.bucket;
    }

    public String getNextMarker() {
        return this.nextMarker;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public int getMaxKeys() {
        return this.maxKeys;
    }

    @Override
    public String getPath() {
        return "/" + this.bucket;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
