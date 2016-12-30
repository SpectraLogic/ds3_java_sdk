package com.spectralogic.ds3client.metadata;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.helpers.MetadataReceivedListener;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.MetaDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.spectralogic.ds3client.utils.MetadataKeyConstants.*;

public class MetadataReceivedListenerImpl implements MetadataReceivedListener {
    static private final Logger LOG = LoggerFactory.getLogger(MetadataReceivedListenerImpl.class);

    private String localFilePath = null;
    private final MetaDataUtil metadataUtil = new MetaDataUtil();

    public MetadataReceivedListenerImpl(final String localFilePath) {
        this.localFilePath = localFilePath;
    }

    @Override
    public void metadataReceived(final String filename, final Metadata metadata) {
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



        String creationTime = null;
        if(metadata.get(KEY_CREATION_TIME).size()>0) {
            creationTime = metadata.get(KEY_CREATION_TIME).get(0);
        }

        String accessTime = null;
        if(metadata.get(KEY_ACCESS_TIME).size()>0) {
            accessTime = metadata.get(KEY_ACCESS_TIME).get(0);
        }

        String modifiedTime = null;
        if(metadata.get(KEY_LAST_MODIFIED_TIME).size()>0) {
            modifiedTime = metadata.get(KEY_LAST_MODIFIED_TIME).get(0);
        }

        if (modifiedTime != null && creationTime != null && accessTime != null) {
            final String modifiedTimes = modifiedTime;
            final String creationTimes = creationTime;
            final String accessTimes = accessTime;

            if (os.contains("Mac")) {
                metadataUtil.restoreCreationTimeMAC(objectName,creationTime);
                metadataUtil.restoreModifiedTimeMAC(objectName,modifiedTime);
            } else {
                //Executor service that will change modified time and creation time after 10 seconds
                final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(new Runnable() {
                    @Override
                    public void run() {
                        metadataUtil.setFileTimes(objectName, creationTimes, modifiedTimes, accessTimes);
                    }
                }, 10, TimeUnit.SECONDS);

            }

        }



        if (metadata.get(KEY_OS).size() > 0) {
            storedOS = metadata.get(KEY_OS).get(0);
        }

        if (storedOS != null && storedOS.equals(os) && os.contains("Windows")) {
            if (metadata.get(KEY_FLAGS).size() > 0) {
                final String flags = metadata.get(KEY_FLAGS).get(0);
                metadataUtil.restoreFlagsWindows(objectName, flags);
            }
        }

        if (storedOS != null && storedOS.equals(os) && os.contains("Windows")) {
            setPermissionsForWindows(metadata, objectName);

        } else {
            if (metadata.get(KEY_PERMISSION).size() > 0) {
                String permissions = metadata.get(KEY_PERMISSION).get(0);
                if (permissions != null && !permissions.equals("")) {
                    final Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(permissions);
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
    private void restoreUserAndOwner(final String os, final String objectName,final Metadata metadata) {

        //if current os is linux or mac
        String ownerId = null;
        if (!os.contains("windows")) {
            if (metadata.get(KEY_UID).size() > 0) {
                ownerId = metadata.get(KEY_UID).get(0);
            }
            String groupId = null;
            if (metadata.get(KEY_GID).size() > 0) {
                groupId = metadata.get(KEY_GID).get(0);
            }
            if (ownerId != null && groupId != null && !ownerId.equals("") && !groupId.equals("")) {
                metadataUtil.setOwnerNGroupLnx(objectName, ownerId, groupId);
            }
        } else {
            String ownerSid = null;
            if (metadata.get(KEY_OWNER).size() > 0) {
                ownerSid = metadata.get(KEY_OWNER).get(0);
            }
            String groupSid = null;
            if (metadata.get(KEY_GROUP).size() > 0) {
                groupSid = metadata.get(KEY_GROUP).get(0);
            }
            if (ownerSid != null && groupSid != null && !ownerSid.equals("") && !groupSid.equals("")) {
                metadataUtil.setOwnerIdandGroupIdWin(objectName, ownerSid, groupSid);
            }
        }
    }

    /**
     *
     * @param metadata
     * @param objectName
     */
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
            final Set<AclEntryPermission> permissions = new HashSet<>();
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
