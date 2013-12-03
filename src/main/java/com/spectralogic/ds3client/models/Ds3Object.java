package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Ds3Object  {
    @JacksonXmlProperty(isAttribute=true)
    private String name;
    @JacksonXmlProperty(isAttribute=true)
    private long size;

    public Ds3Object() {

    }

    public Ds3Object(final String name, long size) {

        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return name + ":" + size;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ds3Object) {
            final Ds3Object ds3Obj = (Ds3Object) obj;
            if (ds3Obj.getName().equals(this.getName()) &&
                    ds3Obj.getSize() == this.getSize()) {
                return true;
            }
        }
        return false;
    }
}
