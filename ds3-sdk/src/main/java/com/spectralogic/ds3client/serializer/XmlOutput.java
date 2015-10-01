/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.models.bulk.Ds3ObjectList;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class XmlOutput {
    private static final JacksonXmlModule module;
    private static final XmlMapper mapper;

    static {
        module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        mapper = new XmlMapper(module);
        final SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFailOnUnknownId(false);
        mapper.setFilters(filterProvider);
        if (isProductionBuild()) {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
    }

    protected static boolean isProductionBuild() {
        final Properties props = new Properties();
        final InputStream input = XmlOutput.class.getClassLoader().getResourceAsStream("/config.properties");
        if (input == null) {
            System.err.println("Could not find property file.");
        }
        else {
            try {
                props.load(input);
                final String productionBuild = (String) props.get("productionBuild");
                if (productionBuild.equals("true")) {
                    return true;
                }
                else {
                    System.err.println("Unknown productionBuild value[" + productionBuild + "].  Defaulting to fail for unknown XML elements.");
                }
            } catch (final IOException e) {
                System.err.println("Failed to load property file.");
            }
        }

        return false;

    }

    public static String toXml(final Object object) {
        return toXml(object, null);
    }

    private static String toXml(final Object object, final FilterProvider filterProvider) {
        try {
            if (filterProvider == null) {
                return mapper.writeValueAsString(object);
            }
            else {
                return mapper.writer(filterProvider).writeValueAsString(object);
            }
        }
        catch(final JsonProcessingException e) {
            throw new RuntimeException(new XmlProcessingException(e));
        }
    }

    public static String toXml(final Ds3ObjectList objects, final BulkCommand command) {
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

    public static<T> T fromXml(final InputStream stream, final Class<T> type) throws IOException {
        return mapper.readValue(stream, type);
    }
}
