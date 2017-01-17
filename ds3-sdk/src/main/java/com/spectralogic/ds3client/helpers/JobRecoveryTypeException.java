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

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.models.JobRequestType;

import java.util.UUID;

/**
 * This exception is thrown when an attempted recover is performed using {@link Ds3ClientHelpers}
 * and the {@link JobRequestType} of the specified job does not match the type of recovery to be performed.
 * {@link Ds3ClientHelpers#recoverReadJob(UUID)} requires the specified job to be of type {@link JobRequestType#GET}.
 * {@link Ds3ClientHelpers#recoverWriteJob(UUID)} requires the specified job to be of type {@link JobRequestType#PUT}.
 */
public class JobRecoveryTypeException extends JobRecoveryException {

    public JobRecoveryTypeException(final String expectedType, final String actualType) {
        super(buildMessage(expectedType, actualType));
    }

    private static String buildMessage(final String expectedType, final String actualType) {
        return String.format("Expected job type '%s' but the actual job was of type '%s'.", expectedType, actualType);
    }
}
