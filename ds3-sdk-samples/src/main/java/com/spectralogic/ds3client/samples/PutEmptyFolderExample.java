/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.PutObjectResponse;
import com.spectralogic.ds3client.commands.spectrads3.GetBucketSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetBucketSpectraS3Response;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.FailedRequestUsingMgmtPortException;
import com.spectralogic.ds3client.utils.EmptySeekableByteChannel;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.SignatureException;

/**
 * Put a zero-byte object with a trailing slash to serve as an empty folder.
 * Use the Standard Amazon S3 Put Object (does not require a Job)
 *
 * NOTE: This PutObjectRequest construstor is marked deprecated
 * This is to drive development toward Spectra S3 calls; support for this will not be removed
 */
public class PutEmptyFolderExample {

    public static void main(final String args[]) throws IOException, SignatureException {

        if (args.length < 2) {
            System.out.println("Usage: bucketname foldername");
            return;
        }
        final String bucketname = args[0];
        String foldername = args[1];

        // an object name with trailing / is treated as a folder
        if (!foldername.endsWith("/")) {
            foldername += "/";
        }

        // Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        try (final Ds3Client client = Ds3ClientBuilder.fromEnv().withHttps(false).build()) {

            // Put using EmptySeekableByteChannel, zero-length
            final PutObjectResponse putObjectResponse = client.putObject(new PutObjectRequest(bucketname, foldername, new EmptySeekableByteChannel(), 0L));

            // New folder can be accessed by Standard S3 or Spectra calls
            final Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(client);
            final Iterable<Contents> objects = helper.listObjects(bucketname, foldername);
            for (final Contents object : objects) {
                System.out.println("Created folder: " + object.getKey() );
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
                System.out.println("ERROR: Invalid Access ID or Secret Key");

            } else if (e.getStatusCode() == 409) {
                System.out.println("ERROR: Object Exists");

                // Else unexpected status code.
            } else {
                System.out.println("BlackPearl returned an unexpected status code");
            }

        } catch (final IOException e) {

            System.out.println("Encountered a networking error");

        }
    }
}
