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

package com.spectralogic.ds3client.metadata;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.utils.Platform;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;

@RunWith(JUnit4.class)
public class PosixMetadataStore_Test {

    private final File file = new File(getClass().getClassLoader().getResource("LoremIpsum.txt").getFile());
    private final ImmutableMap.Builder<String, String> mMetadataMap = new ImmutableMap.Builder<>();
    private final PosixMetadataStore posixMetadataStore = new PosixMetadataStore(mMetadataMap);

    @Test
    public void saveCreationTimeMetaData_Test() throws IOException {
        final BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        final long creationTime = attr.creationTime().toMillis();
        posixMetadataStore.saveCreationTimeMetaData(attr);
        Assert.assertEquals(mMetadataMap.build().get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_CREATION_TIME), String.valueOf(creationTime));
    }

    @Test
    public void saveAccessTimeMetaData_Test() throws IOException {
        final BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        final long lastAccessTime = attr.lastAccessTime().toMillis();
        posixMetadataStore.saveAccessTimeMetaData(attr);
        Assert.assertEquals(mMetadataMap.build().get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_ACCESS_TIME), String.valueOf(lastAccessTime));
    }


    @Test
    public void saveLastModifiedTime_Test() throws IOException {
        final BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        final long lastModifiedTime = attr.lastModifiedTime().toMillis();
        posixMetadataStore.saveLastModifiedTime(attr);
        Assert.assertEquals(mMetadataMap.build().get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_LAST_MODIFIED_TIME), String.valueOf(lastModifiedTime));
    }

    @Test
    public void saveOSMetaData_Test() throws IOException {
        final String localOSName = MetaDataUtil.getOS();
        posixMetadataStore.saveOSMetaData(localOSName);
        Assert.assertEquals(mMetadataMap.build().get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_OS), String.valueOf(localOSName));
    }


    @Test
    public void saveUserId() throws Exception {
        if (!MetaDataUtil.getOS().contains("Windows")) {
            final Class aClass = PosixMetadataStore.class;
            final Method metohd = aClass.getDeclaredMethod("saveUserId", new Class[]{Path.class});
            metohd.setAccessible(true);
            final String userId = (String) metohd.invoke(posixMetadataStore, file.toPath());
            Assert.assertNotNull(userId);
        }
    }

    @Test
    public void saveGroupId() throws Exception {
        if (!MetaDataUtil.getOS().contains("Windows")) {
            final Class aClass = PosixMetadataStore.class;
            final Method method = aClass.getDeclaredMethod("saveGroupId", new Class[]{Path.class});
            method.setAccessible(true);
            final String groupId = (String) method.invoke(posixMetadataStore, file.toPath());
            Assert.assertNotNull(groupId);
        }
    }


    @Test
    public void saveModeMetaData() throws Exception {
        if (!MetaDataUtil.getOS().contains("Windows")) {
            final Class aClass = PosixMetadataStore.class;
            final Method method = aClass.getDeclaredMethod("saveModeMetaData", new Class[]{Path.class});
            method.setAccessible(true);
            final String mode = (String) method.invoke(posixMetadataStore, file.toPath());
            Assert.assertNotNull(mode);
        }
    }


    @Test
    public void saveOwnerNameMetaData() throws Exception {
        if (!MetaDataUtil.getOS().contains("Windows")) {
            final PosixFileAttributes attrPosix = Files.readAttributes(file.toPath(), PosixFileAttributes.class);
            final Class aClass = PosixMetadataStore.class;
            final Method method = aClass.getDeclaredMethod("saveOwnerNameMetaData", new Class[]{PosixFileAttributes.class});
            method.setAccessible(true);
            final String ownerName = (String) method.invoke(posixMetadataStore, attrPosix);
            Assert.assertNotNull(ownerName);
        }
    }


    @Test
    public void saveGroupNameMetaData() throws Exception {
        if (!MetaDataUtil.getOS().contains("Windows")) {
            final PosixFileAttributes attrPosix = Files.readAttributes(file.toPath(), PosixFileAttributes.class);
            final Class aClass = PosixMetadataStore.class;
            final Method method = aClass.getDeclaredMethod("saveGroupNameMetaData", new Class[]{PosixFileAttributes.class});
            method.setAccessible(true);
            final String groupName = (String) method.invoke(posixMetadataStore, attrPosix);
            Assert.assertNotNull(groupName);
        }
    }


    @Test
    public void savePosixPermissionsMeta() throws Exception {
        if (!MetaDataUtil.getOS().contains("Windows")) {
            final PosixFileAttributes attrPosix = Files.readAttributes(file.toPath(), PosixFileAttributes.class);
            final Class aClass = PosixMetadataStore.class;
            final Method method = aClass.getDeclaredMethod("savePosixPermissionsMeta", new Class[]{PosixFileAttributes.class});
            method.setAccessible(true);
            final String permission = (String) method.invoke(posixMetadataStore, attrPosix);
            Assert.assertNotNull(permission);
        }
    }


    @Test
    public void saveOSSpecificMetadata() throws Exception {
        if (!Platform.isWindows()) {
            final PosixFileAttributes attr = Files.readAttributes(file.toPath(), PosixFileAttributes.class);
            posixMetadataStore.saveOSSpecificMetadata(file.toPath(), attr);
        }
    }
}
