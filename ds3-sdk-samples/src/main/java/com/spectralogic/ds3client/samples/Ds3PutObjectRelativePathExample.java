/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.samples;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.helpers.*;
import com.spectralogic.ds3client.helpers.channelbuilders.PrefixAdderObjectChannelBuilder;
import com.spectralogic.ds3client.helpers.channelbuilders.PrefixRemoverObjectChannelBuilder;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.bulk.Ds3Object;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Ds3PutObjectRelativePathExample {

    public static void main(final String args[]) throws IOException {
        // Get a client builder and then build a client instance.  This is the main entry point to the SDK.
        try (final Ds3Client client = Ds3ClientBuilder.fromEnv().withHttps(false).build()) {
            /*************************************************************************************************
            Let's say I want to move directory DirB, which is located at ./DirA/DirB on my Windows machine,
            to BlackPearl. I want to put DirB into BucketA/Dir1/Dir2, so that the final path will be
            BucketA/Dir1/Dir2/DirB. When I move DirB to BlackPearl, I don't want DirA to move as well.

            Prerequisites:
              DirA/DirB/* must exist in the working directory
            *************************************************************************************************/
            final String rootDirectory = "DirA/";
            final String bucketName = "BucketA";
            final String remoteFolder = "Dir1/Dir2/";

            final Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(client);

            // Make sure that the bucket exists, if it does not this will create it
            helper.ensureBucketExists(bucketName);

            // Find the desired objects to PUT
            final Iterable<Ds3Object> putObjectList = helper.listObjectsForDirectory(Paths.get(rootDirectory));

            // Add the desired destination prefix to all objects
            final Iterable<Ds3Object> remotePathObjectList = helper.addPrefixToDs3ObjectsList(putObjectList, remoteFolder);

            // Create the write job with the bucket we want to write to and the list
            // of objects that will be written
            final Ds3ClientHelpers.Job job = helper.startWriteJob(bucketName, remotePathObjectList);

            // Start the write job using a PrefixedObjectPutter that will strip off the remote path before
            // reading the files from the local file system.
            job.transfer(new ObjectChannelBuilderLogger(new PrefixAdderObjectChannelBuilder(new FileObjectPutter(Paths.get(rootDirectory)), remoteFolder)));


            /*************************************************************************************************
            Let's say I want to move directory DirB, located at BucketA/Dir1/Dir2/DirB on the BlackPearl, to
            my Windows machine. I want to be able to put DirB (and only DirB) into ./DirC, so that the final
            path will be ./DirC/DirB. I don't want Dir1 or Dir2 to move as well.
            *************************************************************************************************/
            final String localDir = "DirC/";

            // Get the list of objects from the bucket that you want to perform the bulk get with.
            final GetBucketResponse response = client.getBucket(new GetBucketRequest(bucketName));

            // We now need to generate the list of Ds3Objects that we want to get from DS3.
            final List<Ds3Object> objectList = new ArrayList<>();
            for (final Contents contents : response.getListBucketResult().getObjects()) {
                objectList.add(new Ds3Object(contents.getKey(), contents.getSize()));
            }

            // Create the read job with the bucket we want to read from and the list
            // of objects that will be received.
            final Ds3ClientHelpers.Job getJob = helper.startReadJob(bucketName, objectList);

            // Start the read job using a PrefixedObjectGetter that will strip off the remote path before
            // reading the files from the local file system.
            getJob.transfer(new PrefixRemoverObjectChannelBuilder(new FileObjectGetter(Paths.get(localDir)), remoteFolder));


            /*************************************************************************************************
            Additionally, it would be very useful to be able to rename the directory when moved from client to
            BlackPearl and visa versa. The use case I ran into for this was for downloading video from a USB
            police body camera. The idea would be that every day the police officer would plug the camera
            into the computer to download the video files to BlackPearl. Unfortunately every time the camera
            is plugged in, the video files would always be in the same directory (e.g. ./DirC/DirB). We need to
            be able to give this directory a new name every time it is moved to BlackPearl. So while the
            local directory might ./DirC/DirB, when moved to BlackPearl we might
            want it to be BucketA/policeBodyCamera/DCIM-20150326-ChiefWiggum/

            Prerequisites:
              DirC/* must exist in the working directory
            *************************************************************************************************/
            final String rootDirectory3 = "DirC/DirB/";
            final String remoteFolder3 = "policeBodyCamera/2015-10-12_ChiefWiggum/";

            // Make sure that the bucket exists, if it does not this will create it
            helper.ensureBucketExists("BucketA");

            // Find the desired objects to PUT
            final Iterable<Ds3Object> putObjectList3 = helper.listObjectsForDirectory(Paths.get(rootDirectory3));

            // Remove the local path from all objects to be PUT
            helper.removePrefixFromDs3ObjectsList(putObjectList3, rootDirectory3);

            // Add the desired remote path
            final Iterable<Ds3Object> remotePathObjectList3 = helper.addPrefixToDs3ObjectsList(putObjectList3, remoteFolder3);

            // Create the write job with the bucket we want to write to and the list
            // of objects that will be written
            final Ds3ClientHelpers.Job job3 = helper.startWriteJob("BucketA", remotePathObjectList3);

            // Start the write job using a PrefixedObjectPutter that will strip off the remote path before
            // reading the files from the local file system.
            job3.transfer(new ObjectChannelBuilderLogger(new PrefixAdderObjectChannelBuilder(new FileObjectPutter(Paths.get(rootDirectory3)), remoteFolder3)));
        }
    }
}
