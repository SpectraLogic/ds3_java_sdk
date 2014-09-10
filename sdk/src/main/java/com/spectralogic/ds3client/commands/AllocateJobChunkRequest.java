package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

import java.util.UUID;

public class AllocateJobChunkRequest extends AbstractRequest {
    private final UUID chunkId;

    public AllocateJobChunkRequest(final UUID chunkId) {
        this.chunkId = chunkId;
        getQueryParams().put("operation", "allocate");
    }

    @Override
    public String getPath() {
        return "/_rest_/job_chunk/" + chunkId.toString();
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }
}
