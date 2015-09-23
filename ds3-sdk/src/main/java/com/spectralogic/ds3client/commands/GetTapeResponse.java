package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.models.tape.Tape;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;

import java.io.IOException;
import java.io.InputStream;

public class GetTapeResponse extends AbstractResponse {

    private Tape tape;

    public GetTapeResponse(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try (final WebResponse response = this.getResponse()) {
            checkStatusCode(200);
            try (final InputStream stream = response.getResponseStream()) {
                this.tape = XmlOutput.fromXml(stream, Tape.class);
            }
        }
    }

    public Tape getTape() {
        return tape;
    }
}
