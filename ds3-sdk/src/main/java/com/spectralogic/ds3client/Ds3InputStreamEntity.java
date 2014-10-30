/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client;

import com.spectralogic.ds3client.utils.IOUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Ds3InputStreamEntity extends InputStreamEntity {

    private int bufferSize = 1024 * 1024;

    public Ds3InputStreamEntity(final InputStream inStream, final long length, final ContentType contentType) {
        super(inStream, length, contentType);
    }

    public void setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public long getBufferSize() {
        return bufferSize;
    }

    @Override
    public void writeTo(final OutputStream outStream) throws IOException {
        IOUtils.copy(this.getContent(), outStream, bufferSize);
    }
}
