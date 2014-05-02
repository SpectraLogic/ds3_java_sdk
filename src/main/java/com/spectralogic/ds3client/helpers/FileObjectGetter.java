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
        final File file = new File(this.root, key);
        ensureParentDirectory(file);
        try (final FileOutputStream output = new FileOutputStream(file)) {
            IOUtils.copy(contents, output);
        }
    }

    private void ensureParentDirectory(final File file) {
        final File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
    }
}
