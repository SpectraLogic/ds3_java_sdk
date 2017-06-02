package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.common.CommonPrefixes;

import java.util.List;

/**
 * Because remoteListDirectory returns both a Contents and a CommonPrefixes
 * We need an object that can shuttle them around.
 */
public class ContentPrefix {
    private final Iterable<Contents> contents;
    private final List<CommonPrefixes> commonPrefixes;

    ContentPrefix(final Iterable<Contents> contents, final List<CommonPrefixes> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
        this.contents = contents;
    }

    public Iterable<Contents> contents() { return contents; }
    public List<CommonPrefixes> commonPrefixes() { return commonPrefixes; }


}
