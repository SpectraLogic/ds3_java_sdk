/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
import java.lang.Integer;
import com.spectralogic.ds3client.models.TargetReadPreferenceType;
import com.spectralogic.ds3client.models.CloudNamingMode;
import com.spectralogic.ds3client.models.S3Region;

public class RegisterS3TargetSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String accessKey;

    private final String name;

    private final String secretKey;

    private Integer autoVerifyFrequencyInDays;

    private String cloudBucketPrefix;

    private String cloudBucketSuffix;

    private String dataPathEndPoint;

    private TargetReadPreferenceType defaultReadPreference;

    private boolean https;

    private CloudNamingMode namingMode;

    private int offlineDataStagingWindowInTb;

    private boolean permitGoingOutOfSync;

    private String proxyDomain;

    private String proxyHost;

    private String proxyPassword;

    private Integer proxyPort;

    private String proxyUsername;

    private S3Region region;

    private int stagedDataExpirationInDays;

    // Constructor
    
    
    public RegisterS3TargetSpectraS3Request(final String accessKey, final String name, final String secretKey) {
        this.accessKey = accessKey;
        this.name = name;
        this.secretKey = secretKey;
        
        this.updateQueryParam("access_key", accessKey);

        this.updateQueryParam("name", name);

        this.updateQueryParam("secret_key", secretKey);

    }

    public RegisterS3TargetSpectraS3Request withAutoVerifyFrequencyInDays(final Integer autoVerifyFrequencyInDays) {
        this.autoVerifyFrequencyInDays = autoVerifyFrequencyInDays;
        this.updateQueryParam("auto_verify_frequency_in_days", autoVerifyFrequencyInDays);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withCloudBucketPrefix(final String cloudBucketPrefix) {
        this.cloudBucketPrefix = cloudBucketPrefix;
        this.updateQueryParam("cloud_bucket_prefix", cloudBucketPrefix);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withCloudBucketSuffix(final String cloudBucketSuffix) {
        this.cloudBucketSuffix = cloudBucketSuffix;
        this.updateQueryParam("cloud_bucket_suffix", cloudBucketSuffix);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withDataPathEndPoint(final String dataPathEndPoint) {
        this.dataPathEndPoint = dataPathEndPoint;
        this.updateQueryParam("data_path_end_point", dataPathEndPoint);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withDefaultReadPreference(final TargetReadPreferenceType defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
        this.updateQueryParam("default_read_preference", defaultReadPreference);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withHttps(final boolean https) {
        this.https = https;
        this.updateQueryParam("https", https);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withNamingMode(final CloudNamingMode namingMode) {
        this.namingMode = namingMode;
        this.updateQueryParam("naming_mode", namingMode);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withOfflineDataStagingWindowInTb(final int offlineDataStagingWindowInTb) {
        this.offlineDataStagingWindowInTb = offlineDataStagingWindowInTb;
        this.updateQueryParam("offline_data_staging_window_in_tb", offlineDataStagingWindowInTb);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withPermitGoingOutOfSync(final boolean permitGoingOutOfSync) {
        this.permitGoingOutOfSync = permitGoingOutOfSync;
        this.updateQueryParam("permit_going_out_of_sync", permitGoingOutOfSync);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withProxyDomain(final String proxyDomain) {
        this.proxyDomain = proxyDomain;
        this.updateQueryParam("proxy_domain", proxyDomain);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withProxyHost(final String proxyHost) {
        this.proxyHost = proxyHost;
        this.updateQueryParam("proxy_host", proxyHost);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withProxyPassword(final String proxyPassword) {
        this.proxyPassword = proxyPassword;
        this.updateQueryParam("proxy_password", proxyPassword);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withProxyPort(final Integer proxyPort) {
        this.proxyPort = proxyPort;
        this.updateQueryParam("proxy_port", proxyPort);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withProxyUsername(final String proxyUsername) {
        this.proxyUsername = proxyUsername;
        this.updateQueryParam("proxy_username", proxyUsername);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withRegion(final S3Region region) {
        this.region = region;
        this.updateQueryParam("region", region);
        return this;
    }


    public RegisterS3TargetSpectraS3Request withStagedDataExpirationInDays(final int stagedDataExpirationInDays) {
        this.stagedDataExpirationInDays = stagedDataExpirationInDays;
        this.updateQueryParam("staged_data_expiration_in_days", stagedDataExpirationInDays);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/s3_target";
    }
    
    public String getAccessKey() {
        return this.accessKey;
    }


    public String getName() {
        return this.name;
    }


    public String getSecretKey() {
        return this.secretKey;
    }


    public Integer getAutoVerifyFrequencyInDays() {
        return this.autoVerifyFrequencyInDays;
    }


    public String getCloudBucketPrefix() {
        return this.cloudBucketPrefix;
    }


    public String getCloudBucketSuffix() {
        return this.cloudBucketSuffix;
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


    public CloudNamingMode getNamingMode() {
        return this.namingMode;
    }


    public int getOfflineDataStagingWindowInTb() {
        return this.offlineDataStagingWindowInTb;
    }


    public boolean getPermitGoingOutOfSync() {
        return this.permitGoingOutOfSync;
    }


    public String getProxyDomain() {
        return this.proxyDomain;
    }


    public String getProxyHost() {
        return this.proxyHost;
    }


    public String getProxyPassword() {
        return this.proxyPassword;
    }


    public Integer getProxyPort() {
        return this.proxyPort;
    }


    public String getProxyUsername() {
        return this.proxyUsername;
    }


    public S3Region getRegion() {
        return this.region;
    }


    public int getStagedDataExpirationInDays() {
        return this.stagedDataExpirationInDays;
    }

}