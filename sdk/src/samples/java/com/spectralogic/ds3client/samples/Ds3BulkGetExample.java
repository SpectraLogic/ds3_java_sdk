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

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
        final BulkGetResponse bulkResponse = client.bulkGet(new BulkGetRequest(bucket, objectList));

        // The bulk response returns a list of lists which is designed to optimize data transmission from DS3.
        final MasterObjectList list = bulkResponse.getResult();
        for (final Objects objects : list.getObjects()) {
            for (final BulkObject obj : objects) {
                final FileChannel channel = FileChannel.open(
                    dirPath.resolve(obj.getName()),
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                );

                // Perform the operation to get the object from DS3.
                client.getObject(new GetObjectRequest(
                    bucket,
                    obj.getName(),
                    obj.getOffset(),
                    list.getJobId(),
                    channel
                ));
            }
        }
    }
}