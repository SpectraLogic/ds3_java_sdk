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
package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.UUID;
import java.util.Date;

@JacksonXmlRootElement(namespace = "Data")
public class S3Target {

    // Variables
    @JsonProperty("AccessKey")
    private String accessKey;

    @JsonProperty("AutoVerifyFrequencyInDays")
    private Integer autoVerifyFrequencyInDays;

    @JsonProperty("CloudBucketPrefix")
    private String cloudBucketPrefix;

    @JsonProperty("CloudBucketSuffix")
    private String cloudBucketSuffix;

    @JsonProperty("DataPathEndPoint")
    private String dataPathEndPoint;

    @JsonProperty("DefaultReadPreference")
    private TargetReadPreferenceType defaultReadPreference;

    @JsonProperty("Https")
    private boolean https;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LastFullyVerified")
    private Date lastFullyVerified;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("NamingMode")
    private CloudNamingMode namingMode;

    @JsonProperty("OfflineDataStagingWindowInTb")
    private int offlineDataStagingWindowInTb;

    @JsonProperty("PermitGoingOutOfSync")
    private boolean permitGoingOutOfSync;

    @JsonProperty("ProxyDomain")
    private String proxyDomain;

    @JsonProperty("ProxyHost")
    private String proxyHost;

    @JsonProperty("ProxyPassword")
    private String proxyPassword;

    @JsonProperty("ProxyPort")
    private Integer proxyPort;

    @JsonProperty("ProxyUsername")
    private String proxyUsername;

    @JsonProperty("Quiesced")
    private Quiesced quiesced;

    @JsonProperty("Region")
    private S3Region region;

    @JsonProperty("SecretKey")
    private String secretKey;

    @JsonProperty("StagedDataExpirationInDays")
    private int stagedDataExpirationInDays;

    @JsonProperty("State")
    private TargetState state;

    // Constructor
    public S3Target() {
        //pass
    }

    // Getters and Setters
    
    public String getAccessKey() {
        return this.accessKey;
    }

    public void setAccessKey(final String accessKey) {
        this.accessKey = accessKey;
    }


    public Integer getAutoVerifyFrequencyInDays() {
        return this.autoVerifyFrequencyInDays;
    }

    public void setAutoVerifyFrequencyInDays(final Integer autoVerifyFrequencyInDays) {
        this.autoVerifyFrequencyInDays = autoVerifyFrequencyInDays;
    }


    public String getCloudBucketPrefix() {
        return this.cloudBucketPrefix;
    }

    public void setCloudBucketPrefix(final String cloudBucketPrefix) {
        this.cloudBucketPrefix = cloudBucketPrefix;
    }


    public String getCloudBucketSuffix() {
        return this.cloudBucketSuffix;
    }

    public void setCloudBucketSuffix(final String cloudBucketSuffix) {
        this.cloudBucketSuffix = cloudBucketSuffix;
    }


    public String getDataPathEndPoint() {
        return this.dataPathEndPoint;
    }

    public void setDataPathEndPoint(final String dataPathEndPoint) {
        this.dataPathEndPoint = dataPathEndPoint;
    }


    public TargetReadPreferenceType getDefaultReadPreference() {
        return this.defaultReadPreference;
    }

    public void setDefaultReadPreference(final TargetReadPreferenceType defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
    }


    public boolean getHttps() {
        return this.https;
    }

    public void setHttps(final boolean https) {
        this.https = https;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public Date getLastFullyVerified() {
        return this.lastFullyVerified;
    }

    public void setLastFullyVerified(final Date lastFullyVerified) {
        this.lastFullyVerified = lastFullyVerified;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public CloudNamingMode getNamingMode() {
        return this.namingMode;
    }

    public void setNamingMode(final CloudNamingMode namingMode) {
        this.namingMode = namingMode;
    }


    public int getOfflineDataStagingWindowInTb() {
        return this.offlineDataStagingWindowInTb;
    }

    public void setOfflineDataStagingWindowInTb(final int offlineDataStagingWindowInTb) {
        this.offlineDataStagingWindowInTb = offlineDataStagingWindowInTb;
    }


    public boolean getPermitGoingOutOfSync() {
        return this.permitGoingOutOfSync;
    }

    public void setPermitGoingOutOfSync(final boolean permitGoingOutOfSync) {
        this.permitGoingOutOfSync = permitGoingOutOfSync;
    }


    public String getProxyDomain() {
        return this.proxyDomain;
    }

    public void setProxyDomain(final String proxyDomain) {
        this.proxyDomain = proxyDomain;
    }


    public String getProxyHost() {
        return this.proxyHost;
    }

    public void setProxyHost(final String proxyHost) {
        this.proxyHost = proxyHost;
    }


    public String getProxyPassword() {
        return this.proxyPassword;
    }

    public void setProxyPassword(final String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }


    public Integer getProxyPort() {
        return this.proxyPort;
    }

    public void setProxyPort(final Integer proxyPort) {
        this.proxyPort = proxyPort;
    }


    public String getProxyUsername() {
        return this.proxyUsername;
    }

    public void setProxyUsername(final String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }

    public void setQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
    }


    public S3Region getRegion() {
        return this.region;
    }

    public void setRegion(final S3Region region) {
        this.region = region;
    }


    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }


    public int getStagedDataExpirationInDays() {
        return this.stagedDataExpirationInDays;
    }

    public void setStagedDataExpirationInDays(final int stagedDataExpirationInDays) {
        this.stagedDataExpirationInDays = stagedDataExpirationInDays;
    }


    public TargetState getState() {
        return this.state;
    }

    public void setState(final TargetState state) {
        this.state = state;
    }

}