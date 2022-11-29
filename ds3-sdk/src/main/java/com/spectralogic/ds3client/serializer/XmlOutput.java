/*
 * ******************************************************************************
 *   Copyright 2014-2022 Spectra Logic Corporation. All Rights Reserved.
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
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.spectralogic.ds3client.models.bulk.Ds3ObjectList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class XmlOutput {
    private static final JacksonXmlModule module;
    private static final XmlMapper mapper;
    static private final Logger LOG = LoggerFactory.getLogger(XmlOutput.class);
    public static final String PRODUCTION_BUILD = "productionBuild";

    static {
        module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        mapper = new XmlMapper(module);
        // we must treat <tag/> as having nil value - see discussion at
        // https://github.com/SpectraLogic/ds3_java_sdk/pull/595
        mapper.configure(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL, true);

        mapper.registerModule(new Jdk8Module());
        final SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFailOnUnknownId(false);
        mapper.setFilterProvider(filterProvider);
        if (isProductionBuild()) {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        } else {
            LOG.info("Non-production build: Asserting on de-serializing unknown elements and attributes in XML response payloads.");
        }
    }

    /**
     * Determines if this is a production build. A build is production by default, and only non-production when the
     * production build environment variable is false.
     */
    protected static boolean isProductionBuild() {
        String productionBuild = System.getenv(PRODUCTION_BUILD);
        return productionBuild == null || !productionBuild.equals("false");
    }

    public static String toXml(final Object object) {
        return toXml(object, null);
    }

    private static String toXml(final Object object, final Class view) {
        try {
            if (view == null) {
                return mapper.writeValueAsString(object);
            }
            else {
                return mapper.writerWithView(view).writeValueAsString(object);
            }
        }
        catch(final JsonProcessingException e) {
            throw new XmlProcessingException(e);
        }
    }

    public static String toXml(final Ds3ObjectList objects, final boolean isBulkPut) {
        if (isBulkPut) {
            return XmlOutput.toXml(objects, Views.PutObject.class);
        }
        return XmlOutput.toXml(objects, Views.GetObject.class);
    }

    public static<T> T fromXml(final String xmlString, final Class<T> type) throws IOException {
        return mapper.readValue(xmlString, type);
    }

    public static<T> T fromXml(final InputStream stream, final Class<T> type) throws IOException {
        return mapper.readValue(stream, type);
    }
}
