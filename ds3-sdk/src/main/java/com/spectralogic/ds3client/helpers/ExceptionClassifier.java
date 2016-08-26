package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;

import java.io.IOException;

public class ExceptionClassifier {
    public static boolean isRecoverableException(final Throwable t) {
        if(
                t instanceof Ds3NoMoreRetriesException ||
                t instanceof IOException
        )
        {
            return true;
        }

        return false;
    }

    public static boolean isUnrecoverableException(final Throwable t) {
        return ! isRecoverableException(t);
    }
}
