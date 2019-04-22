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

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.helpers.ExceptionClassifier;
import com.spectralogic.ds3client.helpers.JobPart;

import java.io.IOException;

/**
 * A {@link TransferRetryDecorator} that will continue to retry a blob transfer at most {@code maxNumObjectTransferAttempts}
 * times, until the transfer either succeeds or experiences an unrecoverable exception.
 */
public class MaxNumObjectTransferAttemptsDecorator implements TransferRetryDecorator {
    private final int maxNumObjectTransferAttempts;
    private TransferMethod transferMethodDelegate;

    public MaxNumObjectTransferAttemptsDecorator(final int maxNumObjectTransferAttempts) {
        this.maxNumObjectTransferAttempts = maxNumObjectTransferAttempts;
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

    /**
     * @param jobPart An instance of {@link JobPart}, which tells us which Black Pearl is the source
     *                or destination for a blob transfer.
     * @throws IOException
     */
    @Override
    public void transferJobPart(final JobPart jobPart) throws IOException {
        int objectTransfersAttempted = 0;

        while(true) {
            try {
                transferMethodDelegate.transferJobPart(jobPart);
                break;
            } catch (final Throwable t) {
                if (ExceptionClassifier.isUnrecoverableException(t) || ++objectTransfersAttempted >= maxNumObjectTransferAttempts) {
                    throw t;
                }
            }
        }
    }
}
