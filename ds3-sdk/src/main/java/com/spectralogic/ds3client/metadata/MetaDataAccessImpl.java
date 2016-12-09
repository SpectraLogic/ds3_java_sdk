package com.spectralogic.ds3client.metadata;


import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.MetaDataUtil;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.*;
import java.util.*;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

/**
 * Implementation of MetaDataAcess Interface
 * Used to store meta data on Server
 */
public class MetaDataAccessImpl implements Ds3ClientHelpers.MetadataAccess {
    private Map<String, Path> fileMapper = null;
    static private final Logger LOG = LoggerFactory.getLogger(MetaDataAccessImpl.class);

    public MetaDataAccessImpl(final Map<String, Path> fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public Map<String, String> getMetadataValue(final String filename) {
        try {
            final Path file = fileMapper.get(filename);
            return  storeMetaData(file).build();
        } catch (final Exception e) {
            LOG.error("failed to store Metadata",e);
            return null;
        }
    }

    /*
    * to store the meta data on server
    *
    * */
    private ImmutableMap.Builder<String, String> storeMetaData(final Path file) {
        final ImmutableMap.Builder<String, String> metadata = new ImmutableMap.Builder<>(); new ImmutableMap.Builder<String, String>();
        try {
            final MetaDataUtil metadataUtil = new MetaDataUtil(metadata);
            final Set<String>sets = metadataUtil.getSupportedFileAttributes(file);
            final String os = metadataUtil.getOS();
            metadataUtil.saveOSMetaData();
            for (final String set : sets) {
                switch (set) {
                    case "basic":

                        final BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                        metadataUtil.saveCreationTimeMetaData(attr);
                        metadataUtil.saveAccessTimeMetaData(attr);
                        metadataUtil.saveLastModifiedTime(attr);

                        if (os.contains("Windows")) {
                            saveWindowsfilePermissions(file, metadata);
                        }
                        break;
                    case "owner":
//
                        if (os.contains("Windows")) {
                            metadataUtil.saveWindowsDescriptors(file);


                        }
                        break;

                    case "user":
                        break;

                    case "dos":
                        if(os.contains("Windows")) {
                            metadataUtil.saveFlagMetaData(file);

                        }
                        break;

                    case "posix":
                        final PosixFileAttributes attrPosix = Files.readAttributes(file, PosixFileAttributes.class);
                        metadataUtil.saveUserId(file);
                        metadataUtil.saveGroupId(file);
                        metadataUtil.saveModeMetaData(file);
                        metadataUtil.saveOwnerNameMetaData(attrPosix);
                        metadataUtil.saveGroupNameMetaData(attrPosix);
                        metadataUtil.savePosixPermssionsMeta(attrPosix);

                        break;
                }
            }
        } catch (final IOException ioe) {
            LOG.error("unable to get metadata",ioe);
        }
        return metadata;
    }


    //if os is windows then posix will not be called and we need to find permission in different manner
    private void saveWindowsfilePermissions(final Path file, final ImmutableMap.Builder<String, String> metadata) {
        try {
            final AclFileAttributeView view = Files.getFileAttributeView(file, AclFileAttributeView.class);
            final List<AclEntry> aclEntries = view.getAcl();
            Set<AclEntryPermission> aclEntryPermissions;
            String userType = "";
            String userDisplay = "";
            StringBuilder permission = null;
            final StringBuilder userList = new StringBuilder();
            final StringBuilder userDisplayList = new StringBuilder();
            final Map<String, Set<Integer>> stringSetMap = new HashMap<String, Set<Integer>>();
            for (final AclEntry aclEntry : aclEntries) {
                userDisplay = aclEntry.principal().getName().split("\\\\")[1];
                permission = new StringBuilder();
                Set<Integer> newSet = stringSetMap.get(userDisplay);
                aclEntryPermissions = aclEntry.permissions();
                if (newSet == null)
                    newSet = new HashSet<Integer>();
                for (final AclEntryPermission aclEntryPermission : aclEntryPermissions) {
                    newSet.add(aclEntryPermission.ordinal());
                }
                stringSetMap.put(userDisplay, newSet);
            }
            final Set<String> keys = stringSetMap.keySet();
            Set<Integer> ordinals;
            int userCount = 1;
            for (final String key : keys) {
                int index = 1;
                ordinals = stringSetMap.get(key);
                userType = key.replaceAll(" ", "").toLowerCase();
                permission = new StringBuilder();
                for (int ord : ordinals) {
                    if (ordinals.size() == index) {
                        permission.append(ord);
                    }
                    else {
                        permission.append(ord + "-");
                    }
                    index++;
                }
                if (keys.size() == userCount) {
                    userDisplayList.append(key);
                    userList.append(userType);
                } else {
                    userDisplayList.append(key + "-");
                    userList.append(userType + "-");
                }
                metadata.put("x-amz-meta-ds3-" + userType, permission.toString());
                userCount++;
            }
            metadata.put("x-amz-meta-ds3-userList", userList.toString());
            metadata.put("x-amz-meta-ds3-userListDisplay", userDisplayList.toString());
        } catch (final Exception e) {
            LOG.error("Unable to get list of users or their permissions",e);
        }
    }






}