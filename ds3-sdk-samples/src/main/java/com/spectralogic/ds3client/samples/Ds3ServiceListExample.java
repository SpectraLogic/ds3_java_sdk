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
import com.spectralogic.ds3client.commands.spectrads3.GetSystemInformationSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetSystemInformationSpectraS3Response;
import com.spectralogic.ds3client.models.BucketDetails;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.FailedRequestUsingMgmtPortException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.SignatureException;
import java.net.UnknownHostException;

public class Ds3ServiceListExample {

    public static void main(final String args[]) throws IOException, SignatureException {

        // Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        try (final Ds3Client client = Ds3ClientBuilder.fromEnv().withHttps(false).build()) {

            // system info -- check connection
            final GetSystemInformationSpectraS3Response sysreponse = client.getSystemInformationSpectraS3(new GetSystemInformationSpectraS3Request());
            System.out.println(sysreponse.getSystemInformationResult().getApiVersion());

            // Tell the client to get us a list of all buckets, this is called a service list.
            final GetServiceResponse response = client.getService(new GetServiceRequest());

            // Iterate through all the buckets and print them to the console.
            for (final BucketDetails bucket : response.getListAllMyBucketsResult().getBuckets()) {
                System.out.println(bucket.getName());
            }
            // Catch unknown host exceptions.
        } catch (final UnknownHostException e) {

            System.out.println("Invalid Endpoint Server Name or IP Address");

            // Catch unknown host exceptions.
        } catch (final FailedRequestUsingMgmtPortException e) {

            System.out.println("Attempted data access on management port -- check endpoint");

            // Catch failed requests with unexpected status codes.
        } catch (final FailedRequestException e) {

        	// If this is invalid authorization.
        	if (e.getStatusCode() == 403) {

        		System.out.println("Invalid Access ID or Secret Key");

        	// Else unexpected status code.
        	} else {

        		System.out.println("BlackPearl return an unexpected status code we did not expect");
                // e.getStatusCode() can be used to get the status code BlackPearl returned for more accurate error handling and detection

        	}

        } catch (final IOException e) {

                System.out.println("Encountered a networking error");

        }
    }
}
