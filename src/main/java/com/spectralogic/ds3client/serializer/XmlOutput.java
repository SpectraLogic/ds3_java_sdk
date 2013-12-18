package com.spectralogic.ds3client.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

public class XmlOutput {
    private static final JacksonXmlModule module;
    private static final XmlMapper mapper;

    static {
        module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        mapper = new XmlMapper(module);
        final SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFailOnUnknownId(false);
        mapper.setFilters(filterProvider);
    }

    public static String toXml(Object object) throws XmlProcessingException {
        return toXml(object, null);
    }

    public static String toXml(Object object, FilterProvider filterProvider) throws XmlProcessingException {
        try {
            if (filterProvider == null) {
                return mapper.writeValueAsString(object);
            }
            else {
                return mapper.writer(filterProvider).writeValueAsString(object);
            }
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
