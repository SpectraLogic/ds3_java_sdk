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
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.spectralogic.ds3client.models.Ds3TargetAccessControlReplication;
import com.google.common.net.UrlEscapers;
import java.lang.Integer;
import com.spectralogic.ds3client.models.TargetReadPreferenceType;
import com.spectralogic.ds3client.models.Quiesced;

public class ModifyDs3TargetSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String ds3Target;

    private Ds3TargetAccessControlReplication accessControlReplication;

    private String adminAuthId;

    private String adminSecretKey;

    private String dataPathEndPoint;

    private boolean dataPathHttps;

    private Integer dataPathPort;

    private String dataPathProxy;

    private boolean dataPathVerifyCertificate;

    private TargetReadPreferenceType defaultReadPreference;

    private String name;

    private boolean permitGoingOutOfSync;

    private Quiesced quiesced;

    private String replicatedUserDefaultDataPolicy;

    // Constructor
    
    
    public ModifyDs3TargetSpectraS3Request(final String ds3Target) {
        this.ds3Target = ds3Target;
        
    }

    public ModifyDs3TargetSpectraS3Request withAccessControlReplication(final Ds3TargetAccessControlReplication accessControlReplication) {
        this.accessControlReplication = accessControlReplication;
        this.updateQueryParam("access_control_replication", accessControlReplication);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withAdminAuthId(final String adminAuthId) {
        this.adminAuthId = adminAuthId;
        this.updateQueryParam("admin_auth_id", adminAuthId);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withAdminSecretKey(final String adminSecretKey) {
        this.adminSecretKey = adminSecretKey;
        this.updateQueryParam("admin_secret_key", adminSecretKey);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withDataPathEndPoint(final String dataPathEndPoint) {
        this.dataPathEndPoint = dataPathEndPoint;
        this.updateQueryParam("data_path_end_point", dataPathEndPoint);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withDataPathHttps(final boolean dataPathHttps) {
        this.dataPathHttps = dataPathHttps;
        this.updateQueryParam("data_path_https", dataPathHttps);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withDataPathPort(final Integer dataPathPort) {
        this.dataPathPort = dataPathPort;
        this.updateQueryParam("data_path_port", dataPathPort);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withDataPathProxy(final String dataPathProxy) {
        this.dataPathProxy = dataPathProxy;
        this.updateQueryParam("data_path_proxy", dataPathProxy);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withDataPathVerifyCertificate(final boolean dataPathVerifyCertificate) {
        this.dataPathVerifyCertificate = dataPathVerifyCertificate;
        this.updateQueryParam("data_path_verify_certificate", dataPathVerifyCertificate);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withDefaultReadPreference(final TargetReadPreferenceType defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
        this.updateQueryParam("default_read_preference", defaultReadPreference);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withPermitGoingOutOfSync(final boolean permitGoingOutOfSync) {
        this.permitGoingOutOfSync = permitGoingOutOfSync;
        this.updateQueryParam("permit_going_out_of_sync", permitGoingOutOfSync);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
        this.updateQueryParam("quiesced", quiesced);
        return this;
    }


    public ModifyDs3TargetSpectraS3Request withReplicatedUserDefaultDataPolicy(final String replicatedUserDefaultDataPolicy) {
        this.replicatedUserDefaultDataPolicy = replicatedUserDefaultDataPolicy;
        this.updateQueryParam("replicated_user_default_data_policy", replicatedUserDefaultDataPolicy);
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


    public String getDataPathEndPoint() {
        return this.dataPathEndPoint;
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


    public String getName() {
        return this.name;
    }


    public boolean getPermitGoingOutOfSync() {
        return this.permitGoingOutOfSync;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }


    public String getReplicatedUserDefaultDataPolicy() {
        return this.replicatedUserDefaultDataPolicy;
    }

}