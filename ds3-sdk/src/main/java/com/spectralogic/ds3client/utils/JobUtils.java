package com.spectralogic.ds3client.utils;

import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobSpectraS3Response;
import com.spectralogic.ds3client.commands.spectrads3.GetJobsSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobsSpectraS3Response;
import com.spectralogic.ds3client.models.*;

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
    public static List<UUID> findJob(final Ds3Client client, final JobRequestType type, final String bucketName, final Set<String> fileNames) throws IOException, SignatureException {
        final ImmutableSet<String> files = ImmutableSet.copyOf(fileNames);
        final GetJobsSpectraS3Response response = client.getJobsSpectraS3(new GetJobsSpectraS3Request());
        final List<UUID> jobIds = new ArrayList<>();

        for (final JobApiBean jobApiBean : response.getJobsApiBeanResult().getJobs()) {
            if (!jobApiBean.getBucketName().equals(bucketName) || jobApiBean.getStatus() != JobStatus.IN_PROGRESS || jobApiBean.getRequestType() != type) continue;
            final GetJobSpectraS3Response jobResponse = client.getJobSpectraS3(new GetJobSpectraS3Request(jobApiBean.getJobId()));
            final JobWithChunksApiBean  jobWithChunksApiBean = jobResponse.getJobWithChunksApiBeanResult();

            for (final JobChunkApiBean jobChunkApiBean : jobWithChunksApiBean.getObjects()) {
                if (chunkAndSetIntersects(jobChunkApiBean, files)){
                    jobIds.add(jobApiBean.getJobId());
                    break;  // move onto the next job
                }
            }
        }
        return jobIds;
    }

    private static boolean chunkAndSetIntersects(
            final JobChunkApiBean jobChunkApiBean,
            final ImmutableSet<String> fileNames) {
        for (final BlobApiBean blobApiBean : jobChunkApiBean.getObjects()) {
            if (fileNames.contains(blobApiBean.getName())) return true;
        }

        return false;
    }

}
