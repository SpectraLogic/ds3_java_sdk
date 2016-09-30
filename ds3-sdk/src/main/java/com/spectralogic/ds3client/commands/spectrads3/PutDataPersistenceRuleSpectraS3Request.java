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
import java.util.UUID;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.DataIsolationLevel;
import com.spectralogic.ds3client.models.DataPersistenceRuleType;
import java.lang.Integer;

public class PutDataPersistenceRuleSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String dataPolicyId;

    private final DataIsolationLevel isolationLevel;

    private final String storageDomainId;

    private final DataPersistenceRuleType type;

    private Integer minimumDaysToRetain;

    // Constructor
    
    
    public PutDataPersistenceRuleSpectraS3Request(final UUID dataPolicyId, final DataIsolationLevel isolationLevel, final UUID storageDomainId, final DataPersistenceRuleType type) {
        this.dataPolicyId = dataPolicyId.toString();
        this.isolationLevel = isolationLevel;
        this.storageDomainId = storageDomainId.toString();
        this.type = type;
        
        this.getQueryParams().put("data_policy_id", dataPolicyId.toString());
        this.getQueryParams().put("isolation_level", isolationLevel.toString());
        this.getQueryParams().put("storage_domain_id", storageDomainId.toString());
        this.getQueryParams().put("type", type.toString());
    }

    
    public PutDataPersistenceRuleSpectraS3Request(final String dataPolicyId, final DataIsolationLevel isolationLevel, final String storageDomainId, final DataPersistenceRuleType type) {
        this.dataPolicyId = dataPolicyId;
        this.isolationLevel = isolationLevel;
        this.storageDomainId = storageDomainId;
        this.type = type;
        
        this.getQueryParams().put("data_policy_id", UrlEscapers.urlFragmentEscaper().escape(dataPolicyId).replace("+", "%2B"));
        this.getQueryParams().put("isolation_level", isolationLevel.toString());
        this.getQueryParams().put("storage_domain_id", UrlEscapers.urlFragmentEscaper().escape(storageDomainId).replace("+", "%2B"));
        this.getQueryParams().put("type", type.toString());
    }

    public PutDataPersistenceRuleSpectraS3Request withMinimumDaysToRetain(final Integer minimumDaysToRetain) {
        this.minimumDaysToRetain = minimumDaysToRetain;
        this.updateQueryParam("minimum_days_to_retain", minimumDaysToRetain);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/data_persistence_rule";
    }
    
    public String getDataPolicyId() {
        return this.dataPolicyId;
    }


    public DataIsolationLevel getIsolationLevel() {
        return this.isolationLevel;
    }


    public String getStorageDomainId() {
        return this.storageDomainId;
    }


    public DataPersistenceRuleType getType() {
        return this.type;
    }


    public Integer getMinimumDaysToRetain() {
        return this.minimumDaysToRetain;
    }

}