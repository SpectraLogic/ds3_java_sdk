ds3_java_sdk
============

## Install

To install the latest ds3_java_sdk either download the latest release jar file from the [Releases](../../releases) page or clone the repository with `git clone https://github.com/SpectraLogic/ds3_java_sdk.git`, cd to `ds3_java_sdk` and run `./gradlew clean sdk:install` to install the sdk into your local maven repository.

## Tests

In addition to unit tests in the main `sdk` module, there are additional integration tests in the `integration` module.  Please see the integration [README](integration/README.md) for details on running the tests.  To just run the SDK's unit tests use:

    ./gradlew clean sdk:test

## Javadoc

The latest javadoc is located at [http://spectralogic.github.io/ds3_java_sdk/](http://spectralogic.github.io/ds3_java_sdk/)

## Examples

This example lists all the buckets available to the specific user (specified by the `Credentials` object) on a remote DS3 appliance.

```java

package com.spectralogic.ds3client.samples;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.GetServiceRequest;
import com.spectralogic.ds3client.commands.GetServiceResponse;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.models.Bucket;

import java.io.IOException;
import java.security.SignatureException;

public class Ds3ServiceListExample {

    public static void main(final String args[]) throws IOException, SignatureException {

        // Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        final Ds3Client client = Ds3ClientBuilder.create("ds3Endpoint:8080",
                new Credentials("accessKey", "secretKey")).withHttpSecure(false).build();

        // Tell the client to get us a list of all buckets, this is called a service list.
        try (final GetServiceResponse response = client.getService(new GetServiceRequest())) {

            // Iterate through all the buckets and print them to the console.
            for (final Bucket bucket : response.getResult().getBuckets()) {
                System.out.println(bucket.getName());
            }
        }
    }
}

```

The SDK contains many helper functions that can be used to ease initial development.  There are some cases where the helper functions cannot be used, but they are a great starting point when first learning to write applications targeting a DS3 Appliance.

This example will explore a user specified diretory on the local filesystem and then send each file to be stored on a remote DS3 Appliance.

```java

package com.spectralogic.ds3client.samples;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectPutter;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.SignatureException;

public class BulkPutExample {

    public static void main(final String args[]) throws IOException, SignatureException, XmlProcessingException {
        final Ds3Client client = Ds3ClientBuilder.create("endpoint:8080",
                new Credentials("accessId", "secretKey"))
                .withHttpSecure(false)
                .build();

        // Wrap the Ds3Client with the helper functions
        final Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(client);

        // The bucket that we will be writing to
        final String bucketName = "my_bucket";

        // Make sure that the bucket exists, if it does not this will create it
        helper.ensureBucketExists(bucketName);

        // Our input path which contains all the files that we want to transfer
        final Path inputPath = FileSystems.getDefault().getPath("input");

        // Get the list of files that are contained in the inputPath
        final Iterable<Ds3Object> objects = helper.listObjectsForDirectory(inputPath);

        // Create the write job with the bucket we want to write to and the list
        // of objects that will be written
        final Ds3ClientHelpers.WriteJob job = helper.startWriteJob(bucketName, objects);

        // Start the write job using an Object Putter that will read the files
        // from the local file system.
        job.write(new FileObjectPutter(inputPath));
    }
}

```

This next example is a little more complex and will perform a bulk get from a DS3 Appliance using the commands that directly correspond to the REST commands that are actually made against the DS3 Appliance.  This example is meant to demonstrate the flexibilty of the SDK when writing applications where the helper functions cannot be used.  The [Apache Commons IO](http://commons.apache.org/proper/commons-io/) library is used in this example for dealing with IO Streams.

```java

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

```
