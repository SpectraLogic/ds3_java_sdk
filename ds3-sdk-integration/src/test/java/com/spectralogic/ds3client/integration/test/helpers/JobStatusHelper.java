package com.spectralogic.ds3client.integration.test.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetJobSpectraS3Request;
import com.spectralogic.ds3client.models.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * This class provides utilities for checking job status
 */
public class JobStatusHelper {
    final private static Logger LOG = LoggerFactory.getLogger(JobStatusHelper.class);

    private static final int MAX_RETRIES = 10;
    private static final int POLLING_PERIOD_MILLIS = 10;

    private static JobStatus actualStatus;

    public static JobStatus getJobStatusWithRetries(final Ds3Client client,
                                       final UUID jobId,
                                       final JobStatus expectedStatus) throws IOException, InterruptedException {
        final GetJobSpectraS3Request getJobSpectraS3Request = new GetJobSpectraS3Request(jobId);
        for (int retry = 0; retry < MAX_RETRIES; retry++) {
            actualStatus = client.getJobSpectraS3(getJobSpectraS3Request).getMasterObjectListResult().getStatus();
            if (actualStatus.equals(expectedStatus)) {
                LOG.info("Found expected JobStatus " + expectedStatus + " after " + retry*POLLING_PERIOD_MILLIS + " millis.");
                break;
            }
            LOG.info("Expected JobStatus " + expectedStatus + " but is actually " + actualStatus + " after " + retry*POLLING_PERIOD_MILLIS + " millis.");
            Thread.sleep(POLLING_PERIOD_MILLIS);
        }
        return actualStatus;
    }
}
