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

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.IWriteJob;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectPutter;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

class WriteJob extends Job implements IWriteJob {
    public WriteJob(
            final Ds3Client client,
            final UUID jobId,
            final String bucketName,
            final Iterable<Objects> objectLists) {
        super(client, jobId, bucketName, objectLists);
    }

    @Override
    public void write(final ObjectPutter putter)
            throws SignatureException, IOException, XmlProcessingException {
        this.transferAll(new Transferrer() {
            @Override
            public void Transfer(
                    final Ds3Client client,
                    final UUID jobId,
                    final String bucketName,
                    final Ds3Object ds3Object) throws SignatureException, IOException {
                final PutObjectRequest request = new PutObjectRequest(
                    bucketName,
                    ds3Object.getName(),
                    jobId,
                    ds3Object.getSize(),
                    putter.getContent(ds3Object.getName())
                );
                client.putObject(request).close();
            }
        });
    }
}
