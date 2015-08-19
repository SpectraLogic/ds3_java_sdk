package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

public class GetPhysicalPlacementRequest extends AbstractRequest {
    @Override
    public String getPath() {
        return null;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }
}
