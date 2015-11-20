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

package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.delete.Delete;
import com.spectralogic.ds3client.models.delete.DeleteObject;
import com.spectralogic.ds3client.serializer.XmlOutput;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DeleteMultipleObjectsRequest extends AbstractRequest {

    private final String bucketName;
    private final List<String> objects;

    private boolean quiet = false;

    private long size;

    public DeleteMultipleObjectsRequest(final String bucketName, final List<String> objects) {
        this.bucketName = bucketName;
        this.objects = objects;
        this.getQueryParams().put("delete", null);
    }

    public DeleteMultipleObjectsRequest(final String bucketName, final Iterable<Contents> objs) {
        this(bucketName, contentsToString(objs));
    }

    static private List<String> contentsToString(final Iterable<Contents> objs) {
        final List<String> objKeyList = new ArrayList<>();
        for (final Contents obj : objs) {
            objKeyList.add(obj.getKey());
        }
        return objKeyList;
    }

    public DeleteMultipleObjectsRequest withQuiet(final boolean quiet) {
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
        final byte[] stringBytes = xmlOutput.getBytes(Charset.forName("UTF-8"));
        this.size = stringBytes.length;

        return new ByteArrayInputStream(stringBytes);
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public String getPath() {
        return "/" + bucketName;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    public List<String> getObjects() {
        return objects;
    }
}
