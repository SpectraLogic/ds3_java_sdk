package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

public class GetObjectLostNotificationRequest extends AbstractGetNotificationRequest {
    public GetObjectLostNotificationRequest(final UUID notificationId) {
        super(notificationId);
    }

    @Override
    public String getPath() {
        return "/_rest_/object_lost_notification_registration/" + this.getNotificationId().toString();
    }
}
