package com.spectralogic.ds3client.utils;

public class InvalidMd5Exception extends Exception {
    private static final long serialVersionUID = 405742511253458992L;

    public InvalidMd5Exception(final Throwable t) {
        super(t);
    }
}
