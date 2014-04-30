package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.models.Ds3Object;

interface ObjectInfo {
    public String getKey();
    public Ds3Object getObject();
}
