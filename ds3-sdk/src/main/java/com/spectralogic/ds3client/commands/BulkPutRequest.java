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

package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.Priority;
import com.spectralogic.ds3client.models.bulk.WriteOptimization;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.util.List;

public class BulkPutRequest extends BulkRequest {

    private static final String MAX_UPLOAD_SIZE_IN_BYTES = "100000000000";
    public static final int MIN_UPLOAD_SIZE_IN_BYTES = 10485760;

    public BulkPutRequest(final String bucket, final List<Ds3Object> objects) throws XmlProcessingException {
        super(bucket, objects);
        this.getQueryParams().put("operation", "start_bulk_put");
    }

    @Override
    public BulkPutRequest withPriority(final Priority priority) {
        super.withPriority(priority);
        return this;
    }

    /**
     * Sets the chunk size for this job.
     * @param size The chunk size in bytes.  If the value passed in is less than MIN_UPLOAD_SIZE_IN_BYTES, then
     *             the default size will be used.
     */
    public BulkPutRequest withMaxUploadSize(final int size) {
        if (size > MIN_UPLOAD_SIZE_IN_BYTES) {
            this.getQueryParams().put("max_upload_size", Long.toString(size));
        }
        else {
            this.getQueryParams().put("max_upload_size", MAX_UPLOAD_SIZE_IN_BYTES);
        }
        return this;
    }

    @Override
    public BulkPutRequest withWriteOptimization(final WriteOptimization writeOptimization) {
        super.withWriteOptimization(writeOptimization);
        return this;
    }

    @Override
    public BulkCommand getCommand() {
        return BulkCommand.PUT;
    }
}
