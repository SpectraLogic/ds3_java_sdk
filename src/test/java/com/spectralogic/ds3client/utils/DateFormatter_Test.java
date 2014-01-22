package com.spectralogic.ds3client.utils;


import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DateFormatter_Test {

    @Test
    public void formatDate() {
        final Date date = new Date(1390414308132L);

        assertThat(DateFormatter.dateToRfc882(date), is("Wed, 22 Jan 2014 11:11:48 -0700"));
    }
}
