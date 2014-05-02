package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.BulkGetRequest;
import com.spectralogic.ds3client.commands.GetObjectRequest;
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
        return this.client
            .bulkGet(new BulkGetRequest(bucket, Lists.newArrayList(ds3Objects)))
            .getResult();
    }

    @Override
    public void transfer(final UUID jobId, final String bucket, final Ds3Object ds3Object)
            throws SignatureException, IOException {
        this.objectGetter.writeContents(
            ds3Object.getName(),
            this.client
                .getObject(new GetObjectRequest(bucket, ds3Object.getName(), jobId))
                .getContent()
        );
    }
}
