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

import com.spectralogic.ds3client.models.AzureTargetReadPreference;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.commands.interfaces.AbstractResponse;

public class GetAzureTargetReadPreferenceSpectraS3Response extends AbstractResponse {
    
    private final AzureTargetReadPreference azureTargetReadPreferenceResult;

    public GetAzureTargetReadPreferenceSpectraS3Response(final AzureTargetReadPreference azureTargetReadPreferenceResult, final String checksum, final ChecksumType.Type checksumType) {
        super(checksum, checksumType);
        this.azureTargetReadPreferenceResult = azureTargetReadPreferenceResult;
    }

    public AzureTargetReadPreference getAzureTargetReadPreferenceResult() {
        return this.azureTargetReadPreferenceResult;
    }

}