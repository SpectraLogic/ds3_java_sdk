package com.spectralogic.ds3client.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.models.Objects;

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

    public static String toXml(final Object object) throws XmlProcessingException {
        return toXml(object, null);
    }

    public static String toXml(final Object object, final FilterProvider filterProvider) throws XmlProcessingException {
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

    public static String toXml(final Objects objects, final BulkCommand command) throws XmlProcessingException {
        if (command == BulkCommand.GET) {
            final FilterProvider filters = new SimpleFilterProvider().addFilter("sizeFilter",
                    SimpleBeanPropertyFilter.serializeAllExcept("size"));
            return XmlOutput.toXml(objects, filters);
        }
        return XmlOutput.toXml(objects);
    }

    public static<T> T fromXml(final String xmlString, final Class<T> type) throws IOException {
        return mapper.readValue(xmlString, type);
    }
}
