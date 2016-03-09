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
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;
import com.google.common.net.UrlEscapers;
import java.lang.Integer;
import com.spectralogic.ds3client.models.TargetReadPreference;
import java.util.UUID;
import com.spectralogic.ds3client.models.Quiesced;
import com.spectralogic.ds3client.models.TargetState;

public class GetDs3TargetsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private String adminAuthId;

    private String dataPathEndPoint;

    private boolean dataPathHttps;

    private int dataPathPort;

    private String dataPathProxy;

    private boolean dataPathVerifyCertificate;

    private TargetReadPreference defaultReadPreference;

    private boolean lastPage;

    private String name;

    private int pageLength;

    private int pageOffset;

    private UUID pageStartMarker;

    private Quiesced quiesced;

    private TargetState state;

    // Constructor
    
    public GetDs3TargetsSpectraS3Request() {
            }

    public GetDs3TargetsSpectraS3Request withAdminAuthId(final String adminAuthId) {
        this.adminAuthId = adminAuthId;
        this.updateQueryParam("admin_auth_id", UrlEscapers.urlFragmentEscaper().escape(adminAuthId));
        return this;
    }

    public GetDs3TargetsSpectraS3Request withDataPathEndPoint(final String dataPathEndPoint) {
        this.dataPathEndPoint = dataPathEndPoint;
        this.updateQueryParam("data_path_end_point", UrlEscapers.urlFragmentEscaper().escape(dataPathEndPoint));
        return this;
    }

    public GetDs3TargetsSpectraS3Request withDataPathHttps(final boolean dataPathHttps) {
        this.dataPathHttps = dataPathHttps;
        this.updateQueryParam("data_path_https", String.valueOf(dataPathHttps));
        return this;
    }

    public GetDs3TargetsSpectraS3Request withDataPathPort(final int dataPathPort) {
        this.dataPathPort = dataPathPort;
        this.updateQueryParam("data_path_port", Integer.toString(dataPathPort));
        return this;
    }

    public GetDs3TargetsSpectraS3Request withDataPathProxy(final String dataPathProxy) {
        this.dataPathProxy = dataPathProxy;
        this.updateQueryParam("data_path_proxy", UrlEscapers.urlFragmentEscaper().escape(dataPathProxy));
        return this;
    }

    public GetDs3TargetsSpectraS3Request withDataPathVerifyCertificate(final boolean dataPathVerifyCertificate) {
        this.dataPathVerifyCertificate = dataPathVerifyCertificate;
        this.updateQueryParam("data_path_verify_certificate", String.valueOf(dataPathVerifyCertificate));
        return this;
    }

    public GetDs3TargetsSpectraS3Request withDefaultReadPreference(final TargetReadPreference defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
        this.updateQueryParam("default_read_preference", defaultReadPreference.toString());
        return this;
    }

    public GetDs3TargetsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }

    public GetDs3TargetsSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", UrlEscapers.urlFragmentEscaper().escape(name));
        return this;
    }

    public GetDs3TargetsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", Integer.toString(pageLength));
        return this;
    }

    public GetDs3TargetsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", Integer.toString(pageOffset));
        return this;
    }

    public GetDs3TargetsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker.toString());
        return this;
    }

    public GetDs3TargetsSpectraS3Request withQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
        this.updateQueryParam("quiesced", quiesced.toString());
        return this;
    }

    public GetDs3TargetsSpectraS3Request withState(final TargetState state) {
        this.state = state;
        this.updateQueryParam("state", state.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/ds3_target";
    }
    
    public String getAdminAuthId() {
        return this.adminAuthId;
    }


    public String getDataPathEndPoint() {
        return this.dataPathEndPoint;
    }


    public boolean getDataPathHttps() {
        return this.dataPathHttps;
    }


    public int getDataPathPort() {
        return this.dataPathPort;
    }


    public String getDataPathProxy() {
        return this.dataPathProxy;
    }


    public boolean getDataPathVerifyCertificate() {
        return this.dataPathVerifyCertificate;
    }


    public TargetReadPreference getDefaultReadPreference() {
        return this.defaultReadPreference;
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


    public UUID getPageStartMarker() {
        return this.pageStartMarker;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }


    public TargetState getState() {
        return this.state;
    }

}