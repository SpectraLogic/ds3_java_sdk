package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.io.InputStream;

public interface PutObject {
    public String getKey();
    public long getSize();
    public InputStream getContents() throws IOException;
}
