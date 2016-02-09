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

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.Ds3TargetAccessControlReplication;
import com.spectralogic.ds3client.models.TargetReadPreference;
import java.util.UUID;

public class RegisterDs3TargetSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String adminAuthId;

    private final String adminSecretKey;

    private final String dataPath;

    private final String name;

    private Ds3TargetAccessControlReplication accessControlReplication;

    private TargetReadPreference defaultReadPreference;

    private UUID localAdminUserId;

    // Constructor
    
    public RegisterDs3TargetSpectraS3Request(final String adminAuthId, final String adminSecretKey, final String dataPath, final String name) {
        this.adminAuthId = adminAuthId;
        this.adminSecretKey = adminSecretKey;
        this.dataPath = dataPath;
        this.name = name;
                this.getQueryParams().put("admin_auth_id", UrlEscapers.urlFragmentEscaper().escape(adminAuthId));
        this.getQueryParams().put("admin_secret_key", UrlEscapers.urlFragmentEscaper().escape(adminSecretKey));
        this.getQueryParams().put("data_path", UrlEscapers.urlFragmentEscaper().escape(dataPath));
        this.getQueryParams().put("name", UrlEscapers.urlFragmentEscaper().escape(name));
    }

    public RegisterDs3TargetSpectraS3Request withAccessControlReplication(final Ds3TargetAccessControlReplication accessControlReplication) {
        this.accessControlReplication = accessControlReplication;
        this.updateQueryParam("access_control_replication", accessControlReplication.toString());
        return this;
    }

    public RegisterDs3TargetSpectraS3Request withDefaultReadPreference(final TargetReadPreference defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
        this.updateQueryParam("default_read_preference", defaultReadPreference.toString());
        return this;
    }

    public RegisterDs3TargetSpectraS3Request withLocalAdminUserId(final UUID localAdminUserId) {
        this.localAdminUserId = localAdminUserId;
        this.updateQueryParam("local_admin_user_id", localAdminUserId.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/ds3_target";
    }
    
    public String getAdminAuthId() {
        return this.adminAuthId;
    }


    public String getAdminSecretKey() {
        return this.adminSecretKey;
    }


    public String getDataPath() {
        return this.dataPath;
    }


    public String getName() {
        return this.name;
    }


    public Ds3TargetAccessControlReplication getAccessControlReplication() {
        return this.accessControlReplication;
    }


    public TargetReadPreference getDefaultReadPreference() {
        return this.defaultReadPreference;
    }


    public UUID getLocalAdminUserId() {
        return this.localAdminUserId;
    }

}