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
import com.spectralogic.ds3client.models.DataIsolationLevel;
import java.lang.Integer;
import com.spectralogic.ds3client.models.DataPersistenceRuleType;
import com.google.common.net.UrlEscapers;

public class ModifyDataPersistenceRuleSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String dataPersistenceRule;

    private DataIsolationLevel isolationLevel;

    private Integer minimumDaysToRetain;

    private DataPersistenceRuleType type;

    // Constructor
    
    
    public ModifyDataPersistenceRuleSpectraS3Request(final String dataPersistenceRule) {
        this.dataPersistenceRule = dataPersistenceRule;
        
    }

    public ModifyDataPersistenceRuleSpectraS3Request withIsolationLevel(final DataIsolationLevel isolationLevel) {
        this.isolationLevel = isolationLevel;
        this.updateQueryParam("isolation_level", isolationLevel);
        return this;
    }


    public ModifyDataPersistenceRuleSpectraS3Request withMinimumDaysToRetain(final Integer minimumDaysToRetain) {
        this.minimumDaysToRetain = minimumDaysToRetain;
        this.updateQueryParam("minimum_days_to_retain", minimumDaysToRetain);
        return this;
    }


    public ModifyDataPersistenceRuleSpectraS3Request withType(final DataPersistenceRuleType type) {
        this.type = type;
        this.updateQueryParam("type", type);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/data_persistence_rule/" + dataPersistenceRule;
    }
    
    public String getDataPersistenceRule() {
        return this.dataPersistenceRule;
    }


    public DataIsolationLevel getIsolationLevel() {
        return this.isolationLevel;
    }


    public Integer getMinimumDaysToRetain() {
        return this.minimumDaysToRetain;
    }


    public DataPersistenceRuleType getType() {
        return this.type;
    }

}