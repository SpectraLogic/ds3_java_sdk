package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.helpers.ExceptionClassifier;
import com.spectralogic.ds3client.helpers.JobPart;

import java.io.IOException;

/**
 * A {@link TransferRetryDecorator} that will continue to retry a blob transfer, until the transfer either
 * succeeds or experiences an unrecoverable exception.
 */
public class ContinueForeverTransferRetryDecorator implements TransferRetryDecorator {
    private TransferMethod transferMethodDelegate;

    /**
     * @param jobPart An instance of {@link JobPart}, which tells us which Black Pearl is the source
     *                or destination for a blob transfer.
     * @throws IOException
     */
    @Override
    public void transferJobPart(final JobPart jobPart) throws IOException {
        while(true) {
            try {
                transferMethodDelegate.transferJobPart(jobPart);
                break;
            } catch (final Throwable t) {
                if (ExceptionClassifier.isUnrecoverableException(t)) {
                    throw t;
                }
            }
        }
    }

    /**
     * @param transferMethod An instance of {@link TransferMethod} used as a delegate when attempting a transfer.
     * @return An instance of this class, with the intent to be able to chain a call to
     *         {@code transferJobPart}.
     */
    @Override
    public TransferMethod wrap(final TransferMethod transferMethod) {
        Preconditions.checkNotNull(transferMethod, "transferMethod may not be null.");
        transferMethodDelegate = transferMethod;
        return this;
    }
}
