/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.UUID;
import java.lang.String;
import java.util.Date;

@JacksonXmlRootElement(namespace = "Data")
public class Pool {

    // Variables
    @JsonProperty("AssignedToStorageDomain")
    private boolean assignedToStorageDomain;

    @JsonProperty("AvailableCapacity")
    private long availableCapacity;

    @JsonProperty("BucketId")
    private UUID bucketId;

    @JsonProperty("Guid")
    private String guid;

    @JsonProperty("Health")
    private PoolHealth health;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LastAccessed")
    private Date lastAccessed;

    @JsonProperty("LastModified")
    private Date lastModified;

    @JsonProperty("LastVerified")
    private Date lastVerified;

    @JsonProperty("Mountpoint")
    private String mountpoint;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("PartitionId")
    private UUID partitionId;

    @JsonProperty("PoweredOn")
    private boolean poweredOn;

    @JsonProperty("Quiesced")
    private Quiesced quiesced;

    @JsonProperty("ReservedCapacity")
    private long reservedCapacity;

    @JsonProperty("State")
    private PoolState state;

    @JsonProperty("StorageDomainId")
    private UUID storageDomainId;

    @JsonProperty("TotalCapacity")
    private long totalCapacity;

    @JsonProperty("Type")
    private PoolType type;

    @JsonProperty("UsedCapacity")
    private long usedCapacity;

    // Constructor
    public Pool() {
        //pass
    }

    // Getters and Setters
    
    public boolean getAssignedToStorageDomain() {
        return this.assignedToStorageDomain;
    }

    public void setAssignedToStorageDomain(final boolean assignedToStorageDomain) {
        this.assignedToStorageDomain = assignedToStorageDomain;
    }


    public long getAvailableCapacity() {
        return this.availableCapacity;
    }

    public void setAvailableCapacity(final long availableCapacity) {
        this.availableCapacity = availableCapacity;
    }


    public UUID getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(final UUID bucketId) {
        this.bucketId = bucketId;
    }


    public String getGuid() {
        return this.guid;
    }

    public void setGuid(final String guid) {
        this.guid = guid;
    }


    public PoolHealth getHealth() {
        return this.health;
    }

    public void setHealth(final PoolHealth health) {
        this.health = health;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public Date getLastAccessed() {
        return this.lastAccessed;
    }

    public void setLastAccessed(final Date lastAccessed) {
        this.lastAccessed = lastAccessed;
    }


    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(final Date lastModified) {
        this.lastModified = lastModified;
    }


    public Date getLastVerified() {
        return this.lastVerified;
    }

    public void setLastVerified(final Date lastVerified) {
        this.lastVerified = lastVerified;
    }


    public String getMountpoint() {
        return this.mountpoint;
    }

    public void setMountpoint(final String mountpoint) {
        this.mountpoint = mountpoint;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public UUID getPartitionId() {
        return this.partitionId;
    }

    public void setPartitionId(final UUID partitionId) {
        this.partitionId = partitionId;
    }


    public boolean getPoweredOn() {
        return this.poweredOn;
    }

    public void setPoweredOn(final boolean poweredOn) {
        this.poweredOn = poweredOn;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }

    public void setQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
    }


    public long getReservedCapacity() {
        return this.reservedCapacity;
    }

    public void setReservedCapacity(final long reservedCapacity) {
        this.reservedCapacity = reservedCapacity;
    }


    public PoolState getState() {
        return this.state;
    }

    public void setState(final PoolState state) {
        this.state = state;
    }


    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }

    public void setStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId;
    }


    public long getTotalCapacity() {
        return this.totalCapacity;
    }

    public void setTotalCapacity(final long totalCapacity) {
        this.totalCapacity = totalCapacity;
    }


    public PoolType getType() {
        return this.type;
    }

    public void setType(final PoolType type) {
        this.type = type;
    }


    public long getUsedCapacity() {
        return this.usedCapacity;
    }

    public void setUsedCapacity(final long usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

}