package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;
import org.apache.http.entity.ContentType;

public class GetObjectRequest extends AbstractRequest{

    private final String bucketName;
    private final String objectName;

    public GetObjectRequest(final String bucketName, final String objectName) {
       this.bucketName = bucketName;
        this.objectName = objectName;
    }

    @Override
    public String getPath() {
        return "/"+ bucketName + "/" + objectName;
    }


    @Override
    public ContentType getContentType() {
        return ContentType.APPLICATION_OCTET_STREAM;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }
}
