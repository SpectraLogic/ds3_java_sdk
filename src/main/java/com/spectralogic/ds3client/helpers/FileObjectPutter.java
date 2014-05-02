package com.spectralogic.ds3client.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectPutter;


public class FileObjectPutter implements ObjectPutter {
    private final File root;

    public FileObjectPutter(final File root) {
        this.root = root;
    }

    @Override
    public InputStream getContent(final String key) throws IOException {
        return new FileInputStream(new File(this.root, key));
    }
}
