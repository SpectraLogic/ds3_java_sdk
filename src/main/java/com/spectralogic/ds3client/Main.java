package com.spectralogic.ds3client;

import com.spectralogic.ds3client.commands.GetServiceRequest;
import com.spectralogic.ds3client.commands.GetServiceResponse;
import com.spectralogic.ds3client.models.Bucket;
import com.spectralogic.ds3client.models.Credentials;

import java.io.IOException;
import java.security.SignatureException;

class Main {
    public static void main(String[] args) throws IOException, SignatureException {
        final Ds3Client client = Ds3Client.builder("192.168.56.101:8080",
                                                  new Credentials("cnlhbg==", "3pxVeear")).withHttpSecure(false).build();

             final GetServiceResponse response = client.getService(new GetServiceRequest());

             for(final Bucket bucket: response.getResult().getBuckets()) {
                     System.out.println(bucket.getName());
             }
    }
}
