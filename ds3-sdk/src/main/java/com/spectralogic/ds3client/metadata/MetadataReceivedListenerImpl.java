package com.spectralogic.ds3client.metadata;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.helpers.MetadataReceivedListener;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.MetaDataUtil;
import com.sun.jna.platform.win32.WinNT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sulabh on 7/10/16.
 */
public class MetadataReceivedListenerImpl implements MetadataReceivedListener {
    static private final Logger LOG = LoggerFactory.getLogger(MetadataReceivedListenerImpl.class);
    private String localFilePath = null;
    private Map<Path, Path> map;
    private MetaDataUtil metadataUtil = new MetaDataUtil();

    public MetadataReceivedListenerImpl(final String localFilePath) {
        this.localFilePath = localFilePath;

    }

    @Override
    public void metadataReceived(String filename, final Metadata metadata) {
        final String actualFilePath = metadataUtil.getRealFilePath(localFilePath, filename);
        restoreMetaData(actualFilePath, metadata);
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
        restoreUserAndOwner(os, objectName, metadata);
        String storedOS = null;

        if (metadata.get(MetaDataUtil.KEY_OS).size() > 0) {
            storedOS = metadata.get(MetaDataUtil.KEY_OS).get(0);
        }

        if (storedOS != null && storedOS.equals(os) && os.contains("Windows")) {
            if (metadata.get(MetaDataUtil.KEY_FLAGS).size() > 0) {
                String flags = metadata.get(MetaDataUtil.KEY_FLAGS).get(0);
                metadataUtil.restoreFlagsWindows(objectName, flags);
            }
        }

        if (storedOS != null && storedOS.equals(os) && os.contains("Windows")) {
            setPermissionsForWindows(metadata, objectName);

        } else {
            if (metadata.get(MetaDataUtil.KEY_PERMISSION).size() > 0) {
                String permissions = metadata.get(MetaDataUtil.KEY_PERMISSION).get(0);
                if (permissions != null && !permissions.equals("")) {
                    Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(permissions);
                    while (m.find()) {
                        permissions = (m.group(1));
                    }
                    metadataUtil.setPermissionsLnx(objectName, permissions);
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

    /**
     * restore owner and group of file based on OS
     *
     * @param os         : operating system
     * @param objectName : fileName
     * @param metadata   : metadata retrieved from BlackPerl
     */
    private void restoreUserAndOwner(String os, String objectName, Metadata metadata) {

        //if current os is linux or mac
        String ownerId = null;
        if (!os.contains("windows")) {
            if (metadata.get(MetaDataUtil.KEY_UID).size() > 0) {
                ownerId = metadata.get(MetaDataUtil.KEY_UID).get(0);
            }
            String groupId = null;
            if (metadata.get(MetaDataUtil.KEY_GID).size() > 0) {
                groupId = metadata.get(MetaDataUtil.KEY_GID).get(0);
            }
            if (ownerId != null && groupId != null && !ownerId.equals("") && !groupId.equals("")) {
                metadataUtil.setOwnerNGroupLnx(objectName, ownerId, groupId);
            }

        } else {
            String ownerSid = null;
            if (metadata.get(MetaDataUtil.KEY_OWNER).size() > 0)
                ownerSid = metadata.get(MetaDataUtil.KEY_OWNER).get(0);
            String groupSid = null;
            if (metadata.get(MetaDataUtil.KEY_GROUP).size() > 0)
                groupSid = metadata.get(MetaDataUtil.KEY_GROUP).get(0);
            if (ownerSid != null && groupSid != null && !ownerSid.equals("") && !groupSid.equals("")) {
                metadataUtil.setOwnerIdandGroupIdWin(objectName, ownerSid, groupSid);
            }
        }
    }

    private void setPermissionsForWindows(final Metadata metadata, final String objectName) {
        final Path path = Paths.get(objectName);
        final AclFileAttributeView aclAttributeView = Files.getFileAttributeView(path, AclFileAttributeView.class);
        final ImmutableList.Builder<AclEntry> aclEntryBuilder = new ImmutableList.Builder<>();

        String userList = "";
        String userListDisplay = "";
        String[] users = null;
        String[] usersDisplay = null;
        if (metadata.get("ds3-userList").size() > 0) {
            userList = metadata.get("ds3-userList").get(0);
            if (metadata.get("ds3-userListDisplay").size() > 0) {
                userListDisplay = metadata.get("ds3-userListDisplay").get(0);
                users = userList.split("-");
                usersDisplay = userListDisplay.split("-");
                for (int i = 0; i < users.length; i++) {
                    if (metadata.get("ds3-" + users[i]).size() > 0) {
                        final String ownerPermission = metadata.get("ds3-" + users[i]).get(0);
                        restorePermissionByUser(ownerPermission, usersDisplay[i], aclEntryBuilder);
                    }
                }
            }
        }
        try {
            aclAttributeView.setAcl(aclEntryBuilder.build());
        } catch (final IOException e) {
            LOG.error("Unable to restore dacl view", e);
        }
    }

    private void restorePermissionByUser(final String permission, final String user, final ImmutableList.Builder<AclEntry> aclEntryBuilder) {
        try {
            final AclEntry.Builder builderWindow = AclEntry.newBuilder();
            final UserPrincipal userPrinciple = FileSystems.getDefault()
                    .getUserPrincipalLookupService().lookupPrincipalByName(user);
            final Set<AclEntryPermission> permissions = new HashSet<AclEntryPermission>();
            final String[] ordinalArr = permission.split("-");
            for (final String ordinal : ordinalArr) {
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
            builderWindow.setPrincipal(userPrinciple);
            builderWindow.setPermissions(permissions);
            builderWindow.setType(AclEntryType.ALLOW);
            final AclEntry aclEntry = builderWindow.build();
            aclEntryBuilder.add(aclEntry);
        } catch (final Exception e) {
            LOG.error("Unable to restore Permissions", e);
        }
    }


}
