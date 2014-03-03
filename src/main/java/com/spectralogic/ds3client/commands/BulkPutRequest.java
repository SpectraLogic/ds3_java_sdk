package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.util.List;

public class BulkPutRequest extends BulkRequest {
    public BulkPutRequest(String bucket, List<Ds3Object> objects) throws XmlProcessingException {
        super(bucket, objects);
        getHeaders().put("operation", "start_bulk_put");
    }

    @Override
    public BulkCommand getCommand() {
        return BulkCommand.PUT;
    }
}
