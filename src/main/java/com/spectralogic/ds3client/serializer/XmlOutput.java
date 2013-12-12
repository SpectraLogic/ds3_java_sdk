package com.spectralogic.ds3client.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.spectralogic.ds3client.models.MasterObjectList;

import java.io.IOException;
import java.util.Map;

public class XmlOutput {
    private static final JacksonXmlModule module;
    private static final XmlMapper mapper;

    static {
        module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        mapper = new XmlMapper(module);
    }

    public static String toXml(Object object) throws XmlProcessingException {
        try {
            return mapper.writeValueAsString(object);
        }
        catch(JsonProcessingException e) {
            throw new XmlProcessingException(e);
        }
    }

    public static<T> T fromXml(String xmlString, Class<T> type) throws IOException {
        final T rootElement = mapper.readValue(xmlString, type);
        return rootElement;
    }
}
