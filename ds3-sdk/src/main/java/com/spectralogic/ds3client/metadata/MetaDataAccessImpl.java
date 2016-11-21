package com.spectralogic.dsbrowser.gui.components.metadata;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.networking.Metadata;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.*;

import java.util.*;

/**
 * Implementation of MetaDataAcess Interface
 * Used to store meta data on Server
 */
public class MetaDataAccessImpl implements Ds3ClientHelpers.MetadataAccess {
    private Map<String, Path> fileMapper = null;

    public MetaDataAccessImpl(Map<String, Path> fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public Map<String, String> getMetadataValue(String filename) {
        Map<String, String> metadata = new HashMap<>();
        try {
            Path file = fileMapper.get(filename);
            metadata = storeMetaData(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return metadata;
    }

    /*
    * to store the meta data on server
    *
    * */
    private Map<String, String> storeMetaData(Path file) {
        final Map<String, String> metadata = new HashMap<>();
        try {
            FileSystem store = file.getFileSystem();
            System.out.println(store.getFileStores().iterator().next().name());
            Set<String> sets = store.supportedFileAttributeViews();
            for (String set : sets) {
                switch (set) {
                    case "basic":
                        BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                        metadata.put("x-amz-meta-ds3-creationTime", String.valueOf(attr.creationTime().toMillis()));
                        metadata.put("x-amz-meta-ds3-accessTime", String.valueOf(attr.lastAccessTime().toMillis()));
                        metadata.put("x-amz-meta-ds3-lastModifiedTime", String.valueOf(attr.lastModifiedTime().toMillis()));
                        metadata.put("x-amz-meta-ds3-isDirectory", String.valueOf(attr.isDirectory()));
                        metadata.put("x-amz-meta-ds3-isOther", String.valueOf(attr.isOther()));
                        metadata.put("x-amz-meta-ds3-isRegularFile", String.valueOf(attr.isRegularFile()));
                        metadata.put("x-amz-meta-ds3-isSymbolicLink", String.valueOf(attr.isSymbolicLink()));
                        metadata.put("x-amz-meta-ds3-size", String.valueOf(attr.size()));
                        String ext = FilenameUtils.getExtension(file.getParent() + "/" + file.getFileName());
                        metadata.put("x-amz-meta-ds3-fileFormat", ext);
                        String os = System.getProperty("os.name");
                        metadata.put("x-amz-meta-ds3-os", os);
                        //if os is windows then posix will not be called and we need to find permission in different manner
                        if (os.contains("Windows")) {
                            saveWindowsfilePermissions(file, metadata);
                        }
                        break;

                    case "owner":
                        FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(file, FileOwnerAttributeView.class);
                        UserPrincipal owner = ownerAttributeView.getOwner();
                        metadata.put("x-amz-meta-ds3-owner", owner.getName());
                        break;

                    case "user":
                        break;

                    case "unix":
                        break;

                    case "posix":
                        PosixFileAttributes attr1 = Files.readAttributes(file, PosixFileAttributes.class);
                        // metadata.put("x-amz-meta-ds3-owner", attr1.owner().getName());
                        metadata.put("x-amz-meta-ds3-groupName", attr1.group().getName());
                        metadata.put("x-amz-meta-ds3-permisions", getModes(PosixFilePermissions.toString(attr1.permissions())));
                        break;
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return metadata;
    }

    // get the octal number for the permission
    private String getModes(String permissions) {
        String permString = new String(permissions);
        permissions = permissions.replaceAll("r", "4");
        permissions = permissions.replaceAll("w", "2");
        permissions = permissions.replaceAll("x", "1");
        permissions = permissions.replaceAll("-", "0");
        String ownerPerm = String.valueOf(Integer.parseInt(String.valueOf(permissions.charAt(0))) + Integer.parseInt(String.valueOf(permissions.charAt(1))) + Integer.parseInt(String.valueOf(permissions.charAt(2))));
        String groupPerm = String.valueOf(Integer.parseInt(String.valueOf(permissions.charAt(3))) + Integer.parseInt(String.valueOf(permissions.charAt(4))) + Integer.parseInt(String.valueOf(permissions.charAt(5))));
        String otherPerm = String.valueOf(Integer.parseInt(String.valueOf(permissions.charAt(6))) + Integer.parseInt(String.valueOf(permissions.charAt(7))) + Integer.parseInt(String.valueOf(permissions.charAt(8))));
        String totalPerm = ownerPerm + groupPerm + otherPerm;
        return totalPerm + "(" + permString + ")";
    }

    //if os is windows then posix will not be called and we need to find permission in different manner
    private void saveWindowsfilePermissions(Path file, Map<String, String> metadata) {
        try {
            AclFileAttributeView view = Files.getFileAttributeView(file, AclFileAttributeView.class);
            List<AclEntry> acl = view.getAcl();
            Set<AclEntryPermission> aclEntryPermissions;
            String userType = "";
            StringBuilder permission = null;
            for (AclEntry ac : acl) {
                permission = new StringBuilder();
                int index = 1;
                aclEntryPermissions = ac.permissions();
                for (AclEntryPermission aclEntryPermission : aclEntryPermissions) {
                    if (index == aclEntryPermissions.size())
                        permission.append(aclEntryPermission.ordinal());
                    else
                        permission.append(aclEntryPermission.ordinal() + "_");
                    index++;
                }
                userType = ac.principal().getName();
                switch (userType) {
                    case "BUILTIN\\Administrators":
                        metadata.put("x-amz-meta-ds3-ownerPerm", permission.toString());
                        break;
                    case "NT AUTHORITY\\SYSTEM":
                        metadata.put("x-amz-meta-ds3-authenticatedUser", permission.toString());
                        break;

                    case "NT AUTHORITY\\Authenticated Users":
                        metadata.put("x-amz-meta-ds3-systemUser", permission.toString());
                        break;

                    case "BUILTIN\\Users":
                        metadata.put("x-amz-meta-ds3-allUser", permission.toString());
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
