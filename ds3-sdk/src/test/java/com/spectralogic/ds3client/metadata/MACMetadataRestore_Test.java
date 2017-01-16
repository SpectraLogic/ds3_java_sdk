/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */
package com.spectralogic.ds3client.metadata;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.spectralogic.ds3client.commands.interfaces.MetadataImpl;
import com.spectralogic.ds3client.metadata.interfaces.MetadataRestoreListener;
import com.spectralogic.ds3client.networking.Headers;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.MetaDataUtil;
import com.spectralogic.ds3client.utils.MetadataKeyConstants;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

@RunWith(JUnit4.class)
public class MACMetadataRestore_Test {

    private final File file = new File(getClass().getClassLoader().getResource("LoremIpsum.txt").getFile());

    @Test
    public void restoreFileTimes_Test() throws Exception {

        if (MetaDataUtil.getOS().contains("Mac")) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm");
            final BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            final BasicHeader basicHeader[] = new BasicHeader[3];
            basicHeader[0] = new BasicHeader(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_CREATION_TIME, String.valueOf(attr.creationTime().toMillis()));
            basicHeader[1] = new BasicHeader(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_ACCESS_TIME, String.valueOf(attr.lastAccessTime().toMillis()));
            basicHeader[2] = new BasicHeader(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_LAST_MODIFIED_TIME, String.valueOf(attr.lastModifiedTime().toMillis()));
            final Metadata metadata = genMetadata(basicHeader);
            final MACMetadataRestore macMetadataRestore = new MACMetadataRestore(metadata, file.getPath(), MetaDataUtil.getOS(), Mockito.mock(MetadataRestoreListener.class));
            macMetadataRestore.restoreFileTimes();
            final BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            Assert.assertEquals(simpleDateFormat.format(fileAttributes.creationTime().toMillis()), simpleDateFormat.format(Long.valueOf(basicHeader[0].getValue())));
            Assert.assertEquals(simpleDateFormat.format(fileAttributes.lastModifiedTime().toMillis()), simpleDateFormat.format(Long.valueOf(basicHeader[2].getValue())));

        }

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
