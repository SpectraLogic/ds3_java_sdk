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

/**
 * This class manages parts for all of the objects in the job. It aggregates
 * ObjectPartTracker implementations, which manage the parts for a single
 * object.
 */
public interface JobPartTracker {
    void completePart(final String key, final ObjectPart objectPart);
    boolean containsPart(final String key, final ObjectPart objectPart);
    JobPartTracker attachDataTransferredListener(final DataTransferredListener listener);
    JobPartTracker attachObjectCompletedListener(final ObjectCompletedListener listener);
    void removeDataTransferredListener(final DataTransferredListener listener);
    void removeObjectCompletedListener(final ObjectCompletedListener listener);
}
