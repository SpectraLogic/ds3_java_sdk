package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.io.InputStream;

public interface ObjectGetter {
    public void writeContents(String key, InputStream contents) throws IOException;
}
