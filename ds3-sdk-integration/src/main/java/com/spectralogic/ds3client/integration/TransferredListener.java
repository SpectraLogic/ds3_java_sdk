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
