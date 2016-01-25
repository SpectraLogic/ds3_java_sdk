package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.models.bulk.Ds3Object;

import java.nio.channels.ByteChannel;

public interface ChecksumFunction {
    String compute(final Ds3Object obj, final ByteChannel channel);
}
