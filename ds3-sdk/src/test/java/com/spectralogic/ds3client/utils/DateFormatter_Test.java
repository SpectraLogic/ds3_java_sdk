/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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


import org.junit.Test;

import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DateFormatter_Test {

    @Test
    public void formatDate() {
        final Date date = new Date(1390414308132L);

        assertThat(DateFormatter.dateToRfc822(date), is("Wed, 22 Jan 2014 18:11:48 +0000"));
    }

    @Test
    public void formatDateMandarin() {
        final Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.CHINESE);
        final Date date = new Date(1390414308132L);

        assertThat(DateFormatter.dateToRfc822(date), is("Wed, 22 Jan 2014 18:11:48 +0000"));
        Locale.setDefault(defaultLocale);
    }
}
