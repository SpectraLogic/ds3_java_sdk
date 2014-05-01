package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;
import java.util.UUID;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.BulkPutRequest;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

class BulkPutTransferrer implements BulkTransferExecutor.Transferrer<PutObject> {
    private final Ds3Client client;

    public BulkPutTransferrer(final Ds3Client client) {
        this.client = client;
    }

    @Override
    public MasterObjectList prime(final String bucket, final List<Ds3Object> ds3Objects)
            throws SignatureException, IOException, XmlProcessingException {
        return this.client.bulkPut(new BulkPutRequest(bucket, ds3Objects)).getResult();
    }

    @Override
    public void transfer(
            final UUID jobId,
            final String bucket,
            final Ds3Object ds3Object,
            final PutObject putObject)
            throws Ds3KeyNotFoundException, IOException, SignatureException {
        this.client.putObject(new PutObjectRequest(
            bucket,
            ds3Object.getName(),
            jobId,
            ds3Object.getSize(),
            putObject.getContent()
        ));
    }
}
