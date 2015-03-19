package com.spectralogic.ds3client.commands.notifications;

import com.spectralogic.ds3client.commands.AbstractResponse;
import com.spectralogic.ds3client.models.notification.NotificationRegistration;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class NotificationResponse extends AbstractResponse {
    private NotificationRegistration registration;
    public NotificationResponse(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try (final WebResponse response = this.getResponse()) {
            if (response == null) {
                throw new IOException("response was null");
            }
            this.checkStatusCode(200, 201);

            try (final InputStream content = response.getResponseStream()) {
                final StringWriter writer = new StringWriter();
                IOUtils.copy(content, writer, UTF8);
                this.registration = XmlOutput.fromXml(writer.toString(), NotificationRegistration.class);
            }
        }
    }

    public NotificationRegistration getRegistration() {
        return this.registration;
    }
}
