/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.delete.Delete;
import com.spectralogic.ds3client.models.delete.DeleteObject;
import com.spectralogic.ds3client.serializer.XmlOutput;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;

public class DeleteObjectsRequest extends AbstractRequest {

    // Variables
    private final List<DeleteObject> objects;
    
    private final String bucketName;
    private boolean quiet = false;
    private long size;

    // Constructor
    
    
    public DeleteObjectsRequest(final String bucketName, final List<String> objects) {
        this.bucketName = bucketName;
        
        this.getQueryParams().put("delete", null);
        this.objects = namesToDeleteObjects(objects);
    }

    
    public DeleteObjectsRequest(final String bucketName, final Iterable<Contents> objects) {
        this.bucketName = bucketName;
        
        this.getQueryParams().put("delete", null);
        this.objects = contentsToDeleteObjects(objects);
    }

    private static List<DeleteObject> contentsToDeleteObjects(final Iterable<Contents> objects) {
        final List<DeleteObject> objectsToDelete = new ArrayList<>();
        for (final Contents obj : objects) {
            objectsToDelete.add(new DeleteObject(obj.getKey(), obj.getVersionId()));
        }
        return objectsToDelete;
    }

    private static List<DeleteObject> namesToDeleteObjects(final Iterable<String> objNames) {
        final List<DeleteObject> objectsToDelete = new ArrayList<>();
        for (final String objName : objNames) {
            objectsToDelete.add(new DeleteObject(objName));
        }
        return objectsToDelete;
    }


    public DeleteObjectsRequest withQuiet(final boolean quiet) {
        this.quiet = quiet;
        return this;
    }

    @Override
    public InputStream getStream() {

        final Delete delete = new Delete();
        delete.setQuiet(quiet);
        delete.setDeleteObjectList(objects);

        final String xmlOutput = XmlOutput.toXml(delete);
        final byte[] stringBytes = xmlOutput.getBytes(StandardCharsets.UTF_8);
        this.size = stringBytes.length;

        return new ByteArrayInputStream(stringBytes);
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/" + this.bucketName;
    }
    public List<DeleteObject> getObjects() {
        return this.objects;
    }


    public boolean getQuiet() {
        return this.quiet;
    }


    
    public String getBucketName() {
        return this.bucketName;
    }


    @Override
    public long getSize() {
        return this.size;
    }

}