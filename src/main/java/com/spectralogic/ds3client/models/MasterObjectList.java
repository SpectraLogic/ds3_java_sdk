package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;
import java.util.UUID;

public class MasterObjectList {
    private List<Objects> objects;

    @JacksonXmlProperty
    private UUID jobid;

    public MasterObjectList() {}

    public List<Objects> getObjects() {
        return objects;
    }

    public void setObjects(List<Objects> objects) {
        this.objects = objects;
    }

    public String toString() {
        return objects.toString();
    }

    public UUID getJobid() {
        return jobid;
    }

    public void setJobid(UUID jobid) {
        this.jobid = jobid;
    }
}
