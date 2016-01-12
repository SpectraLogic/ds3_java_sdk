package com.spectralogic.ds3client.helpers.channels;

import com.spectralogic.ds3client.models.BlobApiBean;

import java.util.Comparator;

/**
 * Used to sort BlobApiBean (Blobs).  We only care about the starting
 * offset, so that is what is used to compare.
 */
public class BlobComparator implements Comparator<BlobApiBean> {
    @Override
    public int compare(final BlobApiBean o1, final BlobApiBean o2) {
        return Long.compare(o1.getOffset(), o2.getOffset());
    }

    public static Comparator<BlobApiBean> create() {
        return new BlobComparator();
    }
}
