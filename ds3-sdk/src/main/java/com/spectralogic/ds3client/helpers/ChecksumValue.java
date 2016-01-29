package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.models.bulk.Ds3Object;

public interface ChecksumValue {
    void value(final Ds3Object obj, final String checksum);
}
