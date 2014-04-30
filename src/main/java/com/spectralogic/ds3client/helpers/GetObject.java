package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.io.InputStream;

public interface GetObject extends ObjectInfo {
    public void writeContent(final InputStream content) throws IOException;
}
