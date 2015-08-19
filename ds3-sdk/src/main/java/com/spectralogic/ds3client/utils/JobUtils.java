package com.spectralogic.ds3client.utils;

import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetJobRequest;
import com.spectralogic.ds3client.commands.GetJobResponse;
import com.spectralogic.ds3client.commands.GetJobsRequest;
import com.spectralogic.ds3client.commands.GetJobsResponse;
import com.spectralogic.ds3client.models.bulk.*;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class JobUtils {

    private JobUtils() {}

    /**
     * Finds all the jobs that have any of the files in {@param fileNames} contained in them.  When {@param type} is PUT
     * only one job ID should be returned.  It is possible that when {@param type} is GET that there could be multiple
     * job IDs returned.
     */
    public static List<UUID> findJob(final Ds3Client client, final RequestType type, final String bucketName, final Set<String> fileNames) throws IOException, SignatureException {
        final ImmutableSet<String> files = ImmutableSet.copyOf(fileNames);
        final GetJobsResponse response = client.getJobs(new GetJobsRequest());
        final List<UUID> jobs = new ArrayList<>();

        for (final JobInfo jobInfo : response.getJobs()) {
            if (!jobInfo.getBucketName().equals(bucketName) || jobInfo.getStatus() != JobStatus.IN_PROGRESS || jobInfo.getRequestType() != type) continue;
            final GetJobResponse jobResponse = client.getJob(new GetJobRequest(jobInfo.getJobId()));
            final MasterObjectList mol = jobResponse.getMasterObjectList();

            for (final Objects chunk : mol.getObjects()) {
                if (chunkAndSetIntersects(chunk, files)){
                    jobs.add(jobInfo.getJobId());
                    break;  // move onto the next job
                }
            }
        }
        return jobs;
    }

    private static boolean chunkAndSetIntersects(final Objects chunk, final ImmutableSet<String> fileNames) {
        for (final BulkObject bulkObject : chunk.getObjects()) {
            if (fileNames.contains(bulkObject.getName())) return true;
        }

        return false;
    }

}
