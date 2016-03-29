/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;

public class DeleteObjectsRequest extends AbstractRequest {

    // Variables
    private final List<String> objects;
    
    private final String bucketName;

    private boolean rollBack;
    private boolean quiet = false;
    private long size;

    // Constructor
    
    public DeleteObjectsRequest(final String bucketName, final List<String> objects) {
        this.bucketName = bucketName;
        this.objects = objects;
        
        this.getQueryParams().put("delete", null);
    }

    public DeleteObjectsRequest(final String bucketName, final Iterable<Contents> objs) {
        this.bucketName = bucketName;
        
        this.getQueryParams().put("delete", null);
        this.objects = contentsToString(objs);
    }

    private static List<String> contentsToString(final Iterable<Contents> objs) {
        final List<String> objKeyList = new ArrayList<>();
        for (final Contents obj : objs) {
            objKeyList.add(obj.getKey());
        }
        return objKeyList;
    }

    public DeleteObjectsRequest withRollBack(final boolean rollBack) {
        this.rollBack = rollBack;
        if (this.rollBack) {
            this.getQueryParams().put("roll_back", null);
        } else {
            this.getQueryParams().remove("roll_back");
        }
        return this;
    }


    public DeleteObjectsRequest withQuiet(final boolean quiet) {
        this.quiet = quiet;
        return this;
    }

    @Override
    public InputStream getStream() {

        final Delete delete = new Delete();
        delete.setQuiet(quiet);
        final List<DeleteObject> deleteObjects = new ArrayList<>();

        for(final String objName : objects) {
            deleteObjects.add(new DeleteObject(objName));
        }

        delete.setDeleteObjectList(deleteObjects);

        final String xmlOutput = XmlOutput.toXml(delete);
        final byte[] stringBytes = xmlOutput.getBytes();
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
    public List<String> getObjects() {
        return this.objects;
    }


    public boolean getQuiet() {
        return this.quiet;
    }


    
    public String getBucketName() {
        return this.bucketName;
    }


    public boolean getRollBack() {
        return this.rollBack;
    }


    @Override
    public long getSize() {
        return this.size;
    }

}