package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

public class GetServiceRequest extends AbstractRequest {

    @Override
    public String getPath() {
        return "/";
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }
}
