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
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectPutter;
import com.spectralogic.ds3client.models.DataIsolationLevel;
import com.spectralogic.ds3client.models.DataPersistenceRuleType;
import com.spectralogic.ds3client.models.DataReplicationRuleType;
import com.spectralogic.ds3client.models.Ds3Target;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.common.Credentials;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SignatureException;
import java.util.*;

public class ReplicationExample {

    public static void main(final String args[]) throws IOException, SignatureException, XmlProcessingException {

        try {
            final String primaryClientURL = "sm25-6.eng.sldomain.com";
            final String primaryClientAccessKey = "c3BlY3RyYQ==";
            final String primaryClientSecretKey = "ZWnapRXW";
            final Credentials primaryCredentials = new Credentials(primaryClientAccessKey, primaryClientSecretKey);

            final String targetClientURL = "sm4u-26.eng.sldomain.com";
            final String targetClientName = "sm4u-26";
            final String targetClientAccessKey = "c3BlY3RyYQ==";
            final String targetClientSecretKey = "YRjsJjsh";

            // The bucket that we will be writing to
            final String date = new Date(System.currentTimeMillis()).toString();
            final String bucketName = "ds3_java_sdk_target_bucket" + date;
            final String dataPolicyName = "ds3ReplicationFrom" + System.getProperties().toString();


            final Ds3Client primaryClient = Ds3ClientBuilder.create(primaryClientURL,
                    new Credentials(primaryClientAccessKey, primaryClientSecretKey)).withHttps(false)
                    .withProxy(System.getenv("http_proxy")).build();

            //final Ds3Client primaryClient = Ds3ClientBuilder.fromEnv().build();
            final Ds3Client targetClient = Ds3ClientBuilder.create(targetClientURL,
                    new Credentials(targetClientAccessKey, targetClientSecretKey)).withHttps(false)
                    .withProxy(System.getenv("http_proxy")).build();

            // Wrap the Ds3Client with the helper functions
            final Ds3ClientHelpers primaryHelper = Ds3ClientHelpers.wrap(primaryClient);

            // Register and Verify DS3 Target
            System.out.println("Registering DS3 Target");
            final GetDs3TargetsSpectraS3Response getDs3TargetsSpectraS3Response = primaryClient
                    .getDs3TargetsSpectraS3(new GetDs3TargetsSpectraS3Request());

            String ds3TargetID = null;
            for (final Ds3Target ds3Target : getDs3TargetsSpectraS3Response.getDs3TargetListResult().getDs3Targets()){
                if (Objects.equals(ds3Target.getName(), targetClientName)){
                    ds3TargetID = ds3Target.getId().toString();
                }
            }
            if (ds3TargetID == null) {
                final RegisterDs3TargetSpectraS3Response registerDs3TargetSpectraS3Response = primaryClient
                        .registerDs3TargetSpectraS3(new RegisterDs3TargetSpectraS3Request(
                                targetClientAccessKey, targetClientSecretKey, targetClientURL, targetClientName)
                                .withDataPathHttps(false));
                ds3TargetID = registerDs3TargetSpectraS3Response.getDs3TargetResult().getId().toString();
                System.out.println("Registered with code " + registerDs3TargetSpectraS3Response.getStatusCode());
            }
            else {
                System.out.println("Found ds3 target " + targetClientName + " " + ds3TargetID +
                        " already registered");
            }

            final VerifyDs3TargetSpectraS3Response verifyDs3TargetSpectraS3Response = primaryClient
                    .verifyDs3TargetSpectraS3(new VerifyDs3TargetSpectraS3Request(targetClientName));  //using name
            System.out.println("Verified target with name response code" +
                    verifyDs3TargetSpectraS3Response.getStatusCode());
            final VerifyDs3TargetSpectraS3Response verifyDs3TargetSpectraS3ResponseUUID = primaryClient
                    .verifyDs3TargetSpectraS3(new VerifyDs3TargetSpectraS3Request(ds3TargetID));
            System.out.println("Verified target with UUID response code" +
                    verifyDs3TargetSpectraS3ResponseUUID.getStatusCode());

            // Create data policy on primary client with Data Replication Rule
            System.out.println("Creating data policy and replication rule.");
            primaryClient.putDataPolicySpectraS3(new PutDataPolicySpectraS3Request(dataPolicyName));

            final GetStorageDomainsSpectraS3Response getStorageDomainsSpectraS3Response = primaryClient
                    .getStorageDomainsSpectraS3(new GetStorageDomainsSpectraS3Request());

            final String storageDomainID = getStorageDomainsSpectraS3Response.getStorageDomainListResult()
                    .getStorageDomains().get(0).getId().toString();

            primaryClient.putDataPersistenceRuleSpectraS3((new
                    PutDataPersistenceRuleSpectraS3Request(dataPolicyName, DataIsolationLevel.STANDARD,
                    storageDomainID, DataPersistenceRuleType.PERMANENT)));

            final PutDataReplicationRuleSpectraS3Response putDataReplicationRuleSpectraS3Response = primaryClient.putDataReplicationRuleSpectraS3(new PutDataReplicationRuleSpectraS3Request(
                    dataPolicyName, ds3TargetID, DataReplicationRuleType.PERMANENT));

            System.out.println("Put replication rule response code " +
                    putDataReplicationRuleSpectraS3Response.getStatusCode());


            //Create bucket with data policy
            primaryClient.putBucketSpectraS3(new PutBucketSpectraS3Request(bucketName).withDataPolicyId(dataPolicyName));

            // Our local path which contains all the files that we want to transfer
            final Path inputPath = Paths.get("input");

            // Get the list of files that are contained in the inputPath
            final Iterable<Ds3Object> objects = primaryHelper.listObjectsForDirectory(inputPath);

            // Create the write job with the bucket we want to write to and the list
            // of objects that will be written
            final Ds3ClientHelpers.Job job = primaryHelper.startWriteJob(bucketName, objects);
            System.out.println("Started write job " + job.getJobId() +
                    "for bucket " + job.getBucketName());
            // Start the write job using an Object Putter that will read the files
            // from the local file system and wait for the job to complete.
            job.transfer(new FileObjectPutter(inputPath));

            //Poll every 30 seconds to see if job is complete
            GetJobSpectraS3Response getJobSpectraS3Response;
            do {
                getJobSpectraS3Response = primaryClient.getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId()));
                Thread.sleep(30*1000);
                System.out.println("Waiting for job to complete");
            } while (getJobSpectraS3Response.getMasterObjectListResult().getStatus().toString().equals("IN_PROGRESS"));

            final ArrayList<Ds3Object> objectsList = new ArrayList<>();
            for (final Ds3Object object : objects){
                objectsList.add(object);
            }
            System.out.println("Getting physical placement from primary");
            final GetPhysicalPlacementForObjectsSpectraS3Response primaryPhysicalPlacementForObjectsSpectraS3Response =
                    primaryClient.getPhysicalPlacementForObjectsSpectraS3(
                    new GetPhysicalPlacementForObjectsSpectraS3Request(bucketName, objectsList));
            System.out.println("Primary physical placement: " + primaryPhysicalPlacementForObjectsSpectraS3Response
                    .getPhysicalPlacementResult().toString());
            final GetPhysicalPlacementForObjectsSpectraS3Response targetGetPhysicalPlacementForObjectsSpectraS3Response =
                    targetClient.getPhysicalPlacementForObjectsSpectraS3(
                            new GetPhysicalPlacementForObjectsSpectraS3Request(bucketName, objectsList));
            System.out.println("Target physical placement: " + primaryPhysicalPlacementForObjectsSpectraS3Response
                    .getPhysicalPlacementResult().toString());

        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
