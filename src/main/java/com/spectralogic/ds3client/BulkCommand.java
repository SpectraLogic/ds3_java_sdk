package com.spectralogic.ds3client;

public enum BulkCommand {
    PUT, GET;

    public String toString() {
        if (this == PUT) {
            return "start_bulk_put";
        }
        else {
            return "start_bulk_get";
        }
    }
}