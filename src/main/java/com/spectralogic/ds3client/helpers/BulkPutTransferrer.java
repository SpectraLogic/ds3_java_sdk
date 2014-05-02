package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.BulkPutRequest;
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
        return this.client.bulkPut(new BulkPutRequest(bucket, Lists.newArrayList(ds3Objects))).getResult();
    }

    @Override
    public void transfer(final UUID jobId, final String bucket, final Ds3Object ds3Object)
            throws SignatureException, IOException {
        this.client.putObject(new PutObjectRequest(
            bucket,
            ds3Object.getName(),
            jobId,
            ds3Object.getSize(),
            this.putter.getContent(ds3Object.getName())
        ));
    }
}
