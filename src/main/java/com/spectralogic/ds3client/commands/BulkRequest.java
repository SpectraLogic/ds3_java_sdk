package com.spectralogic.ds3client.commands;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;


public abstract class BulkRequest extends AbstractRequest {

    private final String bucket;
    private final List<Ds3Object> ds3Objects;
    private final InputStream stream;
    private int size;

    public BulkRequest(final String bucket, final List<Ds3Object> objects) throws XmlProcessingException {
        this.bucket = bucket;
        this.ds3Objects = objects;
        this.stream = generateStream();
    }

    private InputStream generateStream() throws XmlProcessingException {
        final com.spectralogic.ds3client.models.Objects objects =
                new com.spectralogic.ds3client.models.Objects();
        objects.setObject(ds3Objects);
        final String xmlOutput = XmlOutput.toXml(objects, getCommand());

        byte[] stringBytes = xmlOutput.getBytes();
        this.size = stringBytes.length;
        return new ByteArrayInputStream(stringBytes);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String getPath() {
        return "/_rest_/buckets/" + bucket;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    public abstract BulkCommand getCommand ();

    @Override
    public InputStream getStream() {
        return stream;
    }
}
