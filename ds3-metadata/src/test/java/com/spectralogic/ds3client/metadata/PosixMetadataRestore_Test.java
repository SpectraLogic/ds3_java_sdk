package com.spectralogic.ds3client.metadata;


import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.spectralogic.ds3client.commands.interfaces.MetadataImpl;
import com.spectralogic.ds3client.networking.Headers;
import com.spectralogic.ds3client.networking.Metadata;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;

import static com.spectralogic.ds3client.metadata.PermissionsUtils.getPermissionInOctal;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

@RunWith(JUnit4.class)
public class PosixMetadataRestore_Test {
    @Before
    public void checkPreconditions() {
        Assume.assumeFalse(org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS);
    }

    private final File file  = new File(getClass().getClassLoader().getResource("LoremIpsum.txt").getFile());

    @Test
    public  void restoreFileTimes_Test() throws Exception{
        final BasicHeader basicHeader[] = new BasicHeader[3];
        final BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        basicHeader[0] = new BasicHeader(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_CREATION_TIME, String.valueOf(attr.creationTime().toMillis()));
        basicHeader[1] = new BasicHeader(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_ACCESS_TIME, String.valueOf(attr.lastAccessTime().toMillis()));
        basicHeader[2] = new BasicHeader(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_LAST_MODIFIED_TIME, String.valueOf(attr.lastModifiedTime().toMillis()));
        final  Metadata metadata = genMetadata(basicHeader);
        final PosixMetadataRestore posixMetadataRestore = new PosixMetadataRestore(metadata,file.getPath(), MetaDataUtil.getOS());
        posixMetadataRestore.restoreFileTimes();
        final BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        Assert.assertEquals(String.valueOf(fileAttributes.creationTime().toMillis()), String.valueOf(basicHeader[0].getValue()));
        Assert.assertEquals(String.valueOf(fileAttributes.lastModifiedTime().toMillis()), String.valueOf(basicHeader[2].getValue()));
    }


      @Test
      public void restoreUserAndOwner() throws IOException{
          final int uid = (int) Files.getAttribute(file.toPath(), "unix:uid", NOFOLLOW_LINKS);
          final int gid = (int) Files.getAttribute(file.toPath(), "unix:gid", NOFOLLOW_LINKS);
          final BasicHeader basicHeader[] = new BasicHeader[2];
          basicHeader[0] = new BasicHeader(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_UID, String.valueOf(uid));
          basicHeader[1] = new BasicHeader(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_GID, String.valueOf(gid));
          final  Metadata metadata = genMetadata(basicHeader);
          final PosixMetadataRestore posixMetadataRestore = new PosixMetadataRestore(metadata, file.getPath(), MetaDataUtil.getOS());
          posixMetadataRestore.restoreUserAndOwner();
          Assert.assertEquals(String.valueOf((int) Files.getAttribute(file.toPath(), "unix:uid", NOFOLLOW_LINKS)), basicHeader[0].getValue());
          Assert.assertEquals(String.valueOf((int) Files.getAttribute(file.toPath(), "unix:gid", NOFOLLOW_LINKS)), basicHeader[1].getValue());
      }


      @Test
      public void restorePermissions() throws  Exception{
          final PosixFileAttributes fileAttributes = Files.readAttributes(file.toPath(), PosixFileAttributes.class);
          final String permissionsOctal = getPermissionInOctal(PosixFilePermissions.toString(fileAttributes.permissions()));
          final BasicHeader basicHeader[] = new BasicHeader[1];
          basicHeader[0] = new BasicHeader(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_PERMISSION, permissionsOctal);
          final  Metadata metadata = genMetadata(basicHeader);
          final PosixMetadataRestore posixMetadataRestore = new PosixMetadataRestore(metadata,file.getPath(), MetaDataUtil.getOS());
          posixMetadataRestore.restorePermissions();
          final PosixFileAttributes fileAttributesAfterRestore = Files.readAttributes(file.toPath(), PosixFileAttributes.class);
          Assert.assertEquals(getPermissionInOctal(PosixFilePermissions.toString(fileAttributesAfterRestore.permissions())), basicHeader[0].getValue());
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
