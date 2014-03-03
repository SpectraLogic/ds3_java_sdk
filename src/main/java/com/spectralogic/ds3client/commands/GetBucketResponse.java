package com.spectralogic.ds3client.commands;


import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.serializer.XmlOutput;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.StringWriter;

public class GetBucketResponse extends AbstractResponse {

    private ListBucketResult result;

    public GetBucketResponse(CloseableHttpResponse response) throws IOException {
        super(response);

    }

    public ListBucketResult getResult() {
        return result;
    }

    @Override
    protected void processResponse() throws IOException {
        checkStatusCode(200);
        final StringWriter writer = new StringWriter();
        final CloseableHttpResponse response = getResponse();

        IOUtils.copy(response.getEntity().getContent(), writer, UTF8);
        this.result = XmlOutput.fromXml(writer.toString(), ListBucketResult.class);
    }
}
