package com.spectralogic.dsbrowser.gui.components.metadata;

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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linchpin on 7/10/16.
 */
public class MetadataRecievedListenerImpl implements MetadataReceivedListener {

    private String localFilePath = null;

    public MetadataRecievedListenerImpl(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    @Override
    public void metadataReceived(String filename, Metadata metadata) {
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
        String creationTime = null;
        if(metadata.get("ds3-creationTime").size()>0)
         creationTime = metadata.get("ds3-creationTime").get(0);

        String accessTime = null;
        if(metadata.get("ds3-accessTime").size()>0)
         accessTime = metadata.get("ds3-accessTime").get(0);

        String modifiedTime = null;
        if(metadata.get("ds3-lastModifiedTime").size()>0)
         modifiedTime = metadata.get("ds3-lastModifiedTime").get(0);

        if (modifiedTime != null && creationTime != null && accessTime != null) {
           setFileTimes(localFilePath + "/" + objectName , creationTime , modifiedTime ,accessTime );

        }

        String owner = null;
        if(metadata.get("ds3-owner").size()>0)
         owner = metadata.get("ds3-owner").get(0);
        String groupName = null;
        if(metadata.get("ds3-groupName").size()>0)
         groupName = metadata.get("ds3-groupName").get(0);
        if (owner != null && groupName != null && !owner.equals("") && !groupName.equals(""))
            setOwnerNGroup(localFilePath + "/" + objectName, owner, groupName);

        String os = System.getProperty("os.name");
        String storedOS = null;
        if(metadata.get("ds3-os").size()>0)
         storedOS = metadata.get("ds3-os").get(0);
        if(storedOS != null && storedOS.equals(os) && os.contains("Windows")){
           setPermissionsForWindows(metadata, objectName);
        }else{
            if(metadata.get("ds3-permisions").size()>0){
            String permissions = metadata.get("ds3-permisions").get(0);
            if(permissions != null && !permissions.equals("")) {
                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(permissions);
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
        System.out.println(builder.toString());
    }

    private void setPermissionsForWindows(final Metadata metadata , String objectName) {
        Path path = Paths.get(localFilePath + "/" + objectName);
        AclFileAttributeView view1 = Files.getFileAttributeView(path, AclFileAttributeView.class);
        List<AclEntry> aclEntries = new ArrayList<AclEntry>();

        if(metadata.get("ds3-ownerPerm").size()>0) {
            String ownerPermission = metadata.get("ds3-ownerPerm").get(0);
            restorePermissionByUser(ownerPermission, "Administrators", path, aclEntries);
        }

        if(metadata.get("ds3-authenticatedUser").size()>0) {
            String authenticatedUser = metadata.get("ds3-authenticatedUser").get(0);
            restorePermissionByUser(authenticatedUser, "Authenticated Users", path, aclEntries);
        }

        if(metadata.get("ds3-systemUser").size()>0) {
            String systemUserPermission = metadata.get("ds3-systemUser").get(0);
            restorePermissionByUser(systemUserPermission, "SYSTEM", path, aclEntries);
        }

        if(metadata.get("ds3-allUser").size()>0) {
            String allUserPermission = metadata.get("ds3-allUser").get(0);
            restorePermissionByUser(allUserPermission, "Users", path, aclEntries);
        }

        try {
            view1.setAcl(aclEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void restorePermissionByUser(String permission , String user , Path path , List<AclEntry> aclEntries){
        try {
            AclEntry.Builder builderWindow = AclEntry.newBuilder();
            UserPrincipal bRiceUser = FileSystems.getDefault()
                    .getUserPrincipalLookupService().lookupPrincipalByName(user);
            Set<AclEntryPermission> permissions = new HashSet<AclEntryPermission>();
            String [] ordinalArr = permission.split("_");
            for(String ordinal : ordinalArr){
                switch (ordinal){
                    case "0" :
                        permissions.add(AclEntryPermission.READ_DATA);
                        break;
                    case  "1" :
                        permissions.add(AclEntryPermission.WRITE_DATA);
                        break;
                    case  "2" :
                        permissions.add(AclEntryPermission.APPEND_DATA);
                        break;
                    case  "3" :
                        permissions.add(AclEntryPermission.READ_NAMED_ATTRS);
                        break;
                    case  "4" :
                        permissions.add(AclEntryPermission.WRITE_NAMED_ATTRS);
                        break;
                    case  "5" :
                        permissions.add(AclEntryPermission.EXECUTE);
                        break;
                    case  "6" :
                        permissions.add(AclEntryPermission.DELETE_CHILD);
                        break;
                    case  "7" :
                        permissions.add(AclEntryPermission.READ_ATTRIBUTES);
                        break;
                    case  "8" :
                        permissions.add(AclEntryPermission.WRITE_ATTRIBUTES);
                        break;
                    case  "9" :
                        permissions.add(AclEntryPermission.DELETE);
                        break;
                    case  "10" :
                        permissions.add(AclEntryPermission.READ_ACL);
                        break;
                    case  "11" :
                        permissions.add(AclEntryPermission.WRITE_ACL);
                        break;
                    case  "12" :
                        permissions.add(AclEntryPermission.WRITE_OWNER);
                        break;
                    case  "13" :
                        permissions.add(AclEntryPermission.SYNCHRONIZE);
                        break;
                }
            }
            builderWindow.setPrincipal(bRiceUser);
            builderWindow.setPermissions(permissions);
            builderWindow.setType(AclEntryType.ALLOW);
            AclEntry aclEntry = builderWindow.build();
            aclEntries.add(aclEntry);
        }catch (Exception e){
            System.out.println("Exception in between");
        }
    }
    //set the last modified time saved on server
    private void setModifiedTime(String filePath, Long modifiedTime) {
        try {
            Path file = Paths.get(filePath);
            BasicFileAttributes attr =
                    Files.readAttributes(file, BasicFileAttributes.class);
            FileTime ft = FileTime.fromMillis(modifiedTime);
            Files.setLastModifiedTime(file, ft);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //set the permissions saved on server
    private void setPermissions(String filePath, String permissions) {
        try {
            Path file = Paths.get(filePath);
            Set<PosixFilePermission> perms =
                    PosixFilePermissions.fromString(permissions);
/*            FileAttribute<Set<PosixFilePermission>> attr =
                    PosixFilePermissions.asFileAttribute(perms);*/
            Files.setPosixFilePermissions(file, perms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //set owner and group name on local from the black perl server
    private void setOwnerNGroup(String filePath, String ownerName, String groupName) {
        try {
            Path file = Paths.get(filePath);
            UserPrincipal owner = file.getFileSystem().getUserPrincipalLookupService()
                    .lookupPrincipalByName(ownerName);
            Files.setOwner(file, owner);
            GroupPrincipal group =
                    file.getFileSystem().getUserPrincipalLookupService()
                            .lookupPrincipalByGroupName(groupName);
            Files.getFileAttributeView(file, PosixFileAttributeView.class)
                    .setGroup(group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFileTimes(String filePath, String creationTime,String lastModifiedTime , String lastAccessedTime) {
        try {
            BasicFileAttributeView attributes = Files.getFileAttributeView(Paths.get(filePath), BasicFileAttributeView.class);
            FileTime timeCreation = FileTime.fromMillis(Long.parseLong(creationTime));
            FileTime timeModified = FileTime.fromMillis(Long.parseLong(lastModifiedTime)+60000);
            FileTime timeAccessed = FileTime.fromMillis(Long.parseLong(lastAccessedTime));
            attributes.setTimes(timeModified, timeAccessed, timeCreation);
            File f = new File(filePath);
            f.setLastModified(Long.parseLong(lastModifiedTime)+60000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
