/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.Ds3TargetAccessControlReplication;
import java.lang.Integer;
import com.spectralogic.ds3client.models.TargetReadPreferenceType;

public class RegisterDs3TargetSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String adminAuthId;

    private final String adminSecretKey;

    private final String dataPathEndPoint;

    private final String name;

    private Ds3TargetAccessControlReplication accessControlReplication;

    private boolean dataPathHttps;

    private Integer dataPathPort;

    private String dataPathProxy;

    private boolean dataPathVerifyCertificate;

    private TargetReadPreferenceType defaultReadPreference;

    private boolean permitGoingOutOfSync;

    private String replicatedUserDefaultDataPolicy;

    // Constructor
    
    
    public RegisterDs3TargetSpectraS3Request(final String adminAuthId, final String adminSecretKey, final String dataPathEndPoint, final String name) {
        this.adminAuthId = adminAuthId;
        this.adminSecretKey = adminSecretKey;
        this.dataPathEndPoint = dataPathEndPoint;
        this.name = name;
        
        this.updateQueryParam("admin_auth_id", adminAuthId);

        this.updateQueryParam("admin_secret_key", adminSecretKey);

        this.updateQueryParam("data_path_end_point", dataPathEndPoint);

        this.updateQueryParam("name", name);

    }

    public RegisterDs3TargetSpectraS3Request withAccessControlReplication(final Ds3TargetAccessControlReplication accessControlReplication) {
        this.accessControlReplication = accessControlReplication;
        this.updateQueryParam("access_control_replication", accessControlReplication);
        return this;
    }


    public RegisterDs3TargetSpectraS3Request withDataPathHttps(final boolean dataPathHttps) {
        this.dataPathHttps = dataPathHttps;
        this.updateQueryParam("data_path_https", dataPathHttps);
        return this;
    }


    public RegisterDs3TargetSpectraS3Request withDataPathPort(final Integer dataPathPort) {
        this.dataPathPort = dataPathPort;
        this.updateQueryParam("data_path_port", dataPathPort);
        return this;
    }


    public RegisterDs3TargetSpectraS3Request withDataPathProxy(final String dataPathProxy) {
        this.dataPathProxy = dataPathProxy;
        this.updateQueryParam("data_path_proxy", dataPathProxy);
        return this;
    }


    public RegisterDs3TargetSpectraS3Request withDataPathVerifyCertificate(final boolean dataPathVerifyCertificate) {
        this.dataPathVerifyCertificate = dataPathVerifyCertificate;
        this.updateQueryParam("data_path_verify_certificate", dataPathVerifyCertificate);
        return this;
    }


    public RegisterDs3TargetSpectraS3Request withDefaultReadPreference(final TargetReadPreferenceType defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
        this.updateQueryParam("default_read_preference", defaultReadPreference);
        return this;
    }


    public RegisterDs3TargetSpectraS3Request withPermitGoingOutOfSync(final boolean permitGoingOutOfSync) {
        this.permitGoingOutOfSync = permitGoingOutOfSync;
        this.updateQueryParam("permit_going_out_of_sync", permitGoingOutOfSync);
        return this;
    }


    public RegisterDs3TargetSpectraS3Request withReplicatedUserDefaultDataPolicy(final String replicatedUserDefaultDataPolicy) {
        this.replicatedUserDefaultDataPolicy = replicatedUserDefaultDataPolicy;
        this.updateQueryParam("replicated_user_default_data_policy", replicatedUserDefaultDataPolicy);
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


    public String getDataPathEndPoint() {
        return this.dataPathEndPoint;
    }


    public String getName() {
        return this.name;
    }


    public Ds3TargetAccessControlReplication getAccessControlReplication() {
        return this.accessControlReplication;
    }


    public boolean getDataPathHttps() {
        return this.dataPathHttps;
    }


    public Integer getDataPathPort() {
        return this.dataPathPort;
    }


    public String getDataPathProxy() {
        return this.dataPathProxy;
    }


    public boolean getDataPathVerifyCertificate() {
        return this.dataPathVerifyCertificate;
    }


    public TargetReadPreferenceType getDefaultReadPreference() {
        return this.defaultReadPreference;
    }


    public boolean getPermitGoingOutOfSync() {
        return this.permitGoingOutOfSync;
    }


    public String getReplicatedUserDefaultDataPolicy() {
        return this.replicatedUserDefaultDataPolicy;
    }

}