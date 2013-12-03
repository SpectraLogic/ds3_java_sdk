package com.spectralogic.ds3client;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class NetUtils_Test {

    @Test
    public void pathBuilderValidPaths() {
        final String result = NetUtils.buildPath("/basePath","file.xml");
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
}
