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

import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.metadata.interfaces.MetadataStoreListener;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_GROUP;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_OWNER;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.METADATA_PREFIX;

@RunWith(JUnit4.class)
public class WindowsMetadataStore_Test {
    private final File file = new File(getClass().getClassLoader().getResource("LoremIpsum.txt").getFile());
    private final ImmutableMap.Builder<String, String> mMetadataMap = new ImmutableMap.Builder<>();
    private final WindowsMetadataStore windowsMetadataStore = new WindowsMetadataStore(mMetadataMap, Mockito.mock(MetadataStoreListener.class));

    @Test
    public void saveCreationTimeMetaData_Test() throws IOException {
        final BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        final long creationTime = attr.creationTime().toMillis();
        windowsMetadataStore.saveCreationTimeMetaData(attr);
        Assert.assertEquals(mMetadataMap.build().get(METADATA_PREFIX + MetadataKeyConstants.KEY_CREATION_TIME), String.valueOf(creationTime));
    }

    @Test
    public void saveAccessTimeMetaData_Test() throws IOException {
        final BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        final long lastAccessTime = attr.lastAccessTime().toMillis();
        windowsMetadataStore.saveAccessTimeMetaData(attr);
        Assert.assertEquals(mMetadataMap.build().get(METADATA_PREFIX + MetadataKeyConstants.KEY_ACCESS_TIME), String.valueOf(lastAccessTime));
    }


    @Test
    public void saveLastModifiedTime_Test() throws IOException {
        final BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        final long lastModifiedTime = attr.lastModifiedTime().toMillis();
        windowsMetadataStore.saveLastModifiedTime(attr);
        Assert.assertEquals(mMetadataMap.build().get(METADATA_PREFIX + MetadataKeyConstants.KEY_LAST_MODIFIED_TIME), String.valueOf(lastModifiedTime));
    }

    @Test
    public void saveOSMetaData_Test() throws IOException {
        final String localOSName = MetaDataUtil.getOS();
        windowsMetadataStore.saveOSMetaData(localOSName);
        Assert.assertEquals(mMetadataMap.build().get(METADATA_PREFIX + MetadataKeyConstants.KEY_OS), String.valueOf(localOSName));
    }

    @Test
    public void saveWinDescriptor_Test() throws Exception {
        if (MetaDataUtil.getOS().contains("Windows")) {
            final Class aClass = WindowsMetadataStore.class;
            final Method method = aClass.getDeclaredMethod("saveWindowsDescriptors", new Class[]{Path.class});
            method.setAccessible(true);
            method.invoke(windowsMetadataStore, file.toPath());
            Assert.assertNotNull(mMetadataMap.build().get(METADATA_PREFIX + KEY_GROUP));
            Assert.assertNotNull(mMetadataMap.build().get(METADATA_PREFIX + KEY_OWNER));

        }
    }

    @Test
    public void saveFlagMetadata_Test() throws Exception {
        if (MetaDataUtil.getOS().contains("Windows")) {
            final Class aClass = WindowsMetadataStore.class;
            final Method method = aClass.getDeclaredMethod("saveFlagMetaData", new Class[]{Path.class});
            method.setAccessible(true);
            final String flags = (String) method.invoke(windowsMetadataStore, file.toPath());
            Assert.assertNotNull(flags);
        }
    }

    @Test
    public void saveWindowsfilePermissions_Test() throws Exception {
        if (MetaDataUtil.getOS().contains("Windows")) {
            final Class aClass = WindowsMetadataStore.class;
            final Method method = aClass.getDeclaredMethod("saveWindowsfilePermissions", new Class[]{Path.class});
            method.setAccessible(true);
            method.invoke(windowsMetadataStore, file.toPath());
            Assert.assertNotNull(mMetadataMap.build().get("x-amz-meta-ds3-userList"));
            Assert.assertNotNull(mMetadataMap.build().get("x-amz-meta-ds3-userListDisplay"));

        }
    }
}
