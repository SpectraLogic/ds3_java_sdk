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
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.PrefixedFileObjectPutter;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.MasterObjectList;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

public class Ds3PutObjectRelativePathExample {

    public static void main(final String args[]) throws IOException, SignatureException, XmlProcessingException {
        // Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        try (final Ds3Client client = Ds3ClientBuilder.fromEnv().withHttps(false).build()) {
            /*************************************************************************************************
            Let's say I want to move directory DirB, which is located at ./DirA/DirB on my Windows machine,
            to BlackPearl. I want to put DirB into BucketA/Dir1/Dir2, so that the final path will be
            BucketA/Dir1/Dir2/DirB. When I move DirB to BlackPearl, I don't want DirA to move as well.

            Prerequisites:
              DirA/DirB/* must exist in the working directory
            *************************************************************************************************/
            System.out.println("Scenario 1: Move files from a subdirectory and leave off some of the local path");
            final String rootDirectory = "DirA/";
            final String targetDirectory = "DirB/";
            final String bucketName = "BucketA";
            final String remoteFolder = "Dir1/Dir2/";
            System.out.println("Root Directory[" + rootDirectory + "]");
            System.out.println("Target Directory[" + targetDirectory + "]");
            System.out.println("Bucket[" + bucketName + "]");
            System.out.println("RemoteFolderPath[" + remoteFolder  + "]");

            final Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(client);

            // Find the desired objects to PUT
            final Iterable<Ds3Object> putObjectList = helper.listObjectsForDirectory(Paths.get(rootDirectory));
            System.out.println("objectList" + putObjectList.toString());

            // Create a list of all objects in DirB, relative to DirA
            final Iterable<Ds3Object> remotePathObjectList = helper.addPrefixToDs3ObjectsList(putObjectList, remoteFolder);
            System.out.println("remotePathObjectList" + remotePathObjectList.toString());

            // Make sure that the bucket exists, if it does not this will create it
            helper.ensureBucketExists(bucketName);

            // Create the write job with the bucket we want to write to and the list
            // of objects that will be written
            final Ds3ClientHelpers.Job job = helper.startWriteJob(bucketName, remotePathObjectList);

            // Start the write job using a PrefixedObjectPutter that will strip off the remote path before
            // reading the files from the local file system.
            job.transfer(new PrefixedFileObjectPutter(rootDirectory, remoteFolder));


            /*************************************************************************************************
            Let's say I want to move directory DirB, located at BucketA/Dir1/Dir2/DirB on the BlackPearl, to
            my Windows machine. I want to be able to put DirB (and only DirB) into ./DirC, so that the final
            path will be ./DirC/DirB. I don't want Dir1 or Dir2 to move as well.
            *************************************************************************************************/
            System.out.println("\n");
            System.out.println("Scenario 2: Get files from a remote bucket and leave off some of the remote path");
            final String localDir = "DirC/";

            // Get the list of objects from the bucket that you want to perform the bulk get with.
            System.out.println("GET BucketA");
            final GetBucketResponse response = client.getBucket(new GetBucketRequest(bucketName));

            // We now need to generate the list of Ds3Objects that we want to get from DS3.
            final List<Ds3Object> objectList = new ArrayList<>();
            for (final Contents contents : response.getResult().getContentsList()) {
                System.out.println("  content[" + contents.getKey() + "]");
                objectList.add(new Ds3Object(contents.getKey(), contents.getSize()));
            }

            // Prime DS3 with the BulkGet command so that it can start to get objects off of tape.
            final BulkGetResponse bulkResponse = client.bulkGet(new BulkGetRequest(bucketName, objectList));

            // The bulk response returns a list of lists which is designed to optimize data transmission from DS3.
            final MasterObjectList list = bulkResponse.getResult();
            for (final Objects objects : list.getObjects()) {
                for (final BulkObject obj : objects) {
                    System.out.println("getObjectName[" + obj.getName() + "]");

                    final String localName = helper.stripLeadingPath(obj.getName(), remoteFolder);
                    System.out.println("localName[" + localName + "]");

                    final Path localFilePath = Paths.get(localDir + localName);
                    System.out.println("localFilePath[" + localFilePath + "]");

                    Files.createDirectories(localFilePath.getParent());

                    final FileChannel channel = FileChannel.open(
                            localFilePath,
                            StandardOpenOption.WRITE,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING
                    );

                    // Perform the operation to get the object from DS3.
                    client.getObject(new GetObjectRequest(
                            bucketName,
                            obj.getName(),
                            obj.getOffset(),
                            list.getJobId(),
                            channel
                    ));
                }
            }
            /*************************************************************************************************
            Additionally, it would be very useful to be able to rename the directory when moved from client to
            BlackPearl and visa versa. The use case I ran into for this was for downloading video from a USB
            police body camera. The idea would be that every day the police officer would plug the camera
            into the computer to download the video files to BlackPearl. Unfortunately every time the camera
            is plugged in, the video files would always be in the same directory (e.g. e:\DCIM). We need to
            be able to give this directory a new name every time it is moved to BlackPearl. So while the
            local directory might e:\DCIM, when moved to BlackPearl we might
            want it to be BucketA\policeBodyCamera\DCIM-20150326-OfficerBob\

            Prerequisites:
              DirC/* must exist in the working directory
            *************************************************************************************************/
            System.out.println("\n");
            System.out.println("Scenario 3: Move files from a subdirectory and change the remote path");

            final String rootDirectory3 = "DirC/DirB/";
            final String remoteFolder3 = "policeBodyCamera/2015-10-12_ChiefWiggum/";

            // Find the desired objects to PUT
            final Iterable<Ds3Object> putObjectList3 = helper.listObjectsForDirectory(Paths.get(rootDirectory3));
            System.out.println("objectList" + putObjectList3.toString());

            helper.removePrefixFromDs3ObjectsList(putObjectList3,rootDirectory3);
            System.out.println("shortenedObjectList" + putObjectList3.toString());

            // Create a list of all objects in DirB, relative to DirA
            final Iterable<Ds3Object> remotePathObjectList3 = helper.addPrefixToDs3ObjectsList(putObjectList3, remoteFolder3);
            System.out.println("remotePathObjectList" + remotePathObjectList3.toString());

            // Make sure that the bucket exists, if it does not this will create it
            helper.ensureBucketExists(bucketName);

            // Create the write job with the bucket we want to write to and the list
            // of objects that will be written
            final Ds3ClientHelpers.Job job3 = helper.startWriteJob(bucketName, remotePathObjectList3);

            // Start the write job using a PrefixedObjectPutter that will strip off the remote path before
            // reading the files from the local file system.
            job3.transfer(new PrefixedFileObjectPutter(rootDirectory3, remoteFolder3));
        } // End try{ Ds3ClientBuilder
    }
}
