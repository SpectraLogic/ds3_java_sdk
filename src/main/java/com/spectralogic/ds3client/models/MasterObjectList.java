package com.spectralogic.ds3client.models;

import java.util.List;

public class MasterObjectList {
    private List<Objects> objects;

    public MasterObjectList() {}

    public MasterObjectList(List<Objects> objects) {
        this.objects = objects;
    }

    public List<Objects> getObjects() {
        return objects;
    }

    public void setObjects(List<Objects> objects) {
        this.objects = objects;
    }

    public String toString() {
        return objects.toString();
    }
}
