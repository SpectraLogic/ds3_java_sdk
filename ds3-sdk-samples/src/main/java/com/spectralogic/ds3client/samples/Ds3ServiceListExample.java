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

package com.spectralogic.ds3client.samples;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.GetServiceRequest;
import com.spectralogic.ds3client.commands.GetServiceResponse;
import com.spectralogic.ds3client.models.Ds3Bucket;
import com.spectralogic.ds3client.models.Credentials;

import java.io.IOException;
import java.security.SignatureException;

public class Ds3ServiceListExample {

    public static void main(final String args[]) throws IOException, SignatureException {

        // Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        try (final Ds3Client client = Ds3ClientBuilder.create("ds3Endpoint:8080",
                new Credentials("accessKey", "secretKey")).withHttps(false).build()) {

            // Tell the client to get us a list of all buckets, this is called a service list.
            final GetServiceResponse response = client.getService(new GetServiceRequest());

            // Iterate through all the buckets and print them to the console.
            for (final Ds3Bucket bucket : response.getListAllMyBucketsResult().getBuckets()) {
                System.out.println(bucket.getName());
            }
        }
    }
}
