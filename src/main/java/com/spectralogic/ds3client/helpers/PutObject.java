package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.io.InputStream;

public interface PutObject extends ObjectInfo {
    public InputStream getContent() throws IOException;
}
