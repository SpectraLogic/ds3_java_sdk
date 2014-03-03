package com.spectralogic.ds3client.commands;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

public class BulkPutResponse extends BulkResponse {
    public BulkPutResponse(CloseableHttpResponse response) throws IOException {
        super(response);
    }
}
