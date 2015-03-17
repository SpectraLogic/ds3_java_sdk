package com.spectralogic.ds3client.commands.notifications;

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;

public abstract class AbstractDeleteNotificationRequest extends AbstractRequest {
    @Override
    public HttpVerb getVerb() {
        return HttpVerb.DELETE;
    }
}
