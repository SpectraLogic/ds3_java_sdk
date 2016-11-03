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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationRequest;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.TargetReadPreferenceType;
import java.util.UUID;
import com.spectralogic.ds3client.models.Quiesced;
import com.spectralogic.ds3client.models.S3Region;
import com.spectralogic.ds3client.models.TargetState;

public class GetS3TargetsSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private String accessKey;

    private String dataPathEndPoint;

    private TargetReadPreferenceType defaultReadPreference;

    private boolean https;

    private boolean lastPage;

    private String name;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private boolean permitGoingOutOfSync;

    private Quiesced quiesced;

    private S3Region region;

    private boolean replicateDeletes;

    private TargetState state;

    // Constructor
    
    
    public GetS3TargetsSpectraS3Request() {
        
    }

    public GetS3TargetsSpectraS3Request withAccessKey(final String accessKey) {
        this.accessKey = accessKey;
        this.updateQueryParam("access_key", accessKey);
        return this;
    }


    public GetS3TargetsSpectraS3Request withDataPathEndPoint(final String dataPathEndPoint) {
        this.dataPathEndPoint = dataPathEndPoint;
        this.updateQueryParam("data_path_end_point", dataPathEndPoint);
        return this;
    }


    public GetS3TargetsSpectraS3Request withDefaultReadPreference(final TargetReadPreferenceType defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
        this.updateQueryParam("default_read_preference", defaultReadPreference);
        return this;
    }


    public GetS3TargetsSpectraS3Request withHttps(final boolean https) {
        this.https = https;
        this.updateQueryParam("https", https);
        return this;
    }


    public GetS3TargetsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetS3TargetsSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    public GetS3TargetsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetS3TargetsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetS3TargetsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetS3TargetsSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetS3TargetsSpectraS3Request withPermitGoingOutOfSync(final boolean permitGoingOutOfSync) {
        this.permitGoingOutOfSync = permitGoingOutOfSync;
        this.updateQueryParam("permit_going_out_of_sync", permitGoingOutOfSync);
        return this;
    }


    public GetS3TargetsSpectraS3Request withQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
        this.updateQueryParam("quiesced", quiesced);
        return this;
    }


    public GetS3TargetsSpectraS3Request withRegion(final S3Region region) {
        this.region = region;
        this.updateQueryParam("region", region);
        return this;
    }


    public GetS3TargetsSpectraS3Request withReplicateDeletes(final boolean replicateDeletes) {
        this.replicateDeletes = replicateDeletes;
        this.updateQueryParam("replicate_deletes", replicateDeletes);
        return this;
    }


    public GetS3TargetsSpectraS3Request withState(final TargetState state) {
        this.state = state;
        this.updateQueryParam("state", state);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/s3_target";
    }
    
    public String getAccessKey() {
        return this.accessKey;
    }


    public String getDataPathEndPoint() {
        return this.dataPathEndPoint;
    }


    public TargetReadPreferenceType getDefaultReadPreference() {
        return this.defaultReadPreference;
    }


    public boolean getHttps() {
        return this.https;
    }


    public boolean getLastPage() {
        return this.lastPage;
    }


    public String getName() {
        return this.name;
    }


    public int getPageLength() {
        return this.pageLength;
    }


    public int getPageOffset() {
        return this.pageOffset;
    }


    public String getPageStartMarker() {
        return this.pageStartMarker;
    }


    public boolean getPermitGoingOutOfSync() {
        return this.permitGoingOutOfSync;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }


    public S3Region getRegion() {
        return this.region;
    }


    public boolean getReplicateDeletes() {
        return this.replicateDeletes;
    }


    public TargetState getState() {
        return this.state;
    }

}