/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.delete.Delete;
import com.spectralogic.ds3client.models.delete.DeleteObject;
import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.serializer.XmlOutput;

public class DeleteObjectsRequest extends AbstractRequest {

    // Variables
    private final Map< String, UUID > objects;
    
    private final String bucketName;

    private boolean replicate;

    private boolean quiet = false;
    private long size;

    // Constructor
    
    
    public DeleteObjectsRequest( final String bucketName, final Map< String, UUID > objects )
    {
        this.bucketName = bucketName;
        this.objects = objects;
        
        this.getQueryParams().put("delete", null);
    }

    
    public DeleteObjectsRequest(final String bucketName, final Iterable<Contents> objs) {
        this.bucketName = bucketName;

        this.getQueryParams().put("delete", null);
        this.objects = contentsToString(objs);
    }
    
    
    private static Map< String, UUID > contentsToString( final Iterable< Contents > objs )
    {
        final Map< String, UUID > objKeyList = new HashMap<>();
        for (final Contents obj : objs) {
            objKeyList.put( obj.getKey(), null );
        }
        return objKeyList;
    }

    public DeleteObjectsRequest withReplicate(final boolean replicate) {
        this.replicate = replicate;
        if (this.replicate) {
            this.getQueryParams().put("replicate", null);
        } else {
            this.getQueryParams().remove("replicate");
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
    
        for ( Map.Entry< String, UUID > entry : objects.entrySet() )
        {
            deleteObjects.add( new DeleteObject( entry.getKey(), entry.getValue() ) );
        }

        delete.setDeleteObjectList(deleteObjects);

        final String xmlOutput = XmlOutput.toXml(delete);
        final byte[] stringBytes = xmlOutput.getBytes(Charset.forName("UTF-8"));
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
    
    
    public Map< String, UUID > getObjects()
    {
        return this.objects;
    }


    public boolean getQuiet() {
        return this.quiet;
    }


    
    public String getBucketName() {
        return this.bucketName;
    }


    public boolean getReplicate() {
        return this.replicate;
    }


    @Override
    public long getSize() {
        return this.size;
    }

}