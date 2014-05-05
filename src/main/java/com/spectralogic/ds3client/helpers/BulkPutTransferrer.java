/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.BulkPutRequest;
import com.spectralogic.ds3client.commands.BulkPutResponse;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectPutter;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

class BulkPutTransferrer implements BulkTransferExecutor.Transferrer {
    private final Ds3Client client;
    private final ObjectPutter putter;

    public BulkPutTransferrer(final Ds3Client client, final ObjectPutter putter) {
        this.client = client;
        this.putter = putter;
    }

    @Override
    public MasterObjectList prime(final String bucket, final Iterable<Ds3Object> ds3Objects)
            throws SignatureException, IOException, XmlProcessingException {
        final BulkPutRequest request = new BulkPutRequest(bucket, Lists.newArrayList(ds3Objects));
        try (final BulkPutResponse response = this.client.bulkPut(request)) {
            return response.getResult();
        }
    }

    @Override
    public void transfer(final UUID jobId, final String bucket, final Ds3Object ds3Object)
            throws SignatureException, IOException {
        final PutObjectRequest request = new PutObjectRequest(
            bucket,
            ds3Object.getName(),
            jobId,
            ds3Object.getSize(),
            this.putter.getContent(ds3Object.getName())
        );
        this.client.putObject(request).close();
    }
}
