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

package com.spectralogic.ds3client.commands.interfaces;

import com.google.common.collect.ImmutableMultimap;
import com.spectralogic.ds3client.networking.Headers;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.Guard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.spectralogic.ds3client.utils.MetadataStringManipulation.toDecodedString;
import static com.spectralogic.ds3client.utils.MetadataStringManipulation.toEncodedKeyString;

public class MetadataImpl implements Metadata {

    private static final String X_AMZ_META = "x-amz-meta-";
    private static final String LTFS_METADATA_PREFIX = "x-spectra-ltfs-";
    private final ImmutableMultimap<String, String> metadata;

    public MetadataImpl(final Headers headers) {
        this.metadata  = genMetadata(headers);
    }

    private static ImmutableMultimap<String, String> genMetadata(final Headers headers) {

        if (headers == null || Guard.isNullOrEmpty(headers.keys())) {
            return ImmutableMultimap.of();
        }

        final ImmutableMultimap.Builder<String, String> mapBuilder = ImmutableMultimap.builder();

        for (final String key : headers.keys()) {
            final String keyWithoutPrefix = metadataKey(key);

            if ( ! keyWithoutPrefix.isEmpty()) {
                final List<String> values = getValues(headers, key);
                mapBuilder.putAll(keyWithoutPrefix, values);
            }
        }

        return mapBuilder.build();
    }

    private static String metadataKey(final String key) {
        if (key.startsWith(X_AMZ_META)) {
            return toDecodedString(key.substring(X_AMZ_META.length()));
        } else if (key.startsWith(LTFS_METADATA_PREFIX)) {
            return toDecodedString(key.substring(LTFS_METADATA_PREFIX.length()));
        }

        return "";
    }

    private static List<String> getValues(final Headers headers, final String key) {
        final List<String> valueList = headers.get(key);
        final List<String> returnList = new ArrayList<>(valueList.size());

        for (final String valueEntry : valueList) {
            final String[] splitEntries = valueEntry.split(",");

            for (final String splitEntry : splitEntries) {
                returnList.add(toDecodedString(splitEntry).trim());
            }
        }

        return returnList;
    }

    @Override
    public List<String> get(final String name) {
        //Only ASCII chars are lower cased, do not search for key with non-ASCII symbols lower cased
        final String lowerCasedName = toDecodedString(toEncodedKeyString(name).toLowerCase());
        return metadata.get(lowerCasedName).asList();
    }

    @Override
    public Set<String> keys() {
        return metadata.keySet();
    }
}
