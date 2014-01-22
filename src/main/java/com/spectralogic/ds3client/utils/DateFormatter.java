package com.spectralogic.ds3client.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    final static private String RFC822FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";
    /**
     * Returns a RFC-882 formatted string with the current time.
     * @return
     */
    public static String dateToRfc882() {
        return dateToRfc882(new Date());
    }

    public static String dateToRfc882(final Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat(RFC822FORMAT);
        return sdf.format(date);
    }

}
