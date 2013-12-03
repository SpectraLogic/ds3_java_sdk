package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName="objects")
public class Objects {

    @JsonProperty("")
    private List<Ds3Object> object;

    public List<Ds3Object> getObject() {
        return object;
    }

    public void setObject(List<Ds3Object> object) {
        this.object = object;
    }

    public String toString() {
        return object.toString();
    }
}
