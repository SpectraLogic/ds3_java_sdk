package com.spectralogic.ds3client.metadata;

import com.google.common.base.Joiner;
import com.spectralogic.ds3client.helpers.MetadataReceivedListener;
import com.spectralogic.ds3client.networking.Metadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetadataReceivedListenerImpl implements MetadataReceivedListener {

    private String localFilePath = null;

    public MetadataReceivedListenerImpl(final String localFilePath) {
        this.localFilePath = localFilePath;
    }

    @Override
    public void metadataReceived(final String filename, final Metadata metadata) {
        restoreMetaData(filename, metadata);
    }

    /**
     * Restore the metadata to local file
     *
     * @param objectName name of the file to be restored
     * @param metadata   metadata which needs to be set on local file
     */
    private void restoreMetaData(final String objectName, final Metadata metadata) {
        final StringBuilder builder = new StringBuilder();
        final Joiner joiner = Joiner.on(", ");

        builder.append("Metadata for object ").append(objectName).append(": ");
        final String os = System.getProperty("os.name");

        String creationTime = null;
        String accessTime = null;
        String modifiedTime = null;
        String owner = null;
        String groupName = null;
        String storedOS = null;

        if (metadata.get("ds3-creationTime").size() > 0)
            creationTime = metadata.get("ds3-creationTime").get(0);

        if (metadata.get("ds3-accessTime").size() > 0)
            accessTime = metadata.get("ds3-accessTime").get(0);

        if (metadata.get("ds3-lastModifiedTime").size() > 0)
            modifiedTime = metadata.get("ds3-lastModifiedTime").get(0);

        if (modifiedTime != null && creationTime != null && accessTime != null) {
            setFileTimes(localFilePath + "/" + objectName, creationTime, modifiedTime, accessTime);
        }

        if (metadata.get("ds3-owner").size() > 0)
            owner = metadata.get("ds3-owner").get(0);

        if (metadata.get("ds3-groupName").size() > 0)
            groupName = metadata.get("ds3-groupName").get(0);

        if (owner != null && groupName != null && !owner.equals("") && !groupName.equals(""))
            setOwnerNGroup(localFilePath + "/" + objectName, owner, groupName);

        if (metadata.get("ds3-os").size() > 0)
            storedOS = metadata.get("ds3-os").get(0);

        if (storedOS != null && storedOS.equals(os) && os.contains("Windows")) {
            setPermissionsForWindows(metadata, objectName);
        } else {
            if (metadata.get("ds3-permisions").size() > 0) {
                String permissions = metadata.get("ds3-permisions").get(0);
                if (permissions != null && !permissions.equals("")) {
                    final Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(permissions);
                    while (m.find()) {
                        permissions = (m.group(1));
                    }
                    setPermissions(localFilePath + "/" + objectName, permissions);
                }
            }
        }


        for (final String metadataKey : metadata.keys()) {
            final List<String> values = metadata.get(metadataKey);
            builder.append("<Key: ")
                    .append(metadataKey)
                    .append(" Values: ")
                    .append(joiner.join(values))
                    .append("> ");
        }
    }

    private void setPermissionsForWindows(final Metadata metadata, final String objectName) {
        final Path path = Paths.get(localFilePath + "/" + objectName);
        final AclFileAttributeView view1 = Files.getFileAttributeView(path, AclFileAttributeView.class);
        final List<AclEntry> aclEntries = new ArrayList<AclEntry>();

        if (metadata.get("ds3-ownerPerm").size() > 0) {
            String ownerPermission = metadata.get("ds3-ownerPerm").get(0);
            restorePermissionByUser(ownerPermission, "Administrators", path, aclEntries);
        }
        if (metadata.get("ds3-authenticatedUser").size() > 0) {
            String authenticatedUser = metadata.get("ds3-authenticatedUser").get(0);
            restorePermissionByUser(authenticatedUser, "Authenticated Users", path, aclEntries);
        }
        if (metadata.get("ds3-systemUser").size() > 0) {
            String systemUserPermission = metadata.get("ds3-systemUser").get(0);
            restorePermissionByUser(systemUserPermission, "SYSTEM", path, aclEntries);
        }
        if (metadata.get("ds3-allUser").size() > 0) {
            String allUserPermission = metadata.get("ds3-allUser").get(0);
            restorePermissionByUser(allUserPermission, "Users", path, aclEntries);
        }
        try {
            view1.setAcl(aclEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restorePermissionByUser(final String permission, final String user, final Path path, final List<AclEntry> aclEntries) {
        try {
            final AclEntry.Builder builderWindow = AclEntry.newBuilder();
            final UserPrincipal bRiceUser = FileSystems.getDefault()
                    .getUserPrincipalLookupService().lookupPrincipalByName(user);
            final Set<AclEntryPermission> permissions = new HashSet<AclEntryPermission>();
            final String[] ordinalArr = permission.split("_");
            for (String ordinal : ordinalArr) {
                switch (ordinal) {
                    case "0":
                        permissions.add(AclEntryPermission.READ_DATA);
                        break;
                    case "1":
                        permissions.add(AclEntryPermission.WRITE_DATA);
                        break;
                    case "2":
                        permissions.add(AclEntryPermission.APPEND_DATA);
                        break;
                    case "3":
                        permissions.add(AclEntryPermission.READ_NAMED_ATTRS);
                        break;
                    case "4":
                        permissions.add(AclEntryPermission.WRITE_NAMED_ATTRS);
                        break;
                    case "5":
                        permissions.add(AclEntryPermission.EXECUTE);
                        break;
                    case "6":
                        permissions.add(AclEntryPermission.DELETE_CHILD);
                        break;
                    case "7":
                        permissions.add(AclEntryPermission.READ_ATTRIBUTES);
                        break;
                    case "8":
                        permissions.add(AclEntryPermission.WRITE_ATTRIBUTES);
                        break;
                    case "9":
                        permissions.add(AclEntryPermission.DELETE);
                        break;
                    case "10":
                        permissions.add(AclEntryPermission.READ_ACL);
                        break;
                    case "11":
                        permissions.add(AclEntryPermission.WRITE_ACL);
                        break;
                    case "12":
                        permissions.add(AclEntryPermission.WRITE_OWNER);
                        break;
                    case "13":
                        permissions.add(AclEntryPermission.SYNCHRONIZE);
                        break;
                }
            }
            builderWindow.setPrincipal(bRiceUser);
            builderWindow.setPermissions(permissions);
            builderWindow.setType(AclEntryType.ALLOW);
            final AclEntry aclEntry = builderWindow.build();
            aclEntries.add(aclEntry);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    //set the last modified time saved on server
    private void setModifiedTime(final String filePath, final Long modifiedTime) {
        try {
            final Path file = Paths.get(filePath);
            final BasicFileAttributes attr =
                    Files.readAttributes(file, BasicFileAttributes.class);
            final FileTime ft = FileTime.fromMillis(modifiedTime);
            Files.setLastModifiedTime(file, ft);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    //set the permissions saved on server
    private void setPermissions(final String filePath, final String permissions) {
        try {
            final Path file = Paths.get(filePath);
            final Set<PosixFilePermission> perms =
                    PosixFilePermissions.fromString(permissions);
/*            FileAttribute<Set<PosixFilePermission>> attr =
                    PosixFilePermissions.asFileAttribute(perms);*/
            Files.setPosixFilePermissions(file, perms);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    //set owner and group name on local from the black perl server
    private void setOwnerNGroup(final String filePath, final String ownerName, final String groupName) {
        try {
            final Path file = Paths.get(filePath);
            final UserPrincipal owner = file.getFileSystem().getUserPrincipalLookupService()
                    .lookupPrincipalByName(ownerName);
            Files.setOwner(file, owner);
            final GroupPrincipal group =
                    file.getFileSystem().getUserPrincipalLookupService()
                            .lookupPrincipalByGroupName(groupName);
            Files.getFileAttributeView(file, PosixFileAttributeView.class)
                    .setGroup(group);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void setFileTimes(final String filePath, final String creationTime, final String lastModifiedTime, final String lastAccessedTime) {
        try {
            final BasicFileAttributeView attributes = Files.getFileAttributeView(Paths.get(filePath), BasicFileAttributeView.class);
            final FileTime timeCreation = FileTime.fromMillis(Long.parseLong(creationTime));
            final FileTime timeModified = FileTime.fromMillis(Long.parseLong(lastModifiedTime) + 60000);
            final FileTime timeAccessed = FileTime.fromMillis(Long.parseLong(lastAccessedTime));
            attributes.setTimes(timeModified, timeAccessed, timeCreation);
            final File f = new File(filePath);
            f.setLastModified(Long.parseLong(lastModifiedTime) + 60000);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
