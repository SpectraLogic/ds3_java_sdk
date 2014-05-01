package com.spectralogic.ds3client.helpers;

import java.io.InputStream;

public interface ObjectPutter {
    public InputStream getContent(String key);
}
