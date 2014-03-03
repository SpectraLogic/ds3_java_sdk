package com.spectralogic.ds3client.commands;


import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

public class BulkGetResponse extends BulkResponse {
    public BulkGetResponse(CloseableHttpResponse response) throws IOException {
        super(response);
    }
}
