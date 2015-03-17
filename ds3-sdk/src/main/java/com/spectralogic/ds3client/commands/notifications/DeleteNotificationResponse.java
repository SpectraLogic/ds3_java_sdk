package com.spectralogic.ds3client.commands.notifications;

import com.spectralogic.ds3client.commands.AbstractResponse;
import com.spectralogic.ds3client.networking.WebResponse;

import java.io.IOException;

public class DeleteNotificationResponse extends AbstractResponse {
    public DeleteNotificationResponse(final WebResponse response) throws IOException {
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
