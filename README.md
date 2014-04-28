ds3_java_sdk
============

## Install

To install the latest ds3_java_sdk either download the latest release jar file from the [Releases](../../releases) page or clone the repository and run `./gradlew install` to install the sdk into your local maven repository.

## Javadoc

The latest javadoc is located at [http://spectralogic.github.io/ds3_java_sdk/](http://spectralogic.github.io/ds3_java_sdk/)

## Examples

This example lists all the buckets available to the specific user (specified by the `Credentials` object) on a remote DS3 appliance.

```java

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetServiceRequest;
import com.spectralogic.ds3client.commands.GetServiceResponse;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.models.Bucket;

import java.io.IOException;
import java.security.SignatureException;

public class Ds3ServiceListExample {

    public static void main(final String args[]) throws IOException, SignatureException {
    
        //Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        final Ds3Client client = Ds3Client.builder("ds3Endpoint:8080",
                                   new Credentials("accessKey", "secretKey")).build();

        //Tell the client to get us a list of all buckets, this is called a service list.
        final GetServiceResponse response = client.getService(new GetServiceRequest());

        //Iterate through all the buckets and print them to the console.
        for(final Bucket bucket: response.getResult().getBuckets()) {
            System.out.println(bucket.getName());
        }
    }
}

```

This next example is a little more complex and will perform a bulk get from a DS3 Appliance.  This example uses the [Apache Commons IO](http://commons.apache.org/proper/commons-io/) library.

```java

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.Objects;
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
        
        //Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        final Ds3Client client = Ds3Client.builder("ds3Endpoint:8080",
                                   new Credentials("accessKey", "secretKey")).build();

        final String bucket = "bucketName"; //The bucket we are interested in getting objects from.

        //Get the list of objects from the bucket that you want to perform the bulk get with.
        final GetBucketResponse response = client.getBucket(new GetBucketRequest(bucket));

        //We now need to generate the list of Ds3Objects that we want to get from DS3.
        final List<Ds3Object> objectList = new ArrayList<>();
        for (final Contents contents: response.getResult().getContentsList()){
            objectList.add(new Ds3Object(contents.getKey(), contents.getSize()));
        }

        //We are writing all the objects out to the directory output
        final Path dirPath = FileSystems.getDefault().getPath("output");

        //Check to make sure output exists, if not create the directory
        if(!Files.exists(dirPath)) {
            Files.createDirectory(dirPath);
        }

        //Prime DS3 with the BulkGet command so that it can start to get objects off of tape.
        final BulkGetResponse bulkResponse = client.bulkGet(new BulkGetRequest(bucket, objectList));

        //The bulk response returns a list of lists which is designed to optimize data transmission from DS3.
        final MasterObjectList list = bulkResponse.getResult();
        for(final Objects objects: list.getObjects()) {
            for(final Ds3Object obj: objects) {

                //Perform the operation to get the object from DS3.
                final GetObjectResponse getObjectResponse = client.getObject(new GetObjectRequest(bucket, obj.getName(), list.getJobid()));

                final Path filePath = dirPath.resolve(obj.getName());
                //Here we are using automatic resource cleanup to make sure the streams we use are cleaned up after use.
                try(final InputStream objStream = getObjectResponse.getContent();
                    final OutputStream fileOut = Files.newOutputStream(filePath)) {
                    IOUtils.copy(objStream, fileOut); //Using IOUtils to copy the object contents to a file.
                }

            }
        }
    }
}

```
