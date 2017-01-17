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
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.spectralogic.ds3client.commands.interfaces.MetadataImpl;
import com.spectralogic.ds3client.metadata.interfaces.MetadataRestoreListener;
import com.spectralogic.ds3client.metadata.interfaces.MetadataStoreListener;
import com.spectralogic.ds3client.networking.Headers;
import com.spectralogic.ds3client.networking.Metadata;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Set;

import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_GROUP;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_OWNER;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.METADATA_PREFIX;

public class WindowsMetadataRestore_Test {
    @Before
    public void checkPreconditions() {
        Assume.assumeTrue(org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS);
    }

    private final File file = new File(getClass().getClassLoader().getResource("LoremIpsum.txt").getFile());
    private final String localOS = MetaDataUtil.getOS();

    @Test
    public void restoreFileTimes_Test() throws Exception {
        final BasicHeader basicHeader[] = new BasicHeader[3];
        final BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        basicHeader[0] = new BasicHeader(METADATA_PREFIX + MetadataKeyConstants.KEY_CREATION_TIME, String.valueOf(attr.creationTime().toMillis()));
        basicHeader[1] = new BasicHeader(METADATA_PREFIX + MetadataKeyConstants.KEY_ACCESS_TIME, String.valueOf(attr.lastAccessTime().toMillis()));
        basicHeader[2] = new BasicHeader(METADATA_PREFIX + MetadataKeyConstants.KEY_LAST_MODIFIED_TIME, String.valueOf(attr.lastModifiedTime().toMillis()));
        final Metadata metadata = genMetadata(basicHeader);
        final WindowsMetadataRestore windowsMetadataRestore = new WindowsMetadataRestore(metadata, file.getPath(), MetaDataUtil.getOS(), Mockito.mock(MetadataRestoreListener.class));
        windowsMetadataRestore.restoreFileTimes();
        final BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        Assert.assertEquals(String.valueOf(fileAttributes.creationTime().toMillis()), String.valueOf(basicHeader[0].getValue()));
        Assert.assertEquals(String.valueOf(fileAttributes.lastModifiedTime().toMillis()), String.valueOf(basicHeader[2].getValue()));
    }


    @Test
    public void restoreUserAndOwner_Test() throws Exception {
        final ImmutableMap.Builder<String, String> mMetadataMap = new ImmutableMap.Builder<>();
        final WindowsMetadataStore windowsMetadataStore = new WindowsMetadataStore(mMetadataMap, Mockito.mock(MetadataStoreListener.class));
        final Class aClass = WindowsMetadataStore.class;
        final Method method = aClass.getDeclaredMethod("saveWindowsDescriptors", new Class[]{Path.class});
        method.setAccessible(true);
        method.invoke(windowsMetadataStore, file.toPath());

        final BasicHeader basicHeader[] = new BasicHeader[3];
        basicHeader[0] = new BasicHeader(METADATA_PREFIX + KEY_OWNER, mMetadataMap.build().get(METADATA_PREFIX + KEY_OWNER));
        basicHeader[1] = new BasicHeader(METADATA_PREFIX + KEY_GROUP, mMetadataMap.build().get(METADATA_PREFIX + KEY_GROUP));
        basicHeader[2] = new BasicHeader(METADATA_PREFIX + MetadataKeyConstants.KEY_OS, localOS);
        final Metadata metadata = genMetadata(basicHeader);
        final WindowsMetadataRestore windowsMetadataRestore = new WindowsMetadataRestore(metadata, file.getPath(), MetaDataUtil.getOS(), Mockito.mock(MetadataRestoreListener.class));
        windowsMetadataRestore.restoreOSName();
        windowsMetadataRestore.restoreUserAndOwner();

        final ImmutableMap.Builder<String, String> mMetadataMapAfterRestore = new ImmutableMap.Builder<>();
        final WindowsMetadataStore windowsMetadataStoreAfterRestore = new WindowsMetadataStore(mMetadataMapAfterRestore, Mockito.mock(MetadataStoreListener.class));
        final Class aClassAfterRestore = WindowsMetadataStore.class;
        final Method methodAfterRestore = aClassAfterRestore.getDeclaredMethod("saveWindowsDescriptors", new Class[]{Path.class});
        methodAfterRestore.setAccessible(true);
        methodAfterRestore.invoke(windowsMetadataStoreAfterRestore, file.toPath());

        Assert.assertEquals(mMetadataMapAfterRestore.build().get(METADATA_PREFIX + KEY_OWNER), basicHeader[0].getValue());
        Assert.assertEquals(mMetadataMapAfterRestore.build().get(METADATA_PREFIX + KEY_GROUP), basicHeader[1].getValue());
    }


    @Test
    public void restorePermissions_Test() {
        try {
            final ImmutableMap.Builder<String, String> mMetadataMap = new ImmutableMap.Builder<>();
            final WindowsMetadataStore windowsMetadataStore = new WindowsMetadataStore(mMetadataMap, Mockito.mock(MetadataStoreListener.class));
            final Class aClass = WindowsMetadataStore.class;
            final Method method = aClass.getDeclaredMethod("saveFlagMetaData", new Class[]{Path.class});
            method.setAccessible(true);
            method.invoke(windowsMetadataStore, file.toPath());

            final Method methodWindowsfilePermissions = aClass.getDeclaredMethod("saveWindowsfilePermissions", new Class[]{Path.class});
            methodWindowsfilePermissions.setAccessible(true);
            methodWindowsfilePermissions.invoke(windowsMetadataStore, file.toPath());

            final Set<String> mapKeys = mMetadataMap.build().keySet();
            final int keySize = mapKeys.size();
            final BasicHeader basicHeader[] = new BasicHeader[keySize + 1];
            basicHeader[0] = new BasicHeader(METADATA_PREFIX + MetadataKeyConstants.KEY_OS, localOS);
            int count = 1;
            for (final String key : mapKeys) {
                basicHeader[count] = new BasicHeader(key, mMetadataMap.build().get(key));
                count++;
            }

            final Metadata metadata = genMetadata(basicHeader);
            final WindowsMetadataRestore windowsMetadataRestore = new WindowsMetadataRestore(metadata, file.getPath(), MetaDataUtil.getOS(), Mockito.mock(MetadataRestoreListener.class));
            windowsMetadataRestore.restoreOSName();
            windowsMetadataRestore.restorePermissions();
        } catch (final Exception e) {
            Assert.fail();
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

    // get the octal number for the permission
    private String getPermissionInOctal(String permissions) {
        final String permString = new String(permissions);
        permissions = permissions.replaceAll("r", "4");
        permissions = permissions.replaceAll("w", "2");
        permissions = permissions.replaceAll("x", "1");
        permissions = permissions.replaceAll("-", "0");
        final String ownerPerm = String.valueOf(Integer.parseInt(String.valueOf(permissions.charAt(0))) + Integer.parseInt(String.valueOf(permissions.charAt(1))) + Integer.parseInt(String.valueOf(permissions.charAt(2))));
        final String groupPerm = String.valueOf(Integer.parseInt(String.valueOf(permissions.charAt(3))) + Integer.parseInt(String.valueOf(permissions.charAt(4))) + Integer.parseInt(String.valueOf(permissions.charAt(5))));
        final String otherPerm = String.valueOf(Integer.parseInt(String.valueOf(permissions.charAt(6))) + Integer.parseInt(String.valueOf(permissions.charAt(7))) + Integer.parseInt(String.valueOf(permissions.charAt(8))));
        final String totalPerm = ownerPerm + groupPerm + otherPerm;
        return totalPerm + "(" + permString + ")";
    }

}
