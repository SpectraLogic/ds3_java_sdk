package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "Objects")
public class Ds3ObjectList {
    @JsonProperty("Object")
    private List<Ds3Object> objects;

    public Ds3ObjectList() {
    }

    public Ds3ObjectList(final List<Ds3Object> objects) {
        this.objects = objects;
    }

    public List<Ds3Object> getObjects() {
        return objects;
    }

    public void setObjects(final List<Ds3Object> objects) {
        this.objects = objects;
    }
}
