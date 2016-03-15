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
import com.spectralogic.ds3client.models.Ds3TargetAccessControlReplication;
import java.lang.Integer;
import com.spectralogic.ds3client.models.TargetReadPreference;
import java.util.UUID;

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

    private TargetReadPreference defaultReadPreference;

    private String localAdminUserId;

    private String localReplicatedUserDefaultDataPolicyId;

    private String replicatedUserDefaultDataPolicy;

    // Constructor
    
    public RegisterDs3TargetSpectraS3Request(final String adminAuthId, final String adminSecretKey, final String dataPathEndPoint, final String name) {
        this.adminAuthId = adminAuthId;
        this.adminSecretKey = adminSecretKey;
        this.dataPathEndPoint = dataPathEndPoint;
        this.name = name;
                this.getQueryParams().put("admin_auth_id", UrlEscapers.urlFragmentEscaper().escape(adminAuthId).replace("+", "%2B"));
        this.getQueryParams().put("admin_secret_key", UrlEscapers.urlFragmentEscaper().escape(adminSecretKey).replace("+", "%2B"));
        this.getQueryParams().put("data_path_end_point", UrlEscapers.urlFragmentEscaper().escape(dataPathEndPoint).replace("+", "%2B"));
        this.getQueryParams().put("name", UrlEscapers.urlFragmentEscaper().escape(name).replace("+", "%2B"));
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

    public RegisterDs3TargetSpectraS3Request withDefaultReadPreference(final TargetReadPreference defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
        this.updateQueryParam("default_read_preference", defaultReadPreference);
        return this;
    }

    public RegisterDs3TargetSpectraS3Request withLocalAdminUserId(final UUID localAdminUserId) {
        this.localAdminUserId = localAdminUserId.toString();
        this.updateQueryParam("local_admin_user_id", localAdminUserId);
        return this;
    }

    public RegisterDs3TargetSpectraS3Request withLocalAdminUserId(final String localAdminUserId) {
        this.localAdminUserId = localAdminUserId;
        this.updateQueryParam("local_admin_user_id", localAdminUserId);
        return this;
    }

    public RegisterDs3TargetSpectraS3Request withLocalReplicatedUserDefaultDataPolicyId(final UUID localReplicatedUserDefaultDataPolicyId) {
        this.localReplicatedUserDefaultDataPolicyId = localReplicatedUserDefaultDataPolicyId.toString();
        this.updateQueryParam("local_replicated_user_default_data_policy_id", localReplicatedUserDefaultDataPolicyId);
        return this;
    }

    public RegisterDs3TargetSpectraS3Request withLocalReplicatedUserDefaultDataPolicyId(final String localReplicatedUserDefaultDataPolicyId) {
        this.localReplicatedUserDefaultDataPolicyId = localReplicatedUserDefaultDataPolicyId;
        this.updateQueryParam("local_replicated_user_default_data_policy_id", localReplicatedUserDefaultDataPolicyId);
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


    public TargetReadPreference getDefaultReadPreference() {
        return this.defaultReadPreference;
    }


    public String getLocalAdminUserId() {
        return this.localAdminUserId;
    }


    public String getLocalReplicatedUserDefaultDataPolicyId() {
        return this.localReplicatedUserDefaultDataPolicyId;
    }


    public String getReplicatedUserDefaultDataPolicy() {
        return this.replicatedUserDefaultDataPolicy;
    }

}