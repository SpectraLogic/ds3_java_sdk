/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3client.utils;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceUtils {

    public static Path loadFileResource(final Path filePath) throws FileNotFoundException, URISyntaxException {
        return loadFileResource(filePath.toString());
    }

    public static Path loadFileResource(final String fileName) throws URISyntaxException, FileNotFoundException {
        final URL resourceUrl = ResourceUtils.class.getClassLoader().getResource(fileName);
        if (resourceUrl == null) {
            throw new FileNotFoundException(fileName);
        }
        return Paths.get(resourceUrl.toURI());
    }
}
