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
import com.spectralogic.ds3client.helpers.ChecksumListener;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectPutter;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.models.Checksum;
import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SignatureException;

public class BulkPutWithChecksums {
    public static void main(final String[] args) throws IOException, SignatureException, XmlProcessingException {
        try (final Ds3Client client = Ds3ClientBuilder.fromEnv().withHttps(false).build()) {

            // Wrap the Ds3Client with the helper functions
            final Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(client);

            // The bucket that we will be writing to
            final String bucketName = "my_bucket";

            // Make sure that the bucket exists, if it does not this will create it
            helper.ensureBucketExists(bucketName);

            // Our local path which contains all the files that we want to transfer
            final Path inputPath = Paths.get("input");

            // Get the list of files that are contained in the inputPath
            final Iterable<Ds3Object> objects = helper.listObjectsForDirectory(inputPath);

            // To enable checksumming we need to tell the helper functions to calculate it with
            // the WriteJobOptions object.  NOTE: when enabling checksumming calculations in the
            // helper functions, the helper functions must process the file twice.  Once
            // to calculate the checksum, and then once to send the object to the
            // Spectra S3 endpoint.
            final WriteJobOptions options = WriteJobOptions.create().withChecksumType(Checksum.Type.MD5);

            // Create the write job with the bucket we want to write to and the list
            // of objects that will be written
            final Ds3ClientHelpers.Job job = helper.startWriteJob(bucketName, objects, options);

            // To capture the checksums as they are calculated for your own records, use the
            // ChecksumListener.  This same listener can be used on read jobs to capture the
            // blob checksums
            job.attachChecksumListener(new ChecksumListener() {
                @Override
                public void value(final BulkObject obj, final Checksum.Type type, final String checksum) {
                    // The checksum for each blob(BulkObject) is reported
                    System.out.println("The checksum for blob " + obj.toString() + " is " + checksum);
                }
            });

            // Start the write job using an Object Putter that will read the files
            // from the local file system.
            job.transfer(new FileObjectPutter(inputPath));
        }
    }
}
