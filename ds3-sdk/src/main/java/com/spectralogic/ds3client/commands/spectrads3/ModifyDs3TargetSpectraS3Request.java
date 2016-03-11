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
import com.spectralogic.ds3client.models.Ds3TargetAccessControlReplication;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.TargetReadPreference;
import com.spectralogic.ds3client.models.Quiesced;

public class ModifyDs3TargetSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String ds3Target;

    private Ds3TargetAccessControlReplication accessControlReplication;

    private String adminAuthId;

    private String adminSecretKey;

    private String dataPath;

    private TargetReadPreference defaultReadPreference;

    private String name;

    private Quiesced quiesced;

    private String replicatedUserDefaultDataPolicy;

    // Constructor
    
    public ModifyDs3TargetSpectraS3Request(final String ds3Target) {
        this.ds3Target = ds3Target;
            }

    public ModifyDs3TargetSpectraS3Request withAccessControlReplication(final Ds3TargetAccessControlReplication accessControlReplication) {
        this.accessControlReplication = accessControlReplication;
        this.updateQueryParam("access_control_replication", accessControlReplication.toString());
        return this;
    }

    public ModifyDs3TargetSpectraS3Request withAdminAuthId(final String adminAuthId) {
        this.adminAuthId = adminAuthId;
        this.updateQueryParam("admin_auth_id", UrlEscapers.urlFragmentEscaper().escape(adminAuthId).replace('+', ' '));
        return this;
    }

    public ModifyDs3TargetSpectraS3Request withAdminSecretKey(final String adminSecretKey) {
        this.adminSecretKey = adminSecretKey;
        this.updateQueryParam("admin_secret_key", UrlEscapers.urlFragmentEscaper().escape(adminSecretKey).replace('+', ' '));
        return this;
    }

    public ModifyDs3TargetSpectraS3Request withDataPath(final String dataPath) {
        this.dataPath = dataPath;
        this.updateQueryParam("data_path", UrlEscapers.urlFragmentEscaper().escape(dataPath).replace('+', ' '));
        return this;
    }

    public ModifyDs3TargetSpectraS3Request withDefaultReadPreference(final TargetReadPreference defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
        this.updateQueryParam("default_read_preference", defaultReadPreference.toString());
        return this;
    }

    public ModifyDs3TargetSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", UrlEscapers.urlFragmentEscaper().escape(name).replace('+', ' '));
        return this;
    }

    public ModifyDs3TargetSpectraS3Request withQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
        this.updateQueryParam("quiesced", quiesced.toString());
        return this;
    }

    public ModifyDs3TargetSpectraS3Request withReplicatedUserDefaultDataPolicy(final String replicatedUserDefaultDataPolicy) {
        this.replicatedUserDefaultDataPolicy = replicatedUserDefaultDataPolicy;
        this.updateQueryParam("replicated_user_default_data_policy", UrlEscapers.urlFragmentEscaper().escape(replicatedUserDefaultDataPolicy).replace('+', ' '));
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/ds3_target/" + ds3Target;
    }
    
    public String getDs3Target() {
        return this.ds3Target;
    }


    public Ds3TargetAccessControlReplication getAccessControlReplication() {
        return this.accessControlReplication;
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


    public TargetReadPreference getDefaultReadPreference() {
        return this.defaultReadPreference;
    }


    public String getName() {
        return this.name;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }


    public String getReplicatedUserDefaultDataPolicy() {
        return this.replicatedUserDefaultDataPolicy;
    }

}