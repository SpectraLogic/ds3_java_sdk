package com.spectralogic.ds3client;


import com.spectralogic.ds3client.fixtures.ConnectionFixture;
import com.spectralogic.ds3client.networking.NetUtils;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

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
        final URL result = NetUtils.buildUrl("path", ConnectionFixture.getConnection(), null);
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

}
