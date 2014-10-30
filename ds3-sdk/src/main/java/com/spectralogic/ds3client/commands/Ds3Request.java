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

package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.models.Checksum;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.util.Map;

public interface Ds3Request {

    public String getPath();
    public HttpVerb getVerb();

    public ContentType getContentType();

    public InputStream getStream();

    public long getSize();

    public Checksum getChecksum();

    public Map<String, String> getQueryParams();

    public Map<String, String> getHeaders();
}
