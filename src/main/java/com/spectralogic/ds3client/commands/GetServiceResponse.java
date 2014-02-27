package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.models.ListAllMyBucketsResult;
import com.spectralogic.ds3client.serializer.XmlOutput;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class GetServiceResponse extends AbstractResponse {

    private ListAllMyBucketsResult result;

    public GetServiceResponse(CloseableHttpResponse response) throws IOException {
        super(response);
    }

    public ListAllMyBucketsResult getResult() {
        return result;
    }

    @Override
    protected void processResponse() throws IOException {
        checkStatusCode(200);
        final CloseableHttpResponse response = getResponse();

        try (final InputStream content = response.getEntity().getContent();
             final StringWriter writer = new StringWriter()) {
            IOUtils.copy(content, writer, UTF8);
            result = XmlOutput.fromXml(writer.toString(), ListAllMyBucketsResult.class);
        }
    }
}
