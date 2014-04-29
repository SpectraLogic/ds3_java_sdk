package com.spectralogic.ds3client.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FilePutObject implements PutObject {
    private final String key;
    private final File file;

    public FilePutObject(final String key, final File file) {
        this.key = key;
        this.file = file;
    }
    
    @Override
    public String getKey() {
        return key;
    }

    @Override
    public long getSize() {
        return this.file.length();
    }

    @Override
    public InputStream getContents() throws IOException {
        return new FileInputStream(this.file);
    }
}
