package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.WebResponse;

import java.io.IOException;

public class DeleteTapeResponse extends AbstractResponse {
    public DeleteTapeResponse(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try {
            checkStatusCode(204);
        } finally {
            getResponse().close();
        }
    }
}
