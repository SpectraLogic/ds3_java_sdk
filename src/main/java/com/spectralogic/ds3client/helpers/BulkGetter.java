package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;
import java.util.UUID;

import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.BulkGetRequest;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

public class BulkGetter {
    private final BulkTransferExecutor<GetObject> bulkHelper;

    public BulkGetter(final Ds3Client client) {
        this.bulkHelper = new BulkTransferExecutor<GetObject>(client);
    }

    public BulkGetter(final ListeningExecutorService service, final Ds3Client client) {
        this.bulkHelper = new BulkTransferExecutor<GetObject>(service, client);
    }
    
    /**
     * Performs a bulk put of {@code objectsToPut} into {@code bucket}. 
     * 
     * @param bucket
     * @param objectsToGet
     * @return A future containing the number of objects put or the resulting exception.
     */
    public CheckedFuture<Integer, Ds3BulkException> execute(final String bucket, final Iterable<GetObject> objectsToGet) {
        return this.bulkHelper.transfer(bucket, objectsToGet, this.buildPartPerformer(bucket));
    }

    private BulkTransferExecutor.Transferrer<GetObject> buildPartPerformer(final String bucket) {
        return new BulkTransferExecutor.Transferrer<GetObject>() {
            @Override
            public MasterObjectList prime(final List<Ds3Object> ds3Objects)
                    throws SignatureException, IOException, XmlProcessingException {
                return BulkGetter.this.bulkHelper.getClient().bulkGet(new BulkGetRequest(bucket, ds3Objects)).getResult();
            }
            
            @Override
            public void transfer(final UUID jobId, final Ds3Object ds3Object, final GetObject object)
                    throws Ds3KeyNotFoundException, IOException, SignatureException {
                object.writeContent(
                    BulkGetter.this.bulkHelper
                        .getClient()
                        .getObject(new GetObjectRequest(bucket, ds3Object.getName(), jobId))
                        .getContent()
                );
            }
        };
    }
}
