package com.spectralogic.ds3client.exceptions;

/**
 * Created by sulabh on 9/12/16.
 */
public class MetaDataException extends RuntimeException {

    private String message;



    @Override
    public String getMessage() {
        return message;
    }



    public MetaDataException(final String message,final Throwable throwable) {
        super(message,throwable);
        this.message = message;
    }
}
