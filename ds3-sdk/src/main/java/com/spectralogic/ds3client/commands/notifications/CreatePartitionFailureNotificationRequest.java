package com.spectralogic.ds3client.commands.notifications;

public class CreatePartitionFailureNotificationRequest extends AbstractCreateNotificationRequest {
    public CreatePartitionFailureNotificationRequest(final String endpoint) {
        super(endpoint);
    }

    @Override
    public String getPath() {
        return "/_rest_/tape_partition_failure_notification_registration";
    }
}
