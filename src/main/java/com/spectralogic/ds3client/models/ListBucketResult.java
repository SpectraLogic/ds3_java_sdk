package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

public class ListBucketResult {

    @JsonProperty("Name")
    private String name;
    @JsonProperty("Prefix")
    private String prefix;
    @JsonProperty("Marker")
    private String marker;
    @JsonProperty("MaxKeys")
    private int maxKeys;
    @JsonProperty("IsTruncated")
    private boolean isTruncated;
    @JsonProperty("CreationDate")
    private String creationDate;
    @JsonProperty("Delimiter")
    private String delimiter;
    @JsonProperty("NextMarker")
    private String nextMarker;

    @JsonProperty("Contents")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Contents> contentsList;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(final String marker) {
        this.marker = marker;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(final int maxKeys) {
        this.maxKeys = maxKeys;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(final boolean isTruncated) {
        this.isTruncated = isTruncated;
    }

    public List<Contents> getContentsList() {
        return contentsList;
    }

    public void setContentsList(final List<Contents> contentsList) {
        this.contentsList = contentsList;
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("{{bucket:: ").append(name).append("},\n");
        builder.append("{numKeys:: ").append(maxKeys).append("},\n");
        builder.append("{objects:: ").append(contentsList).append("}}");
        return builder.toString();
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public void setNextMarker(final String nextMarker) {
        this.nextMarker = nextMarker;
    }
}
