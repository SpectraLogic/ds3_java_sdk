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

    @Test
    public void getSingleValueLtfs() {
        final String userGuid = "060a2b340101010101010f0013-000000-709e29c2d1e20085-e7610015b2a9-a854";
        final Metadata metadata = genMetadata(new BasicHeader("x-spectra-ltfs-user.guid", userGuid));
        final List<String> userGuids = metadata.get("user.guid");
        assertThat(userGuids, is(notNullValue()));
        assertFalse(userGuids.isEmpty());
        assertThat(userGuids.size(), is(1));
        assertThat(userGuids.get(0), is(userGuid));
    }

    @Test
    public void getTwoValuesLtfs() {
        final String userGuid1 = "060a2b340101010101010f0013-000000-709e29c2d1e20085-e7610015b2a9-a854";
        final String userGuid2 = "160a2b340101010101010f0013-000000-709e29c2d1e20085-e7610015b2a9-a854";
        final Metadata metadata = genMetadata(new BasicHeader("x-spectra-ltfs-user.guid", userGuid1 + "," + userGuid2));
        final List<String> userGuids = metadata.get("user.guid");
        assertThat(userGuids, is(notNullValue()));
        assertFalse(userGuids.isEmpty());
        assertThat(userGuids.size(), is(2));
        assertThat(userGuids.get(0), is(userGuid1));
        assertThat(userGuids.get(1), is(userGuid2));
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
