/*
 * ****************************************************************************
 *    Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.exceptions;

import org.apache.commons.io.output.StringBuilderWriter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AggregateException extends RuntimeException {

    private final List<Throwable> exceptions;

    public AggregateException(final Iterable<Throwable> exceptions) {
        super("One or more exceptions were aggregated together");
        this.exceptions = exceptionList(exceptions);
    }

    private static List<Throwable> exceptionList(final Iterable<Throwable> exceptions) {
        final List<Throwable> exceptionList = new ArrayList<>();
        for (final Throwable t : exceptions) {
            exceptionList.add(t);
        }
        return exceptionList;
    }

    public Iterable<Throwable> getExceptions() {
        return exceptions;
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("One or more exceptions were aggregated:");

        try (final StringBuilderWriter writer = new StringBuilderWriter(builder);
             final PrintWriter printWriter = new PrintWriter(writer)) {

            for (final Throwable t : exceptions) {
                printWriter.append("Exception: ").append(t.getMessage());
                t.printStackTrace(printWriter);
            }
        }

        return builder.toString();
    }
}
