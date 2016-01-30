package com.spectralogic.ds3client.helpers;


import com.spectralogic.ds3client.models.Checksum;
import com.spectralogic.ds3client.models.bulk.BulkObject;

public interface ChecksumListener {
    void value(final BulkObject obj, final Checksum.Type type, final String checksum);
}
