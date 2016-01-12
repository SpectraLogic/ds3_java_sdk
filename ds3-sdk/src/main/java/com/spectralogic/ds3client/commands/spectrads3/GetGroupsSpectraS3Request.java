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

import com.spectralogic.ds3client.commands.AbstractRequest;
import com.spectralogic.ds3client.HttpVerb;
import java.lang.String;
import java.util.UUID;

public class GetGroupsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String group;

    private boolean builtIn;
    private boolean lastPage;
    private String name;
    private int pageLength;
    private int pageOffset;
    private UUID pageStartMarker;

    // Constructor
    public GetGroupsSpectraS3Request(final String group) {
        this.group = group;
        
    }
    public GetGroupsSpectraS3Request withBuiltIn(final boolean builtIn) {
        this.builtIn = builtIn;
        this.updateQueryParam("built_in", null);
        return this;
    }

    public GetGroupsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }

    public GetGroupsSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }

    public GetGroupsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", Integer.toString(pageLength));
        return this;
    }

    public GetGroupsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", Integer.toString(pageOffset));
        return this;
    }

    public GetGroupsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/group/" + group;
    }
    
    public String getGroup() {
        return this.group;
    }


    public boolean getBuiltIn() {
        return this.builtIn;
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

}