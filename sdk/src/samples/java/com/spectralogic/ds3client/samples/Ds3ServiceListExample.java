package com.spectralogic.ds3client.samples;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.GetServiceRequest;
import com.spectralogic.ds3client.commands.GetServiceResponse;
import com.spectralogic.ds3client.models.Bucket;
import com.spectralogic.ds3client.models.Credentials;

import java.io.IOException;
import java.security.SignatureException;

public class Ds3ServiceListExample {

    public static void main(final String args[]) throws IOException, SignatureException {

        // Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        final Ds3Client client = Ds3ClientBuilder.create("ds3Endpoint:8080",
                new Credentials("accessKey", "secretKey")).withHttps(false).build();

        // Tell the client to get us a list of all buckets, this is called a service list.
        final GetServiceResponse response = client.getService(new GetServiceRequest());

        // Iterate through all the buckets and print them to the console.
        for (final Bucket bucket : response.getResult().getBuckets()) {
            System.out.println(bucket.getName());
        }
    }
}
