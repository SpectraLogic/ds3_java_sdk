package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.util.List;

public class BulkGetRequest extends BulkRequest {
    public BulkGetRequest(String bucket, List<Ds3Object> objects) throws XmlProcessingException {
        super(bucket, objects);
        getQueryParams().put("operation", "start_bulk_get");
    }

    @Override
    public BulkCommand getCommand() {
        return BulkCommand.GET;
    }
}
