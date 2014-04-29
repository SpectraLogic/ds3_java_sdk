package com.spectralogic.ds3client.helpers;

public class Ds3KeyNotFoundException extends Exception {
    private static final long serialVersionUID = 1747882336930646141L;
    
    public Ds3KeyNotFoundException(final String objectKey) {
        super(buildExceptionMessage(objectKey));
    }

    private static String buildExceptionMessage(final String objectKey) {
        return String.format("The bulk response referenced an object key '%s' that was never provided to the request.", objectKey);
    }
}
