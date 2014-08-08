/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.models.bulk.Ds3ObjectList;

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

    private static String toXml(final Object object, final FilterProvider filterProvider) throws XmlProcessingException {
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

    public static String toXml(final Ds3ObjectList objects, final BulkCommand command) throws XmlProcessingException {
        if (command == BulkCommand.GET) {
            final FilterProvider filters = new SimpleFilterProvider().addFilter("sizeFilter",
                    SimpleBeanPropertyFilter.serializeAllExcept("Size"));
            return XmlOutput.toXml(objects, filters);
        }
        return XmlOutput.toXml(objects);
    }

    public static<T> T fromXml(final String xmlString, final Class<T> type) throws IOException {
        return mapper.readValue(xmlString, type);
    }
}
