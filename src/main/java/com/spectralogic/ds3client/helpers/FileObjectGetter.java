package com.spectralogic.ds3client.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectGetter;

public class FileObjectGetter implements ObjectGetter {
    private final File root;

    public FileObjectGetter(final File root) {
        this.root = root;
    }

    @Override
    public void writeContents(final String key, final InputStream contents) throws IOException {
        try (final FileOutputStream output = new FileOutputStream(new File(this.root, key))) {
            IOUtils.copy(contents, output);
        }
    }
}
