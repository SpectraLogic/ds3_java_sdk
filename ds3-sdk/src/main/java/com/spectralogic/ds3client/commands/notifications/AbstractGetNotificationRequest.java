package com.spectralogic.ds3client.commands.notifications;

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;

import java.util.UUID;

public abstract class AbstractGetNotificationRequest extends AbstractRequest {

    private final UUID notificationId;

    public AbstractGetNotificationRequest(final UUID notificationId) {
        super();
        this.notificationId = notificationId;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    public UUID getNotificationId() {
        return notificationId;
    }
}
