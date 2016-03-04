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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.lang.String;
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class StorageDomain {

    // Variables
    @JsonProperty("AutoEjectUponCron")
    private String autoEjectUponCron;

    @JsonProperty("AutoEjectUponJobCancellation")
    private boolean autoEjectUponJobCancellation;

    @JsonProperty("AutoEjectUponJobCompletion")
    private boolean autoEjectUponJobCompletion;

    @JsonProperty("AutoEjectUponMediaFull")
    private boolean autoEjectUponMediaFull;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LtfsFileNaming")
    private LtfsFileNamingMode ltfsFileNaming;

    @JsonProperty("MaxTapeFragmentationPercent")
    private int maxTapeFragmentationPercent;

    @JsonProperty("MaximumAutoVerificationFrequencyInDays")
    private int maximumAutoVerificationFrequencyInDays;

    @JsonProperty("MediaEjectionAllowed")
    private boolean mediaEjectionAllowed;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("VerifyPriorToAutoEject")
    private Priority verifyPriorToAutoEject;

    @JsonProperty("WriteOptimization")
    private WriteOptimization writeOptimization;

    // Constructor
    public StorageDomain() {
        //pass
    }

    // Getters and Setters
    
    public String getAutoEjectUponCron() {
        return this.autoEjectUponCron;
    }

    public void setAutoEjectUponCron(final String autoEjectUponCron) {
        this.autoEjectUponCron = autoEjectUponCron;
    }


    public boolean getAutoEjectUponJobCancellation() {
        return this.autoEjectUponJobCancellation;
    }

    public void setAutoEjectUponJobCancellation(final boolean autoEjectUponJobCancellation) {
        this.autoEjectUponJobCancellation = autoEjectUponJobCancellation;
    }


    public boolean getAutoEjectUponJobCompletion() {
        return this.autoEjectUponJobCompletion;
    }

    public void setAutoEjectUponJobCompletion(final boolean autoEjectUponJobCompletion) {
        this.autoEjectUponJobCompletion = autoEjectUponJobCompletion;
    }


    public boolean getAutoEjectUponMediaFull() {
        return this.autoEjectUponMediaFull;
    }

    public void setAutoEjectUponMediaFull(final boolean autoEjectUponMediaFull) {
        this.autoEjectUponMediaFull = autoEjectUponMediaFull;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public LtfsFileNamingMode getLtfsFileNaming() {
        return this.ltfsFileNaming;
    }

    public void setLtfsFileNaming(final LtfsFileNamingMode ltfsFileNaming) {
        this.ltfsFileNaming = ltfsFileNaming;
    }


    public int getMaxTapeFragmentationPercent() {
        return this.maxTapeFragmentationPercent;
    }

    public void setMaxTapeFragmentationPercent(final int maxTapeFragmentationPercent) {
        this.maxTapeFragmentationPercent = maxTapeFragmentationPercent;
    }


    public int getMaximumAutoVerificationFrequencyInDays() {
        return this.maximumAutoVerificationFrequencyInDays;
    }

    public void setMaximumAutoVerificationFrequencyInDays(final int maximumAutoVerificationFrequencyInDays) {
        this.maximumAutoVerificationFrequencyInDays = maximumAutoVerificationFrequencyInDays;
    }


    public boolean getMediaEjectionAllowed() {
        return this.mediaEjectionAllowed;
    }

    public void setMediaEjectionAllowed(final boolean mediaEjectionAllowed) {
        this.mediaEjectionAllowed = mediaEjectionAllowed;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public Priority getVerifyPriorToAutoEject() {
        return this.verifyPriorToAutoEject;
    }

    public void setVerifyPriorToAutoEject(final Priority verifyPriorToAutoEject) {
        this.verifyPriorToAutoEject = verifyPriorToAutoEject;
    }


    public WriteOptimization getWriteOptimization() {
        return this.writeOptimization;
    }

    public void setWriteOptimization(final WriteOptimization writeOptimization) {
        this.writeOptimization = writeOptimization;
    }

}