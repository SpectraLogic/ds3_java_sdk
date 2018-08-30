package com.spectralogic.ds3client.serializer;

/**
 * Used to annotate Ds3Object attributes to specify which attributes are
 * unique to bulk get vs bulk put request payloads. Note that parameters
 * with no annotation will be marshaled into both get and put payloads.
 * These are used with the JsonView annotation.
 */
public class Views {
    /** Denotes parameters within Ds3Object which are unique to get object request payloads */
    public static class GetObject { }

    /** Denotes parameters within Ds3Object which are unique to get object request payloads */
    public static class PutObject { }
}