package com.spectralogic.ds3client.models.tape;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(namespace = "Data")
public class Tapes {

    @JsonProperty("Tape")
    private List<Tape> tapes;

    public List<Tape> getTapes() {
        return tapes;
    }

    public void setTapes(List<Tape> tapes) {
        this.tapes = tapes;
    }
}
