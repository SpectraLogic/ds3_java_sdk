package com.spectralogic.ds3client.networking;

import java.util.List;
import java.util.Set;

public interface Metadata {
    /**
     * If the metadata value does not exist, then an empty list is returned.
     */
    List<String> get(final String name);
    Set<String> keys();
}
