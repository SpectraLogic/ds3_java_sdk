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

package com.spectralogic.ds3client.utils;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PropertyUtils {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyUtils.class);

    private static final String SDK_VERSION_PROPERTY_NAME = "version";
    private static final String PROPERTIES_FILE_NAME = "ds3_sdk.properties";

    private static final AtomicReference<String> versionProperty = new AtomicReference<>("");

    private PropertyUtils() {}

    /**
     * Get the sdk version out of a properties file
     * @return The sdk version, if we can find one, an empty string otherwise.
     */
    public static String getSdkVersion() {
        if ( ! Guard.isStringNullOrEmpty(versionProperty.get())) {
            return versionProperty.get();
        }

        final Properties properties = new Properties();

        try (final InputStream propertiesStream = PropertyUtils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {
            if (propertiesStream != null) {
                properties.load(propertiesStream);
                final Object versionPropertyObject = properties.get(SDK_VERSION_PROPERTY_NAME);

                if (versionPropertyObject != null) {
                    final String versionFromPropFile = properties.get(SDK_VERSION_PROPERTY_NAME).toString();

                    if ( ! Guard.isStringNullOrEmpty(versionFromPropFile)) {
                        versionProperty.set(versionFromPropFile);
                    }
                }
            }
        } catch (final Throwable t) {
            LOG.warn("Could not read properties file.", t);
        }

        return versionProperty.get();
    }
}
