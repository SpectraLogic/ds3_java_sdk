package com.spectralogic.ds3client.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.spectralogic.ds3client.models.Ds3Object;

public class FilePutObject implements PutObject {
    private final Ds3Object ds3Object;
    private final File file;

    public FilePutObject(final String key, final File file) {
        this.ds3Object = new Ds3Object(key, file.length());
        this.file = file;
    }

    @Override
    public String getKey() {
        return this.ds3Object.getName();
    }

    @Override
    public Ds3Object getObject() {
        return this.ds3Object;
    }

    @Override
    public InputStream getContent() throws IOException {
        return new FileInputStream(this.file);
    }
}
