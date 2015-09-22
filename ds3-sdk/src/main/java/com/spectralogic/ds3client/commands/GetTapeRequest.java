package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

import java.util.UUID;

public class GetTapeRequest extends AbstractRequest {

    private final UUID id;
    private boolean fullDetails;

    public GetTapeRequest(final UUID id) {
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/_rest_/tape/" + id.toString();
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    public UUID getId() {
        return id;
    }

    public GetTapeRequest withFullDetails(final boolean details) {
        this.fullDetails = details;
        if (this.fullDetails) {
            this.getQueryParams().put("full_details", null);
        } else {
            this.getQueryParams().remove("full_details");
        }
        return this;
    }

    public boolean isFullDetails() {
        return fullDetails;
    }
}
