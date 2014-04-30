package com.spectralogic.ds3client.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.ByteStreams;
import com.spectralogic.ds3client.helpers.BucketContentGetter.GetObjectFactory;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.Ds3Object;

public class FileGetObject implements GetObject {
    private final Ds3Object ds3Object;
    private final File file;

    public FileGetObject(final String key, final File file) {
        this.ds3Object = new Ds3Object(key);
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
    public void writeContent(final InputStream content) throws IOException {
        final File dir = this.file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        ByteStreams.copy(content, new FileOutputStream(this.file));
    }
    
    public static GetObjectFactory buildFactory(final String prefix) {
        return new GetObjectFactory() {
            @Override
            public GetObject apply(final Contents input) {
                return new FileGetObject(input.getKey(), new File(prefix, input.getKey()));
            }
        };
    }
}
