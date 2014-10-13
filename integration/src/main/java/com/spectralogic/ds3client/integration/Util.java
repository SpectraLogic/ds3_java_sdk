package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.DeleteBucketRequest;
import com.spectralogic.ds3client.commands.DeleteObjectRequest;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.Credentials;
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
    private Util() {}

    public static Ds3Client fromEnv() {
        final Ds3ClientBuilder builder = clientBuilder();
        builder.withHttps(false);
        return builder.build();
    }

    public static Ds3Client insecureFromEnv() {
        final Ds3ClientBuilder builder = clientBuilder();
        builder.withCertificateVerification(false);
        return builder.build();
    }

    private static Ds3ClientBuilder clientBuilder() {
        final String endpoint = System.getenv("DS3_ENDPOINT");
        final String accessKey = System.getenv("DS3_ACCESS_KEY");
        final String secretKey = System.getenv("DS3_SECRET_KEY");
        final String httpProxy = System.getenv("http_proxy");

        if (endpoint == null) {
            throw new IllegalArgumentException("Missing DS3_ENDPOINT");
        }

        if (accessKey == null) {
            throw new IllegalArgumentException("Missing DS3_ACCESS_KEY");
        }

        if (secretKey == null) {
            throw new IllegalArgumentException("Missing DS3_SECRET_KEY");
        }

        final Ds3ClientBuilder builder = Ds3ClientBuilder.create(endpoint,new Credentials(accessKey, secretKey));
        if (httpProxy != null) {
            builder.withProxy(httpProxy);
        }
        return builder;
    }

    private static final String[] BOOKS = {"beowulf.txt", "sherlock_holmes.txt", "tale_of_two_cities.txt", "ulysses.txt"};
    public static void loadBookTestData(final Ds3Client client, final String bucketName) throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String resourceBaseName = "books/";
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        final List<Ds3Object> objects = new ArrayList<>();

        for(final String book : BOOKS) {
            final File objFile = ResourceUtils.loadFileResource(resourceBaseName + book);
            final Ds3Object obj = new Ds3Object(book, objFile.length());

            objects.add(obj);
        }

        helpers
            .startWriteJob(bucketName, objects)
            .transfer(new ResourceObjectPutter(resourceBaseName));
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
