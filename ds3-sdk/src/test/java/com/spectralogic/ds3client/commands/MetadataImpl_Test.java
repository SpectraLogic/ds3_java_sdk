package com.spectralogic.ds3client.commands;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.spectralogic.ds3client.commands.interfaces.MetadataImpl;
import com.spectralogic.ds3client.networking.Headers;
import com.spectralogic.ds3client.networking.Metadata;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class MetadataImpl_Test {

    @Test
    public void getValue() {
        final Metadata metadata = genMetadata(new BasicHeader("x-amz-meta-name",  "value"));

        final List<String> value = metadata.get("name");
        assertThat(value, is(notNullValue()));
        assertFalse(value.isEmpty());
        assertThat(value.size(), is(1));
        assertThat(value.get(0), is("value"));
    }

    @Test
    public void getMultiValue() {
        final Metadata metadata = genMetadata(new BasicHeader("x-amz-meta-name", "value1,value2,value3"));
        final List<String> value = metadata.get("name");
        assertThat(value, is(notNullValue()));
        assertFalse(value.isEmpty());
        assertThat(value.size(), is(3));
        assertThat(value.get(0), is("value1"));
        assertThat(value.get(2), is("value3"));
    }

    @Test
    public void getMultiValueWithSpace() {
        final Metadata metadata = genMetadata(new BasicHeader("x-amz-meta-name", "value1, value2, value3"));
        final List<String> value = metadata.get("name");
        assertThat(value, is(notNullValue()));
        assertFalse(value.isEmpty());
        assertThat(value.size(), is(3));
        assertThat(value.get(0), is("value1"));
        assertThat(value.get(2), is("value3"));
    }

    private Metadata genMetadata(final Header... headers) {

        final ImmutableMultimap.Builder<String, String> mapBuilder = ImmutableMultimap.builder();
        for (final Header header : headers) {
            mapBuilder.put(header.getName(), header.getValue());
        }

        final ImmutableMultimap<String, String> map = mapBuilder.build();

        return new MetadataImpl(new Headers() {
            @Override
            public List<String> get(final String key) {
                return Lists.newArrayList(map.get(key));
            }

            @Override
            public Set<String> keys() {
                return Sets.newHashSet(map.keySet());
            }
        });
    }
}
