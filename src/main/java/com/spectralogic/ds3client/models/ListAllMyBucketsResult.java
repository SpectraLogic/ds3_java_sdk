package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

public class ListAllMyBucketsResult {

    @JsonProperty("Owner")
    private Owner owner;

    @JsonProperty("Buckets")
    @JacksonXmlElementWrapper(useWrapping = true)
    private List<Bucket> buckets;

    public ListAllMyBucketsResult() {}

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(final Owner owner) {
        this.owner = owner;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(final List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("{{owner:: ").append(owner.toString()).append("},\n");
        if(buckets != null) {
            builder.append("{buckets:: ").append(buckets.toString()).append("}}");
        }

        return builder.toString();
    }
}
