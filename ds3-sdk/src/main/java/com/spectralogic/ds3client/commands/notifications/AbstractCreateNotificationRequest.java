package com.spectralogic.ds3client.commands.notifications;

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;

public abstract class AbstractCreateNotificationRequest extends AbstractRequest {

    public AbstractCreateNotificationRequest(final String endpoint) {
        super();
        this.getQueryParams().put("notification_end_point", endpoint);
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }
}
