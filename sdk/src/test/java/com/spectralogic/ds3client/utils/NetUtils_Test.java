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

package com.spectralogic.ds3client.utils;

import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.ConnectionFixture;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.NetUtils;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class NetUtils_Test {

    @Test
    public void pathBuilderValidPaths() {
        final String result = NetUtils.buildPath("/basePath", "file.xml");
        assertThat(result,is("/basePath/file.xml"));
    }

    @Test
    public void pathBothNull() {
        final String result = NetUtils.buildPath(null,null);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void pathFirstNull() {
        final String result = NetUtils.buildPath(null, "file.xml");
        assertThat(result.isEmpty(), is(false));
        assertThat(result, is("file.xml"));
    }

    @Test
    public void pathSecondNull() {
        final String result = NetUtils.buildPath("/basePath", null);
        assertThat(result, is("/basePath"));
    }

    @Test
    public void pathNoSlashes() {
        final String result = NetUtils.buildPath("basePath", "file.xml");
        assertThat(result, is("/basePath/file.xml"));
    }

    @Test
    public void pathFirstEndsWithSlash() {
        final String result = NetUtils.buildPath("/basePath/", "file.xml");
        assertThat(result, is("/basePath/file.xml"));
    }

    @Test
    public void pathMultipleSlashes() {
        final String result = NetUtils.buildPath("/basePath/", "/file.xml");
        assertThat(result, is("/basePath/file.xml"));
    }

    @Test
    public void buildPathWithoutSlash() throws MalformedURLException {
        final URL result = NetUtils.buildUrl(ConnectionFixture.getConnection(), "path");
        assertThat(result.getPath(), is("/path"));
    }

    @Test
    public void buildBulkPutCommand() throws MalformedURLException {
        final URL result = NetUtils.buildBucketPath("testBucket", ConnectionFixture.getConnection(), BulkCommand.PUT);
        assertThat(result.getPath(), is("/_rest_/buckets/testBucket"));
        assertThat(result.toString(), is("http://localhost:8080/_rest_/buckets/testBucket?operation=start_bulk_put"));
    }

    @Test
    public void buildBulkGetCommand() throws MalformedURLException {
        final URL result = NetUtils.buildBucketPath("testBucket1", ConnectionFixture.getConnection(), BulkCommand.GET);
        assertThat(result.getPath(), is("/_rest_/buckets/testBucket1"));
        assertThat(result.toString(), is("http://localhost:8080/_rest_/buckets/testBucket1?operation=start_bulk_get"));
    }

    @Test
    public void buildUrlWithMultipleQueryParams() throws MalformedURLException {
        final Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("var", "2");
        queryParams.put("foo", "bar");
        final URL result = NetUtils.buildUrl(ConnectionFixture.getConnection(), "path", queryParams);
        assertThat(result.toString(), is("http://localhost:8080/path?foo=bar&var=2"));
    }


    @Test
    public void buildHostField() {
        final String result = NetUtils.buildHostField(ConnectionFixture.getConnection());
        assertThat(result, is("localhost:8080"));
    }

    @Test
    public void singleQueryParams() {
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("foo","bar");

        final String result = NetUtils.buildQueryString(queryParams);
        assertThat(result, is("foo=bar"));
    }

    @Test
    public void twoQueryParams() {
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("foo","bar");
        queryParams.put("char", "far");

        final String result = NetUtils.buildQueryString(queryParams);
        assertThat(result, is("char=far&foo=bar"));

    }

    @Test
    public void escapeSpacesInPath() throws MalformedURLException {
        final URL result = NetUtils.buildUrl(ConnectionFixture.getConnection(), "path with space", null);
        assertThat(result.toString(), is("http://localhost:8080/path%20with%20space"));
    }

    @Test
    public void escapeSpacesInQueryParam() throws MalformedURLException {
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("foo","bar val");
        final URL result = NetUtils.buildUrl(ConnectionFixture.getConnection(), "path", queryParams);
        assertThat(result.toString(), is("http://localhost:8080/path?foo=bar%20val"));
    }

    @Test
    public void getPortBack() throws MalformedURLException {
        final URL url = new URL("http://localhost:8080/path");
        int port = NetUtils.getPort(url);
        assertTrue(port == 8080);
    }

    @Test
    public void getHttpsDefaultPort() throws MalformedURLException {
        final URL url = new URL("https://localhost/path");
        int port = NetUtils.getPort(url);
        assertTrue(port == 443);
    }

    @Test
    public void getHttpDefaultPort() throws MalformedURLException {
        final URL url = new URL("http://localhost/path");
        int port = NetUtils.getPort(url);
        assertTrue(port == 80);
    }
}
