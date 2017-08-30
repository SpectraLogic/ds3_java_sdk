/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.utils;

import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobSpectraS3Response;
import com.spectralogic.ds3client.commands.spectrads3.GetJobsSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobsSpectraS3Response;
import com.spectralogic.ds3client.models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class JobUtils {

    private JobUtils() {}

    /**
     * Finds all the jobs that have any of the files in {@param fileNames} contained in them.  When {@param type} is PUT
     * only one job ID should be returned.  It is possible that when {@param type} is GET that there could be multiple
     * job IDs returned.
     */
    public static List<UUID> findJob(final Ds3Client client, final JobRequestType type, final String bucketName, final Set<String> fileNames) throws IOException {
        final ImmutableSet<String> files = ImmutableSet.copyOf(fileNames);
        final GetJobsSpectraS3Response response = client.getJobsSpectraS3(new GetJobsSpectraS3Request());
        final List<UUID> jobIds = new ArrayList<>();

        for (final Job job : response.getJobListResult().getJobs()) {
            if (!job.getBucketName().equals(bucketName) || job.getStatus() != JobStatus.IN_PROGRESS || job.getRequestType() != type) continue;
            final GetJobSpectraS3Response jobResponse = client.getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId().toString()));
            final MasterObjectList masterObjectList = jobResponse.getMasterObjectListResult();

            for (final Objects objects : masterObjectList.getObjects()) {
                if (chunkAndSetIntersects(objects, files)){
                    jobIds.add(job.getJobId());
                    break;  // move onto the next job
                }
            }
        }
        return jobIds;
    }

    private static boolean chunkAndSetIntersects(
            final Objects objects,
            final ImmutableSet<String> fileNames) {
        for (final BulkObject blobApiBean : objects.getObjects()) {
            if (fileNames.contains(blobApiBean.getName())) return true;
        }

        return false;
    }

}
