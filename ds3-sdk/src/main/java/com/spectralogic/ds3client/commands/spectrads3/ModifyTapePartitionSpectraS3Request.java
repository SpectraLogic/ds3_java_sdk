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
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.spectralogic.ds3client.models.Quiesced;
import com.google.common.net.UrlEscapers;

public class ModifyTapePartitionSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String tapePartition;

    private boolean autoCompactionEnabled;

    private int minimumReadReservedDrives;

    private int minimumWriteReservedDrives;

    private Quiesced quiesced;

    // Constructor
    
    
    public ModifyTapePartitionSpectraS3Request(final String tapePartition) {
        this.tapePartition = tapePartition;
        
    }

    public ModifyTapePartitionSpectraS3Request withAutoCompactionEnabled(final boolean autoCompactionEnabled) {
        this.autoCompactionEnabled = autoCompactionEnabled;
        this.updateQueryParam("auto_compaction_enabled", autoCompactionEnabled);
        return this;
    }


    public ModifyTapePartitionSpectraS3Request withMinimumReadReservedDrives(final int minimumReadReservedDrives) {
        this.minimumReadReservedDrives = minimumReadReservedDrives;
        this.updateQueryParam("minimum_read_reserved_drives", minimumReadReservedDrives);
        return this;
    }


    public ModifyTapePartitionSpectraS3Request withMinimumWriteReservedDrives(final int minimumWriteReservedDrives) {
        this.minimumWriteReservedDrives = minimumWriteReservedDrives;
        this.updateQueryParam("minimum_write_reserved_drives", minimumWriteReservedDrives);
        return this;
    }


    public ModifyTapePartitionSpectraS3Request withQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
        this.updateQueryParam("quiesced", quiesced);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/tape_partition/" + tapePartition;
    }
    
    public String getTapePartition() {
        return this.tapePartition;
    }


    public boolean getAutoCompactionEnabled() {
        return this.autoCompactionEnabled;
    }


    public int getMinimumReadReservedDrives() {
        return this.minimumReadReservedDrives;
    }


    public int getMinimumWriteReservedDrives() {
        return this.minimumWriteReservedDrives;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }

}