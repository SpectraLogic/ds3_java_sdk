package com.spectralogic.ds3client.integration.test.helpers;

import java.util.UUID;

/**
 * Used to store the UUID of the storage domain member and the
 * data persistence rule when setting up the testing environment.
 * These IDs are stored for teardown of testing environment.
 */
public class TempStorageIds {
    private UUID storageDomainMemberId;
    private UUID dataPersistenceRuleId;

    public TempStorageIds(final UUID storageDomainMemberId, final UUID dataPersistenceRuleId) {
        this.storageDomainMemberId = storageDomainMemberId;
        this.dataPersistenceRuleId = dataPersistenceRuleId;
    }

    public UUID getStorageDomainMemberId() {
        return storageDomainMemberId;
    }

    public UUID getDataPersistenceRuleId() {
        return dataPersistenceRuleId;
    }
}
