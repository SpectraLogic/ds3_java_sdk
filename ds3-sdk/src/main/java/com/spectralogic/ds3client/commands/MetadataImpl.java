package com.spectralogic.ds3client.commands;

import com.google.common.collect.ImmutableMultimap;
import com.spectralogic.ds3client.networking.Headers;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.Guard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class MetadataImpl implements Metadata {

    private static final String X_AMZ_META = "x-amz-meta-";
    final ImmutableMultimap<String, String> metadata;

    MetadataImpl(final Headers headers) {
        this.metadata  = genMetadata(headers);
    }

    private static ImmutableMultimap<String, String> genMetadata(final Headers headers) {

        if (headers == null || Guard.isNullOrEmpty(headers.keys())) {
            return ImmutableMultimap.of();
        }

        final ImmutableMultimap.Builder<String, String> mapBuilder = ImmutableMultimap.builder();

        for (final String key : headers.keys()) {
            if (key.startsWith(X_AMZ_META)) {
                final String name = key.substring(X_AMZ_META.length());

                final List<String> values = getValues(headers, key);
                mapBuilder.putAll(name, values);
            }
        }

        return mapBuilder.build();
    }

    private static List<String> getValues(final Headers headers, final String key) {


        final List<String> valueList = headers.get(key);
        final List<String> returnList = new ArrayList<>(valueList.size());

        for (final String valueEntry : valueList) {
            final String[] splitEntries = valueEntry.split(",");

            for (final String splitEntry : splitEntries) {
                returnList.add(splitEntry.trim());
            }
        }

        return returnList;
    }

    @Override
    public List<String> get(final String name) {
        return metadata.get(name).asList();
    }

    @Override
    public Set<String> keys() {
        return metadata.keySet();
    }
}
