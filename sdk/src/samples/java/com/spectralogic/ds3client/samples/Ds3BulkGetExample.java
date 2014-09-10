package com.spectralogic.ds3client.samples;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.MasterObjectList;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

public class Ds3BulkGetExample {

    public static void main(final String args[]) throws IOException, SignatureException, XmlProcessingException {

        // Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        final Ds3Client client = Ds3ClientBuilder.create("ds3Endpoint:8080",
                                   new Credentials("accessKey", "secretKey")).withHttpSecure(false).build();

        final String bucket = "bucketName"; //The bucket we are interested in getting objects from.

        // Get the list of objects from the bucket that you want to perform the bulk get with.
        final GetBucketResponse response = client.getBucket(new GetBucketRequest(bucket));

        // We now need to generate the list of Ds3Objects that we want to get from DS3.
        final List<Ds3Object> objectList = new ArrayList<>();
        for (final Contents contents: response.getResult().getContentsList()){
            objectList.add(new Ds3Object(contents.getKey(), contents.getSize()));
        }

        // We are writing all the objects out to the directory output
        final Path dirPath = FileSystems.getDefault().getPath("output");

        // Check to make sure output exists, if not create the directory
        if(!Files.exists(dirPath)) {
            Files.createDirectory(dirPath);
        }

        // Prime DS3 with the BulkGet command so that it can start to get objects off of tape.
        // All Response objects to the SDK implement the Closeable interface and can be used in try-with-resource blocks
        try (final BulkGetResponse bulkResponse = client.bulkGet(new BulkGetRequest(bucket, objectList))) {

            // The bulk response returns a list of lists which is designed to optimize data transmission from DS3.
            final MasterObjectList list = bulkResponse.getResult();
            for (final Objects objects : list.getObjects()) {
                for (final BulkObject obj : objects) {

                    // Perform the operation to get the object from DS3.
                    try (final GetObjectResponse getObjectResponse = client.getObject(
                            new GetObjectRequest(bucket, obj.getName(), obj.getOffset(), list.getJobId()))) {

                        final Path filePath = dirPath.resolve(obj.getName());
                        // Here we are using automatic resource cleanup to make sure the streams we use are cleaned up after use.
                        try (final InputStream objStream = getObjectResponse.getContent();
                             final OutputStream fileOut = Files.newOutputStream(filePath)) {
                            IOUtils.copy(objStream, fileOut); //Using IOUtils to copy the object contents to a file.
                        }
                    }
                }
            }
        }
    }
}