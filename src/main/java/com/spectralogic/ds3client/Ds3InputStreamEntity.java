package com.spectralogic.ds3client;


import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Ds3InputStreamEntity extends InputStreamEntity{

    public Ds3InputStreamEntity(final InputStream inStream, final long length, final ContentType contentType) {
        super(inStream, length, contentType);
    }

    @Override
    public void writeTo(final OutputStream outStream) throws IOException {
        IOUtils.copy(this.getContent(), outStream);
    }
}
