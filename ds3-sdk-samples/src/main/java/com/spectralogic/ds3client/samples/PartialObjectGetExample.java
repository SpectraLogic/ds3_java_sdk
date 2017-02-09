/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.samples;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectGetter;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.PartialDs3Object;
import com.spectralogic.ds3client.utils.ResourceUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartialObjectGetExample {
    public static void main(final String args[]) throws IOException, URISyntaxException {
        // The bucket that we will be writing to
        final String bucketName = "my_bucket";

        try (final Ds3Client client = Ds3ClientBuilder.fromEnv()
                .withHttps(false)
                .build()) {

            // Wrap the Ds3Client with the helper functions
            final Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(client);

            // Loading some test data
            putBookData(helper, bucketName);

            final List<Ds3Object> filesToGet = new ArrayList<>();

            // Specify the object that you want to get and the range from that object you want to retrieve
            filesToGet.add(new PartialDs3Object("beowulf.txt", Range.byLength(0, 100)));

            // You can specify multiple ranges by creating more PartialDs3Objects
            filesToGet.add(new PartialDs3Object("beowulf.txt", Range.byLength(200, 100)));

            // You can also mix regular object gets with partial object gets
            filesToGet.add(new Ds3Object("ulysses.txt"));

            // When the helper function writes the data to a file it will write it in the sorted over of the Ranges
            // where the range with the lowest starting offset is first.  Any ranges that overlap will be consolidated
            // into a single range, and all the ranges will be written to the same file.

            final Ds3ClientHelpers.Job job = helper.startReadJob(bucketName, filesToGet);
            final Path outputPath = Paths.get("output");

            job.transfer(new FileObjectGetter(outputPath));
        }
    }

    private static final String[] BOOKS = new String[]{"beowulf.txt", "sherlock_holmes.txt", "tale_of_two_cities.txt", "ulysses.txt"};

    private static void putBookData(final Ds3ClientHelpers helper, final String bucketName) throws IOException, URISyntaxException {
        helper.ensureBucketExists(bucketName);

        final List<Ds3Object> ds3Objects = new ArrayList<>();
        final Map<String, Path> objectPaths = new HashMap<>();

        for (final String book : BOOKS) {
            final Path bookPath = Paths.get("books/", book);
            final Path resourcePath = ResourceUtils.loadFileResource(bookPath);
            objectPaths.put(book, resourcePath);
            ds3Objects.add(new Ds3Object(book, Files.size(resourcePath)));
        }

        final Ds3ClientHelpers.Job job = helper.startWriteJob(bucketName, ds3Objects);

        job.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                return FileChannel.open(objectPaths.get(key), StandardOpenOption.READ);
            }
        });
    }
}
