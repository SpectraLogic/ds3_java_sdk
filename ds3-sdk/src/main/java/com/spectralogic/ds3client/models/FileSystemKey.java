package com.spectralogic.ds3client.models;

import com.spectralogic.ds3client.models.common.CommonPrefixes;

import java.util.Date;

public class FileSystemKey {
    private final String prefix;
    private final String eTag;
    private final String key;
    private final Date lastModified;
    private final User owner;
    private final Long size;
    private final String storageClass;

    private FileSystemKey(final String prefixString) {
        prefix = prefixString;
        eTag = null;
        key = null;
        lastModified = null;
        owner = null;
        size = null;
        storageClass = null;
    }

    public FileSystemKey(final CommonPrefixes commonPrefixes) {
        this(commonPrefixes.getPrefix());
    }

    private FileSystemKey(final String eTag, final  String key, final  Date lastModified, final User owner, final long size, final String storageClass) {
        this.prefix = null;
        this.eTag = eTag;
        this.key = key;
        this.lastModified = lastModified;
        this.owner = owner;
        this.size = size;
        this.storageClass = storageClass;
    }

    public FileSystemKey(final Contents contents) {
        this(contents.getETag(), contents.getKey(), contents.getLastModified(), contents.getOwner(), contents.getSize(), contents.getStorageClass());
    }

    public boolean isPrefix() {
        return prefix != null;
    }

    public boolean isContents() {
        return prefix == null;
    }

    public CommonPrefixes toCommonPrefixes() throws RuntimeException{
       if(isContents()) {
           throw new RuntimeException("Could not create CommonPrefixes");
       }
       final CommonPrefixes commonPrefixes = new CommonPrefixes();
       commonPrefixes.setPrefix(prefix);
       return commonPrefixes;
    }

    public Contents toContents() throws RuntimeException {
        if(isPrefix()) {
            throw new RuntimeException("Could not create Contents");
        }
        final Contents contents = new Contents();
        contents.setETag(eTag);
        contents.setKey(key);
        contents.setLastModified(lastModified);
        contents.setOwner(owner);
        contents.setSize(size);
        contents.setStorageClass(storageClass);
        return contents;
    }

    public String getName() {
        return isPrefix() ? prefix : key;
    }
}