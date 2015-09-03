package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.WebResponse;
import java.io.IOException;

public class DeleteFolderResponse extends AbstractResponse {

    public DeleteFolderResponse(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try {
            this.checkStatusCode(204);
        } finally {
            this.getResponse().close();
        }
    }
}
