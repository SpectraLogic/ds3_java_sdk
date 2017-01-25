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
package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.lang.Long;
import java.lang.String;
import java.util.Date;
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class FeatureKey {

    // Variables
    @JsonProperty("CurrentValue")
    private Long currentValue;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("ExpirationDate")
    private Date expirationDate;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("Key")
    private FeatureKeyType key;

    @JsonProperty("LimitValue")
    private Long limitValue;

    // Constructor
    public FeatureKey() {
        //pass
    }

    // Getters and Setters
    
    public Long getCurrentValue() {
        return this.currentValue;
    }

    public void setCurrentValue(final Long currentValue) {
        this.currentValue = currentValue;
    }


    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public FeatureKeyType getKey() {
        return this.key;
    }

    public void setKey(final FeatureKeyType key) {
        this.key = key;
    }


    public Long getLimitValue() {
        return this.limitValue;
    }

    public void setLimitValue(final Long limitValue) {
        this.limitValue = limitValue;
    }

}