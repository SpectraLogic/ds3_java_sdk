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
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import java.util.UUID;

public class ConvertStorageDomainToDs3TargetSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String storageDomain;

    private final String convertToDs3Target;

    // Constructor
    
    
    public ConvertStorageDomainToDs3TargetSpectraS3Request(final UUID convertToDs3Target, final String storageDomain) {
        this.storageDomain = storageDomain;
        this.convertToDs3Target = convertToDs3Target.toString();
        
        this.updateQueryParam("convert_to_ds3_target", convertToDs3Target);

    }

    
    public ConvertStorageDomainToDs3TargetSpectraS3Request(final String convertToDs3Target, final String storageDomain) {
        this.storageDomain = storageDomain;
        this.convertToDs3Target = convertToDs3Target;
        
        this.updateQueryParam("convert_to_ds3_target", convertToDs3Target);

    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/storage_domain/" + storageDomain;
    }
    
    public String getStorageDomain() {
        return this.storageDomain;
    }


    public String getConvertToDs3Target() {
        return this.convertToDs3Target;
    }

}