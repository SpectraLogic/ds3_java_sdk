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
