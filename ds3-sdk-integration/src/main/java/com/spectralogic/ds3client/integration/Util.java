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

package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.DeleteBucketRequest;
import com.spectralogic.ds3client.commands.DeleteObjectRequest;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static final String RESOURCE_BASE_NAME = "books/";

    private Util() {}

    public static Ds3Client fromEnv() {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.fromEnv();
        builder.withHttps(false);
        return builder.build();
    }

    public static Ds3Client insecureFromEnv() {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.fromEnv();
        builder.withCertificateVerification(false);
        return builder.build();
    }

    private static final String[] BOOKS = {"beowulf.txt", "sherlock_holmes.txt", "tale_of_two_cities.txt", "ulysses.txt"};
    public static void loadBookTestData(final Ds3Client client, final String bucketName) throws IOException, SignatureException, XmlProcessingException, URISyntaxException {

        getLoadJob(client, bucketName, RESOURCE_BASE_NAME)
            .transfer(new ResourceObjectPutter(RESOURCE_BASE_NAME));
    }

    public static Ds3ClientHelpers.Job getLoadJob(final Ds3Client client, final String bucketName, final String resourceBaseName) throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        final List<Ds3Object> objects = new ArrayList<>();

        for(final String book : BOOKS) {
            final File objFile = ResourceUtils.loadFileResource(resourceBaseName + book);
            final Ds3Object obj = new Ds3Object(book, objFile.length());

            objects.add(obj);
        }

        return helpers
                .startWriteJob(bucketName, objects);
    }

    public static void loadBookTestDataWithPrefix(final Ds3Client client, final String bucketName, final String prefix) throws XmlProcessingException, SignatureException, IOException, URISyntaxException {
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        final List<Ds3Object> objects = new ArrayList<>();

        for(final String book : BOOKS) {
            final File objFile = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book);
            final Ds3Object obj = new Ds3Object(prefix + book, objFile.length());

            objects.add(obj);
        }

        helpers.startWriteJob(bucketName, objects).transfer(new PrefixedObjectPutter(new ResourceObjectPutter(RESOURCE_BASE_NAME), prefix));

    }


    public static void deleteAllContents(final Ds3Client client, final String bucketName) throws IOException, SignatureException {
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        final Iterable<Contents> objects = helpers.listObjects(bucketName);
        for(final Contents contents : objects) {
            client.deleteObject(new DeleteObjectRequest(bucketName, contents.getKey()));
        }

        client.deleteBucket(new DeleteBucketRequest(bucketName));
    }

}
