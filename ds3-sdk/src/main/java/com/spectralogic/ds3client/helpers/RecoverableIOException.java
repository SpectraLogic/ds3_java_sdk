package com.spectralogic.ds3client.helpers;

import java.io.IOException;

/**
 * A RecoverableIOException is used to classify IOExceptions into one of 2 types:
 * those that should result in retrying a data transfer, such as a failure in an HTTP GET or PUT;
 * and those that should <b>not</b> result in retrying a data transfer, such as a failure writing to
 * or reading from a file.  During an HTTP PUT or GET that involves transferring data to or from
 * a file, file system failures are considered non-recoverable, where network-related failures
 * are considered recoverable.
 */
public class RecoverableIOException extends IOException {
    public RecoverableIOException() {
        super();
    }

    public RecoverableIOException(final String message) {
        super(message);
    }

    public RecoverableIOException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RecoverableIOException(final Throwable cause) {
        super(cause);
    }
}
