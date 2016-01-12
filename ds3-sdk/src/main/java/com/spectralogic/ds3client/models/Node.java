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
package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Integer;
import java.lang.String;
import java.util.UUID;
import java.util.Date;

public class Node {

    // Variables
    @JsonProperty("DataPathHttpPort")
    private Integer dataPathHttpPort;

    @JsonProperty("DataPathHttpsPort")
    private Integer dataPathHttpsPort;

    @JsonProperty("DataPathIpAddress")
    private String dataPathIpAddress;

    @JsonProperty("DnsName")
    private String dnsName;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LastHeartbeat")
    private Date lastHeartbeat;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("SerialNumber")
    private String serialNumber;

    // Constructor
    public Node(final Integer dataPathHttpPort, final Integer dataPathHttpsPort, final String dataPathIpAddress, final String dnsName, final UUID id, final Date lastHeartbeat, final String name, final String serialNumber) {
        this.dataPathHttpPort = dataPathHttpPort;
        this.dataPathHttpsPort = dataPathHttpsPort;
        this.dataPathIpAddress = dataPathIpAddress;
        this.dnsName = dnsName;
        this.id = id;
        this.lastHeartbeat = lastHeartbeat;
        this.name = name;
        this.serialNumber = serialNumber;
    }

    // Getters and Setters
    
    public Integer getDataPathHttpPort() {
        return this.dataPathHttpPort;
    }

    public void setDataPathHttpPort(final Integer dataPathHttpPort) {
        this.dataPathHttpPort = dataPathHttpPort;
    }


    public Integer getDataPathHttpsPort() {
        return this.dataPathHttpsPort;
    }

    public void setDataPathHttpsPort(final Integer dataPathHttpsPort) {
        this.dataPathHttpsPort = dataPathHttpsPort;
    }


    public String getDataPathIpAddress() {
        return this.dataPathIpAddress;
    }

    public void setDataPathIpAddress(final String dataPathIpAddress) {
        this.dataPathIpAddress = dataPathIpAddress;
    }


    public String getDnsName() {
        return this.dnsName;
    }

    public void setDnsName(final String dnsName) {
        this.dnsName = dnsName;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public Date getLastHeartbeat() {
        return this.lastHeartbeat;
    }

    public void setLastHeartbeat(final Date lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

}