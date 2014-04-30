package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;
import java.util.UUID;

import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.BulkPutRequest;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

public class BulkPutter {
    private final BulkTransferExecutor<PutObject> bulkHelper;

    public BulkPutter(final Ds3Client client) {
        this.bulkHelper = new BulkTransferExecutor<PutObject>(client);
    }

    public BulkPutter(final ListeningExecutorService service, final Ds3Client client) {
        this.bulkHelper = new BulkTransferExecutor<PutObject>(service, client);
    }
    
    /**
     * Performs a bulk put of {@code objectsToPut} into {@code bucket}. 
     * 
     * @param bucket
     * @param objectsToPut
     * @return A future containing the number of objects put or the resulting exception.
     */
    public CheckedFuture<Integer, Ds3BulkException> execute(final String bucket, final Iterable<PutObject> objectsToPut) {
        return this.bulkHelper.transfer(bucket, objectsToPut, this.buildPartPerformer(bucket));
    }

    private BulkTransferExecutor.Transferrer<PutObject> buildPartPerformer(final String bucket) {
        return new BulkTransferExecutor.Transferrer<PutObject>() {
            @Override
            public MasterObjectList prime(final List<Ds3Object> ds3Objects)
                    throws SignatureException, IOException, XmlProcessingException {
                return BulkPutter.this.bulkHelper.getClient().bulkPut(new BulkPutRequest(bucket, ds3Objects)).getResult();
            }
            
            @Override
            public void transfer(final UUID jobId, final Ds3Object ds3Object, final PutObject putObject)
                    throws Ds3KeyNotFoundException, IOException, SignatureException {
                BulkPutter.this.bulkHelper.getClient().putObject(new PutObjectRequest(
                    bucket,
                    ds3Object.getName(),
                    jobId,
                    ds3Object.getSize(),
                    putObject.getContent()
                ));
            }
        };
    }
}
