/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
import com.spectralogic.ds3client.models.bulk.Ds3ObjectList;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class XmlOutput {
    private static final JacksonXmlModule module;
    private static final XmlMapper mapper;
    static private final Logger LOG = LoggerFactory.getLogger(XmlOutput.class);
    public static final String PRODUCTION_BUILD = "productionBuild";

    static {
        module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        mapper = new XmlMapper(module);
        final SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFailOnUnknownId(false);
        mapper.setFilterProvider(filterProvider);
        if (isProductionBuild()) {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        } else {
            LOG.info("Non-production build: Asserting on de-serializing unknown elements and attributes in XML response payloads.");
        }
    }

    protected static boolean isProductionBuild() {
        String productionBuild = System.getenv(PRODUCTION_BUILD);
        if (productionBuild != null) {
            return productionBuild.equals("true");
        }

        final Properties props = new Properties();
        final InputStream input = XmlOutput.class.getClassLoader().getResourceAsStream("ds3_sdk.properties");
        if (input == null) {
            LOG.error("Could not find property file.");
        }
        else {
            try {
                props.load(input);
                productionBuild = (String) props.get(PRODUCTION_BUILD);
                if (productionBuild != null && productionBuild.equals("true")) {
                    return true;
                }
                else {
                    LOG.error("Unknown productionBuild value[{}].  Defaulting to false for unknown XML elements.", productionBuild);
                }
            } catch (final IOException e) {
                LOG.error("Failed to load property file: {}", e);
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
            throw new XmlProcessingException(e);
        }
    }

    public static String toXml(final Ds3ObjectList objects, final boolean isBulkPut) {
        if (isBulkPut) {
            return XmlOutput.toXml(objects);
        }
        final FilterProvider filters = new SimpleFilterProvider().addFilter("sizeFilter",
                SimpleBeanPropertyFilter.serializeAllExcept("Size"));
        return XmlOutput.toXml(objects, filters);
    }

    public static<T> T fromXml(final String xmlString, final Class<T> type) throws IOException {
        return mapper.readValue(xmlString, type);
    }

    public static<T> T fromXml(final InputStream stream, final Class<T> type) throws IOException {
        return mapper.readValue(stream, type);
    }
}
