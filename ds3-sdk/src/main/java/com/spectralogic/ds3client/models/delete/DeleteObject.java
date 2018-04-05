package com.spectralogic.ds3client.models.delete;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteObject {

    @JsonProperty("Key")
    private String key;
    
    @JsonProperty("VersionId")
    private UUID versionId;
    
    public DeleteObject(final String key, final UUID versionId) {
        this.key = key;
        this.versionId = versionId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }
    
    
    public UUID getVersionId()
    {
        return versionId;
    }
    
    
    public void setVersionId( final UUID versionId )
    {
        this.versionId = versionId;
    }
}
