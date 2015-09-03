package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

public class DeleteFolderRequest extends AbstractRequest {

    private final String folderName;
    private final String bucketName;

    public DeleteFolderRequest(final String bucketName, final String folderName) {
        this.bucketName = bucketName;
        this.folderName = folderName;
        getQueryParams().put("bucketId", this.bucketName);
        getQueryParams().put("recursive", null);
    }

    public String getBucket() { return this.bucketName; }

    public String getFolder() { return this.folderName; }

    @Override
    public String getPath() { return "/_rest_/folder/" + this.folderName; }

    @Override
    public HttpVerb getVerb() { return HttpVerb.DELETE; }
}
