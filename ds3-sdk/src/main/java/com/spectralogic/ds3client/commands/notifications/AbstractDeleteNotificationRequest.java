package com.spectralogic.ds3client.commands.notifications;

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;

import java.util.UUID;

public abstract class AbstractDeleteNotificationRequest extends AbstractRequest {

    private final UUID notificationId;

    public AbstractDeleteNotificationRequest(final UUID notificationId) {
        super();
        this.notificationId = notificationId;
    }

    public UUID getNotificationId() {
        return this.notificationId;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.DELETE;
    }
}
