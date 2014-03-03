package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.serializer.XmlOutput;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public abstract class BulkResponse extends AbstractResponse {
    private MasterObjectList result;
    public BulkResponse(CloseableHttpResponse response) throws IOException {
        super(response);
    }

    public MasterObjectList getResult() {
        return result;
    }

    @Override
    protected void processResponse() throws IOException {
        checkStatusCode(200);
        final CloseableHttpResponse response = getResponse();

        try(final StringWriter writer = new StringWriter();
            final InputStream content = response.getEntity().getContent()){

            IOUtils.copy(content, writer, UTF8);

            result = XmlOutput.fromXml(writer.toString(), MasterObjectList.class);
        }
    }
}
