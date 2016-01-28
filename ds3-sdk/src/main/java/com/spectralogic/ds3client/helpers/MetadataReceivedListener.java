package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.networking.Metadata;

public interface MetadataReceivedListener {
    void metadataReceived(final String filename, final Metadata metdata);
}
