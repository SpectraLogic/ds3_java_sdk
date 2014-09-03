package com.spectralogic.ds3client.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

public class ResourceUtils {
    public static File loadFileResource(final String fileName) throws URISyntaxException, FileNotFoundException {

        final URL resourceUrl = ResourceUtils.class.getClassLoader().getResource(fileName);
        if (resourceUrl == null) {
            throw new FileNotFoundException(fileName);
        }
        return new File(resourceUrl.toURI());
    }
}
