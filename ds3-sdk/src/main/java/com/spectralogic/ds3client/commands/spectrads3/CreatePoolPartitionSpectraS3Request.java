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

import com.spectralogic.ds3client.commands.AbstractRequest;
import com.spectralogic.ds3client.HttpVerb;
import java.lang.String;
import com.spectralogic.ds3client.models.PoolType;

public class CreatePoolPartitionSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String poolPartition;

    private final String name;

    private final PoolType type;


    // Constructor
    public CreatePoolPartitionSpectraS3Request(final String name, final String poolPartition, final PoolType type) {
        this.poolPartition = poolPartition;
        this.name = name;
        this.type = type;
        
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/pool_partition/" + poolPartition;
    }
    
    public String getPoolPartition() {
        return this.poolPartition;
    }


    public String getName() {
        return this.name;
    }


    public PoolType getType() {
        return this.type;
    }


}