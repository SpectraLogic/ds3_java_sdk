package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.models.tape.Tapes;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;

import java.io.IOException;
import java.io.InputStream;

public class GetTapesResponse extends AbstractResponse {

    private Tapes tapes;

    public GetTapesResponse(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try (final WebResponse response = getResponse()) {
            checkStatusCode(200);
            try (final InputStream stream = response.getResponseStream()) {
                this.tapes = XmlOutput.fromXml(stream, Tapes.class);
            }
        }
    }

    public Tapes getTapes() {
        return this.tapes;
    }
}
