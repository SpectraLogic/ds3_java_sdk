package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.DeleteBucketRequest;
import com.spectralogic.ds3client.commands.DeleteObjectRequest;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectPutter;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.SignatureException;

public class Util {
    private Util() {}

    public static Ds3Client fromEnv() {
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
        builder.withHttpSecure(false);
        if (httpProxy != null) {
            builder.withProxy(httpProxy);
        }
        return builder.build();
    }

    public static void loadBookTestData(final Ds3Client client, final String bucketName) throws IOException, SignatureException, XmlProcessingException {
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        final Path basePath = FileSystems.getDefault().getPath("src/test/resource/books");

        final Iterable<Ds3Object> objects = helpers.listObjectsForDirectory(basePath);
        final Ds3ClientHelpers.WriteJob job = helpers.startWriteJob(bucketName, objects);
        job.write(new FileObjectPutter(basePath));
    }

    public static void deleteAllContents(final Ds3Client client, final String bucketName) throws IOException, SignatureException {
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        final Iterable<Contents> objects = helpers.listObjects(bucketName);
        for(final Contents contents : objects) {
            client.deleteObject(new DeleteObjectRequest(bucketName, contents.getKey())).close();
        }

        client.deleteBucket(new DeleteBucketRequest(bucketName)).close();
    }
}
