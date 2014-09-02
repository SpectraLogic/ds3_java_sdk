package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.ResettableFileInputStream;
import com.spectralogic.ds3client.utils.ResourceUtils;

import java.io.*;
import java.net.URISyntaxException;

public class ResourceObjectPutter implements Ds3ClientHelpers.ObjectPutter {

    private final String basePath;

    public ResourceObjectPutter(final String basePath) {
        this.basePath = basePath;
    }

    @Override
    public InputStream getContent(final String key) throws IOException {

        try {
            return new ResettableFileInputStream(new FileInputStream(ResourceUtils.loadFileResource(basePath + key)));
        } catch (final URISyntaxException e) {
            e.printStackTrace();
            throw new FileNotFoundException(key);
        }
    }
}
