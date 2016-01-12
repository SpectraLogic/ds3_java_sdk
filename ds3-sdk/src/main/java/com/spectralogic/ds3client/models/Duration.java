/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Duration {

    // Variables
    @JsonProperty("ElapsedMillis")
    private long elapsedMillis;

    @JsonProperty("ElapsedMinutes")
    private int elapsedMinutes;

    @JsonProperty("ElapsedNanos")
    private long elapsedNanos;

    @JsonProperty("ElapsedSeconds")
    private int elapsedSeconds;

    // Constructor
    public Duration(final long elapsedMillis, final int elapsedMinutes, final long elapsedNanos, final int elapsedSeconds) {
        this.elapsedMillis = elapsedMillis;
        this.elapsedMinutes = elapsedMinutes;
        this.elapsedNanos = elapsedNanos;
        this.elapsedSeconds = elapsedSeconds;
    }

    // Getters and Setters
    
    public long getElapsedMillis() {
        return this.elapsedMillis;
    }

    public void setElapsedMillis(final long elapsedMillis) {
        this.elapsedMillis = elapsedMillis;
    }


    public int getElapsedMinutes() {
        return this.elapsedMinutes;
    }

    public void setElapsedMinutes(final int elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
    }


    public long getElapsedNanos() {
        return this.elapsedNanos;
    }

    public void setElapsedNanos(final long elapsedNanos) {
        this.elapsedNanos = elapsedNanos;
    }


    public int getElapsedSeconds() {
        return this.elapsedSeconds;
    }

    public void setElapsedSeconds(final int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

}