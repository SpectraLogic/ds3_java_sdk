package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

import java.util.UUID;

public class DeleteTapeRequest extends AbstractRequest {

    private final UUID id;

    public DeleteTapeRequest(final UUID id) {
        this.id  = id;
    }

    @Override
    public String getPath() {
        return "/_rest_/tape/" + id.toString();
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.DELETE;
    }

    public UUID getId() {
        return id;
    }
}
