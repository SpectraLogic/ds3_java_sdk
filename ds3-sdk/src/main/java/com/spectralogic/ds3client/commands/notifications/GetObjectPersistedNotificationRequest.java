package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

public class GetObjectPersistedNotificationRequest extends AbstractGetNotificationRequest {
    public GetObjectPersistedNotificationRequest(final UUID notificationId) {
        super(notificationId);
    }

    @Override
    public String getPath() {
        return "/_rest_/object_persisted_notification_registration/" + this.getNotificationId().toString();
    }
}
