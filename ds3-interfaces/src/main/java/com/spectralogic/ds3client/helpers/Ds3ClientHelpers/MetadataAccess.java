package com.spectralogic.ds3client.helpers.Ds3ClientHelpers;

import java.util.Map;

public interface MetadataAccess {
    Map<String, String> getMetadataValue(final String filename);
}
