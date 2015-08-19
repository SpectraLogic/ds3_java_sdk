package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.WebResponse;

import java.io.IOException;

public class GetPhysicalPlacementResponse extends AbstractResponse {
    public GetPhysicalPlacementResponse(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {

    }
}
