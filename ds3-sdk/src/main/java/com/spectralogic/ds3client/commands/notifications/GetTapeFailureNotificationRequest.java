package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

public class GetTapeFailureNotificationRequest extends AbstractGetNotificationRequest{
    public GetTapeFailureNotificationRequest(final UUID notificationId) {
        super(notificationId);
    }

    @Override
    public String getPath() {
        return "/_rest_/tape_failure_notification_registration/" + this.getNotificationId().toString();
    }
}
