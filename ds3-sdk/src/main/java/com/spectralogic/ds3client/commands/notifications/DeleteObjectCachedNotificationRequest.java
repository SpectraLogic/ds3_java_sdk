package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

public class DeleteObjectCachedNotificationRequest extends AbstractDeleteNotificationRequest {
    public DeleteObjectCachedNotificationRequest(final UUID notificationId) {
        super(notificationId);
    }

    @Override
    public String getPath() {
        return "/_rest_/object_cached_notification_registration/" + this.getNotificationId().toString();
    }
}
