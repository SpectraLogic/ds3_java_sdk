package com.spectralogic.ds3client;

import com.spectralogic.ds3client.networking.Headers;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class HeadersImpl_Test {

    @Test
    public void findHeader() {
        final Headers headers = genHeaders(new BasicHeader("Content-MD5", "Q2hlY2sgSW50ZWdyaXR5IQ=="));
        final String value = headers.get("Content-MD5");
        assertThat(value, is("Q2hlY2sgSW50ZWdyaXR5IQ=="));
    }

    @Test
    public void unknownHeader() {
        final Headers headers = genHeaders();
        final String value = headers.get("Content-MD5");
        assertNull(value);
    }

    public Headers genHeaders(final Header... headers) {
        return new HeadersImpl(headers);
    }
}
