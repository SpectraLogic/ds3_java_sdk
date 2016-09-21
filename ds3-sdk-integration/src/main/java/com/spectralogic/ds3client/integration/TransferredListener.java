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

package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.helpers.DataTransferredListener;
import com.spectralogic.ds3client.helpers.ObjectCompletedListener;

class TransferredListener implements DataTransferredListener, ObjectCompletedListener {

    private long totalBytes = 0;
    private int numberOfFiles = 0;
    public TransferredListener() {
        //pass
    }

    @Override
    public synchronized void dataTransferred(final long size) {
        this.totalBytes += size;
    }

    public long getTotalBytes() {
        return this.totalBytes;
    }


    @Override
    public synchronized void objectCompleted(final String s) {
        this.numberOfFiles += 1;
    }

    public int getNumberOfFiles() {
        return this.numberOfFiles;
    }
}
