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
import com.spectralogic.ds3client.commands.BulkGetRequest;
import com.spectralogic.ds3client.commands.BulkGetResponse;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectGetter;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

class BulkGetTransferrer implements BulkTransferExecutor.Transferrer {
    private final Ds3Client client;
    private final ObjectGetter objectGetter;

    public BulkGetTransferrer(final Ds3Client client, final ObjectGetter objectGetter) {
        this.client = client;
        this.objectGetter = objectGetter;
    }

    @Override
    public MasterObjectList prime(final String bucket, final Iterable<Ds3Object> ds3Objects)
            throws SignatureException, IOException, XmlProcessingException {
        final BulkGetRequest request = new BulkGetRequest(bucket, Lists.newArrayList(ds3Objects));
        try (final BulkGetResponse response = this.client.bulkGet(request)) {
            return response.getResult();
        }
    }

    @Override
    public void transfer(final UUID jobId, final String bucket, final Ds3Object ds3Object)
            throws SignatureException, IOException {
        final GetObjectRequest request = new GetObjectRequest(bucket, ds3Object.getName(), jobId);
        try (final GetObjectResponse response = this.client.getObject(request)) {
            this.objectGetter.writeContents(ds3Object.getName(), response.getContent());
        }
    }
}
