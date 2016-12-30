package com.spectralogic.ds3client.utils;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.metadata.MetaDataAccessImpl;
import com.spectralogic.ds3client.metadata.jna.Advapi32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import static com.spectralogic.ds3client.utils.MetadataKeyConstants.*;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public class MetaDataUtil {

    static private final Logger LOG = LoggerFactory.getLogger(MetaDataAccessImpl.class);

    //mode for linux
    public static final String mMode = "";
    //creation time key
    private String mCreationTime = "";
    //access time key
    private String mAccessTime = "";
    private String mlastModified = "";
    //file is a directory
    private String mDirectory = "";
    //type of file is regular
    private String mRegularFile = "";
    //type is other
    private String mOther = "";
    //type of a file
    private String mType = "";
    //whether symbolic link
    private String mSymbolicLink = "";
    //size of file
    private String mSize = "";
    //file format
    private String mFileFormat = "";
    //owner sid for windows
    private String mOwner = "";
    //group sid for windows
    private String mGroup = "";
    //user id of a file linux
    private String mUid = "";
    //group id for linux
    private String mGid = "";
    //control flag
    private String mFlags = "";
    private String mOwnerName = "";
    private String mGroupName = "";
    //dacl String for windows
    private String mDacl = "";
    //os
    private String mOS = "";
    //permissions for each user
    private String mUserList = "";
    //name of all users/groups
    private String mUserListDisplay = "";
    //permissions
    private String mPermissions = "";
    private ImmutableMap.Builder<String, String> mMetadataMap;

    public MetaDataUtil(final ImmutableMap.Builder<String, String> metadataMap) {
        this.mMetadataMap = metadataMap;
    }

    public MetaDataUtil() {
    }

    public static String getmMode() {
        return mMode;
    }

    public String getmOwnerName() {
        return mOwnerName;
    }

    public void setmOwnerName(final String mOwnerName) {
        this.mOwnerName = mOwnerName;
    }

    public String getmGroupName() {
        return mGroupName;
    }

    public void setmGroupName(final String mGroupName) {
        this.mGroupName = mGroupName;
    }

    public String getmCreationTime() {
        return mCreationTime;
    }

    public void setmCreationTime(final String mCreationTime) {
        this.mCreationTime = mCreationTime;
    }

    public String getmAccessTime() {
        return mAccessTime;
    }

    public void setmAccessTime(final String mAccessTime) {
        this.mAccessTime = mAccessTime;
    }

    public String getMlastModified() {
        return mlastModified;
    }

    public void setMlastModified(final String mlastModified) {
        this.mlastModified = mlastModified;
    }

    public String getmDirectory() {
        return mDirectory;
    }

    public void setmDirectory(final String mDirectory) {
        this.mDirectory = mDirectory;
    }

    public String getmRegularFile() {
        return mRegularFile;
    }

    public void setmRegularFile(final String mRegularFile) {
        this.mRegularFile = mRegularFile;
    }

    public String getmOther() {
        return mOther;
    }

    public void setmOther(final String mOther) {
        this.mOther = mOther;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(final String mType) {
        this.mType = mType;
    }

    public String getmSymbolicLink() {
        return mSymbolicLink;
    }

    public void setmSymbolicLink(final String mSymbolicLink) {
        this.mSymbolicLink = mSymbolicLink;
    }

    public String getmSize() {
        return mSize;
    }

    public void setmSize(final String mSize) {
        this.mSize = mSize;
    }

    public String getmFileFormat() {
        return mFileFormat;
    }

    public void setmFileFormat(final String mFileFormat) {
        this.mFileFormat = mFileFormat;
    }

    public String getmOwner() {
        return mOwner;
    }

    public void setmOwner(final String mOwner) {
        this.mOwner = mOwner;
    }

    public String getmGroup() {
        return mGroup;
    }

    public void setmGroup(final String mGroup) {
        this.mGroup = mGroup;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(final String mUid) {
        this.mUid = mUid;
    }

    public String getmGid() {
        return mGid;
    }

    public void setmGid(final String mGid) {
        this.mGid = mGid;
    }

    public String getmFlags() {
        return mFlags;
    }

    public void setmFlags(final String mFlags) {
        this.mFlags = mFlags;
    }

    public String getmDacl() {
        return mDacl;
    }

    public void setmDacl(final String mDacl) {
        this.mDacl = mDacl;
    }

    public String getmOS() {
        return mOS;
    }

    public void setmOS(final String mOS) {
        this.mOS = mOS;
    }

    public String getmUserList() {
        return mUserList;
    }

    public void setmUserList(final String mUserList) {
        this.mUserList = mUserList;
    }

    public String getmUserListDisplay() {
        return mUserListDisplay;
    }

    public void setmUserListDisplay(final String mUserListDisplay) {
        this.mUserListDisplay = mUserListDisplay;
    }

    public String getmPermissions() {
        return mPermissions;
    }

    public void setmPermissions(final String mPermissions) {
        this.mPermissions = mPermissions;
    }

    public ImmutableMap.Builder<String, String> getmMetadataMap() {
        return mMetadataMap;
    }

    public void setmMetadataMap(final ImmutableMap.Builder<String, String> mMetadataMap) {
        this.mMetadataMap = mMetadataMap;
    }

    public Set<String> getSupportedFileAttributes(final Path file) {
        final FileSystem store = file.getFileSystem();
        return store.supportedFileAttributeViews();
    }

    /**
     * @param attr basic file attributes
     * @return file creation time in String
     */
    public String saveCreationTimeMetaData(final BasicFileAttributes attr) {
        mCreationTime = String.valueOf(attr.creationTime().toMillis());
        mMetadataMap.put(METADATA_PREFIX + KEY_CREATION_TIME, mCreationTime);
        return mCreationTime;
    }


    /**
     * @param file path of file
     * @return user id of file Owner inLinux
     * @throws IOException
     */
    public String saveUserId(final Path file) throws IOException {
        final int uid = (int) Files.getAttribute(file, "unix:uid", NOFOLLOW_LINKS);
        mMetadataMap.put(METADATA_PREFIX + KEY_UID, String.valueOf(uid));
        setmUid(String.valueOf(uid));
        return String.valueOf(uid);
    }


    /**
     * @param file path of file
     * @return group id of file Owner inLinux
     * @throws IOException
     */
    public String saveGroupId(final Path file) throws IOException {
        final int gid = (int) Files.getAttribute(file, "unix:gid", NOFOLLOW_LINKS);
        mMetadataMap.put(METADATA_PREFIX + KEY_GID, String.valueOf(gid));
        setmGid(String.valueOf(gid));
        return String.valueOf(gid);
    }

    /**
     * @param file path of file
     * @return group id of file Owner inLinux
     * @throws IOException
     */

    public String saveModeMetaData(final Path file) throws IOException {
        final int mode = (int) Files.getAttribute(file, "unix:mode", NOFOLLOW_LINKS);
        final String modeOctal = Integer.toOctalString(mode);
        mMetadataMap.put(METADATA_PREFIX + KEY_MODE, modeOctal);
        return modeOctal;
    }


    /**
     * @param attr basic file attributes
     * @return file last access time in String
     */
    public String saveAccessTimeMetaData(final BasicFileAttributes attr) {
        mAccessTime = String.valueOf(attr.lastAccessTime().toMillis());
        mMetadataMap.put(METADATA_PREFIX + KEY_ACCESS_TIME, mAccessTime);
        return mAccessTime;
    }


    /**
     * @param attr basic file attributes
     * @return file last modified time in String
     */
    public String saveLastModifiedTime(final BasicFileAttributes attr) {
        mlastModified = String.valueOf(attr.lastModifiedTime().toMillis());
        mMetadataMap.put(METADATA_PREFIX + KEY_LAST_MODIFIED_TIME, mlastModified);
        return mlastModified;
    }

    /**
     * @return name of Operting System in String
     */

    public String getOS() {
        if (mOS == null || mOS.equals("")) {
            mOS = System.getProperty("os.name");
        }
        return mOS;
    }

    /**
     * save os meta data and return os name
     *
     * @return os name
     */
    public String saveOSMetaData() {
        final String os = getOS();
        mMetadataMap.put(METADATA_PREFIX + KEY_OS, os);
        return os;
    }

    /**
     * save owner sid , group sid and dacl for windows
     *
     * @param path
     */
    public void saveWindowsDescriptors(final Path path) {
        final  int infoType = WinNT.OWNER_SECURITY_INFORMATION
                | WinNT.GROUP_SECURITY_INFORMATION
                | WinNT.DACL_SECURITY_INFORMATION | 0;

        final PointerByReference ppsidOwner = new PointerByReference();
        final PointerByReference ppsidGroup = new PointerByReference();
        final PointerByReference ppDacl = new PointerByReference();
        final PointerByReference ppSecurityDescriptor = new PointerByReference();

        final File file = path.toFile();
        try {
            final int bool = Advapi32.INSTANCE.GetNamedSecurityInfo(
                    file.getAbsolutePath(),
                    1,
                    infoType,
                    ppsidOwner,
                    ppsidGroup,
                    ppDacl,
                    null,
                    ppSecurityDescriptor);
            if (bool == 0) {
                final WinNT.PSID psidOwner = new WinNT.PSID(ppsidOwner.getValue().getByteArray(0, 256));
                final String ownerSid = psidOwner.getSidString();

                final WinNT.PSID psidGroup = new WinNT.PSID(ppsidGroup.getValue().getByteArray(0, 256));
                final String groupSid = psidGroup.getSidString();

                mMetadataMap.put(METADATA_PREFIX + KEY_GROUP, groupSid);
                setmGroup(groupSid);
                mMetadataMap.put(METADATA_PREFIX + KEY_OWNER, ownerSid);
                setmOwner(ownerSid);

                final WinNT.ACL acl = new WinNT.ACL(ppDacl.getValue());
                final String daclString = getDaclString(acl);
                setmDacl(daclString);
                mMetadataMap.put(METADATA_PREFIX + KEY_DACL, daclString);
            }
        } catch (final Exception e) {
            LOG.error("Unable to get sid of user and owner", e);
        }
    }


    /**
     * save flag of file in windows
     *
     * @param file
     * @return flag of file in String
     */
    public String saveFlagMetaData(final Path file) {
        final StringBuilder flagBuilder = new StringBuilder();
        try {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("attrib  ");
            stringBuilder.append("\""+file+"\"");

            final ProcessBuilder processBuilder = new ProcessBuilder("attrib",file.toString());
            final  Process process =  processBuilder.start();
            final BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            final String flagWindows =  reader.readLine();
            final String[] flags = flagWindows.split(" ");

            for (int i = 0; i < flags.length - 1; i++) {
                if(!flags[i].equals("")) {
                    if (flags[i].contains("\\")) {
                        break;
                    } else {
                        flagBuilder.append(flags[i].trim());
                    }
                }
            }
            if(flagBuilder.toString().equals("")){
                flagBuilder.append("N");
            }
            mMetadataMap.put(METADATA_PREFIX + KEY_FLAGS, flagBuilder.toString());
            setmFlags(flagBuilder.toString());
        } catch (final IOException ioe) {
            LOG.error("Unable to read file", ioe);
        } catch (final Exception e) {
            LOG.error("Unable to fetch attributes of file", e);
        }
        return flagBuilder.toString();
    }


    /**
     * Get the dacl string from acl
     *
     * @param acl acl got from jna
     * @return dacl string
     */
    private String getDaclString(final WinNT.ACL acl) {
        final WinNT.ACCESS_ACEStructure[] aceStructures = acl.getACEStructures();
        String daclString = "";
        for (int i = 0; i < aceStructures.length; i++) {
            daclString = daclString + "(";
            final WinNT.ACCESS_ACEStructure aceStructure = aceStructures[i];
            if (aceStructure.AceType == 0) {
                daclString = daclString + "A;";
            } else if (aceStructure.AceType == 1) {
                daclString = daclString + "D;";
            } else if (aceStructure.AceType == 2) {
                daclString = daclString + "AU;";
            }

            daclString = daclString + "0x" + Integer.toHexString(aceStructure.AceFlags) + ";";
            daclString = daclString + "0x" + Integer.toHexString(aceStructure.Mask) + ";;;";
            daclString = daclString + (aceStructure.getSidString()) + ")";
        }
        return daclString;
    }

    /**
     * @param attr PosixFileAttributes to get posix info
     * @return owner name of the file
     */
    public String saveOwnerNameMetaData(final PosixFileAttributes attr) {
        final UserPrincipal owner = attr.owner();
        final String ownerName = owner.getName();
        mMetadataMap.put(METADATA_PREFIX + KEY_OWNER_NAME, ownerName);
        setmOwnerName(ownerName);
        return ownerName;
    }


    /**
     * @param attr PosixFileAttributes to get posix info
     * @return group name of the file owner
     */
    public String saveGroupNameMetaData(final PosixFileAttributes attr) {
        final GroupPrincipal group = attr.group();
        final String groupName = group.getName();
        mMetadataMap.put(METADATA_PREFIX + KEY_GROUP_NAME, groupName);
        setmGroupName(groupName);
        return groupName;
    }

    /**
     * @param attr PosixFileAttributes to get posix info
     * @return file permissions in octal
     */
    public String savePosixPermssionsMeta(final PosixFileAttributes attr) {
        final String permissionsOctal = getPermissionInOctal(PosixFilePermissions.toString(attr.permissions()));
        mMetadataMap.put(METADATA_PREFIX + KEY_PERMISSION, permissionsOctal);
        setmPermissions(permissionsOctal);
        return permissionsOctal;
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


    /**
     * set the owner and group on the file by using owner and group sid
     *
     * @param filePath   path of the file
     * @param ownerSidId sid of the owner
     * @param groupSidId sid of the group
     */
    public void setOwnerIdandGroupIdWin(final String filePath, final String ownerSidId, final String groupSidId) {
        try {
            final int infoType = WinNT.OWNER_SECURITY_INFORMATION | WinNT.GROUP_SECURITY_INFORMATION;

            final WinNT.PSIDByReference referenceOwner = new WinNT.PSIDByReference();
            Advapi32.INSTANCE.ConvertStringSidToSid(ownerSidId, referenceOwner);

            final WinNT.PSIDByReference referenceGroup = new WinNT.PSIDByReference();
            Advapi32.INSTANCE.ConvertStringSidToSid(groupSidId, referenceGroup);
            final File file = new File(filePath);

            final int i = Advapi32.INSTANCE.SetNamedSecurityInfo(file.getAbsolutePath(), 1, infoType, referenceOwner.getValue().getPointer(), referenceGroup.getValue().getPointer(), null, null);

            if (i != 0) {
                LOG.error("not able to set owner and group on the file", i);
            }
        } catch (final Exception e) {
            LOG.error("not able to set owner and group on the file ", e);
        }
    }

    /**
     * set owner and group name on local from the black perl server in case of linux
     * @param filePath
     * @param ownerName
     * @param groupName
     */
    public void setOwnerNGroupLnx(final String filePath, final String ownerName, final String groupName) {
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
            LOG.error("Unable to set owner and group name", e);
        }
    }

    /**
     * restore  the attributes of windows
     *
     * @param filePath : path of the file
     * @param flag     : flag retrieved from blackperl
     */
    public void restoreFlagsWindows(final String filePath, final String flag) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("attrib");
        if (flag.contains("A")) {
            stringBuilder.append(" +A");
        }
        if (flag.contains("R")) {
            stringBuilder.append(" +R");
        }
        if (flag.contains("H")) {
            stringBuilder.append(" +H");
        }
        if (flag.contains("S")) {
            stringBuilder.append(" +S");
        }
        if (flag.contains("I")) {
            stringBuilder.append(" +I");
        }
        if (flag.contains("N")) {
            stringBuilder.append(" -A");
            stringBuilder.append(" -R");
            stringBuilder.append(" -I");
            stringBuilder.append(" -H");
        }
        stringBuilder.append(" " + "\""+filePath+"\"");
        try {
            final Process p = Runtime.getRuntime().exec(stringBuilder.toString().split(" "));
            p.waitFor();
        } catch (final Exception e) {
            LOG.error("Unable to restore flag attributes", e);
        }
    }

    /**
     * restore the linux permissions
     *
     * @param filePath    path of the file
     * @param permissions permissions got from the blackperl server
     */
    public void setPermissionsLnx(final String filePath, final String permissions) {
        try {
            final Path file = Paths.get(filePath);
            final Set<PosixFilePermission> perms =
                    PosixFilePermissions.fromString(permissions);
            Files.setPosixFilePermissions(file, perms);
        } catch (final Exception e) {
            LOG.error("Unable to restore Permissions", e);
        }
    }

    /**
     * Get the actual file path from the objectName received from BP
     *
     * @param localFilePath :path of local directory
     * @param filename      : name of the file with logical folder
     * @return actualFilePath
     */
    public String getRealFilePath(final String localFilePath, final String filename) {
        final String filePath = localFilePath + "/" + filename;
        String fName = filePath;
        while (true) {
            final File file = new File(fName);
            if (file.exists()) {
                break;
            } else {
                final File parentFile = new File(file.getParent());
                fName = parentFile.getParent() + "/" + file.getName();
            }
        }
        return fName;
    }

    /**
     * Restore the creation time , access time and modified time to local
     *
     * @param filePath
     * @param creationTime
     * @param lastModifiedTime
     * @param lastAccessedTime
     */
    public void setFileTimes(final String filePath,final String creationTime,final String lastModifiedTime ,final String lastAccessedTime) {
        try {
            final BasicFileAttributeView attributes = Files.getFileAttributeView(Paths.get(filePath), BasicFileAttributeView.class);
            final FileTime timeCreation = FileTime.fromMillis(Long.parseLong(creationTime));
            final FileTime timeModified = FileTime.fromMillis(Long.parseLong(lastModifiedTime));
            final FileTime timeAccessed = FileTime.fromMillis(Long.parseLong(lastAccessedTime));
            attributes.setTimes(timeModified, timeAccessed, timeCreation);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     *Restore creation time in mac only if target creation is before current creation time
     * @param objectName
     * @param creationTime
     */
    public void restoreCreationTimeMAC(final String objectName, final String creationTime) {
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder("touch","-t", getDate(Long.parseLong(creationTime),"YYYYMMddHHmm"),objectName);
            processBuilder.start();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     *
     * Restore modified time in case of MAC using touch command
     * @param objectName
     * @param modifiedTime
     */
    public void restoreModifiedTimeMAC(final String objectName, final String modifiedTime) {
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder("touch","-mt", getDate(Long.parseLong(modifiedTime),"YYYYMMddHHmm"),objectName);
            processBuilder.start();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    public static String getDate(final long milliSeconds, final String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
