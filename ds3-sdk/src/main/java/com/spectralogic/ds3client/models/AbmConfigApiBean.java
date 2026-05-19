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
package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@JacksonXmlRootElement(namespace = "Data")
public class AbmConfigApiBean {

    // Variables
    @JsonProperty("DataPolicies")
    @JacksonXmlElementWrapper(useWrapping = true)
    private List<DataPolicyApiBean> dataPolicies = new ArrayList<>();

    @JsonProperty("Message")
    private String message;

    @JsonProperty("PoolPartitions")
    @JacksonXmlElementWrapper(useWrapping = true)
    private List<PoolPartitionApiBean> poolPartitions = new ArrayList<>();

    @JsonProperty("StorageDomains")
    @JacksonXmlElementWrapper(useWrapping = true)
    private List<StorageDomainApiBean> storageDomains = new ArrayList<>();

    @JsonProperty("TapePartitions")
    @JacksonXmlElementWrapper(useWrapping = true)
    private List<TapePartitionApiBean> tapePartitions = new ArrayList<>();

    @JsonProperty("Targets")
    @JacksonXmlElementWrapper(useWrapping = true)
    private List<TargetApiBean> targets = new ArrayList<>();

    // Constructor
    public AbmConfigApiBean() {
        //pass
    }

    // Getters and Setters
    
    public List<DataPolicyApiBean> getDataPolicies() {
        return this.dataPolicies;
    }

    public void setDataPolicies(final List<DataPolicyApiBean> dataPolicies) {
        this.dataPolicies = dataPolicies;
    }


    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }


    public List<PoolPartitionApiBean> getPoolPartitions() {
        return this.poolPartitions;
    }

    public void setPoolPartitions(final List<PoolPartitionApiBean> poolPartitions) {
        this.poolPartitions = poolPartitions;
    }


    public List<StorageDomainApiBean> getStorageDomains() {
        return this.storageDomains;
    }

    public void setStorageDomains(final List<StorageDomainApiBean> storageDomains) {
        this.storageDomains = storageDomains;
    }


    public List<TapePartitionApiBean> getTapePartitions() {
        return this.tapePartitions;
    }

    public void setTapePartitions(final List<TapePartitionApiBean> tapePartitions) {
        this.tapePartitions = tapePartitions;
    }


    public List<TargetApiBean> getTargets() {
        return this.targets;
    }

    public void setTargets(final List<TargetApiBean> targets) {
        this.targets = targets;
    }

}