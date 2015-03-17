package com.spectralogic.ds3client.commands.notifications;

import com.spectralogic.ds3client.commands.AbstractResponse;
import com.spectralogic.ds3client.networking.WebResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.StringWriter;

public class CreateNotificationResponse extends AbstractResponse {
    CreateNotificationResponse(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try (final WebResponse response = this.getResponse()) {
            this.checkStatusCode(200);


            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getResponseStream(), writer, UTF8);
            System.out.print(writer.toString());
        }
    }
}
