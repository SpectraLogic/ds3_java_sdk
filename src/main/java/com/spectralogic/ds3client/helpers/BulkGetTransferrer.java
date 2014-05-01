package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;
import java.util.UUID;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.BulkGetRequest;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

class BulkGetTransferrer implements BulkTransferExecutor.Transferrer<GetObject> {
    private final Ds3Client client;

    public BulkGetTransferrer(final Ds3Client client) {
        this.client = client;
    }

    @Override
    public MasterObjectList prime(final String bucket, final List<Ds3Object> ds3Objects)
            throws SignatureException, IOException, XmlProcessingException {
        return this.client
            .bulkGet(new BulkGetRequest(bucket, ds3Objects))
            .getResult();
    }

    @Override
    public void transfer(
            final UUID jobId,
            final String bucket,
            final Ds3Object ds3Object,
            final GetObject object)
            throws Ds3KeyNotFoundException, IOException, SignatureException {
        object.writeContent(
            this.client
                .getObject(new GetObjectRequest(bucket, ds3Object.getName(), jobId))
                .getContent()
        );
    }
}