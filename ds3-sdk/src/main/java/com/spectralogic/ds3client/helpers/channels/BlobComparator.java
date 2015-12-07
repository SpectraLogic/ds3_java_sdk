package com.spectralogic.ds3client.helpers.channels;

import com.spectralogic.ds3client.models.bulk.BulkObject;

import java.util.Comparator;

/**
 * Used to sort BulkObjects (Blobs).  We only care about the starting
 * offset, so that is what is used to compare.
 */
public class BlobComparator implements Comparator<BulkObject> {
    @Override
    public int compare(final BulkObject o1, final BulkObject o2) {
        return Long.compare(o1.getOffset(), o2.getOffset());
    }

    public static Comparator<BulkObject> create() {
        return new BlobComparator();
    }
}
