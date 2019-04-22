/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.serializer;

/**
 * Used to annotate Ds3Object attributes to specify which attributes are
 * unique to bulk get vs bulk put request payloads. Note that parameters
 * with no annotation will be marshaled into both get and put payloads.
 * These are used with the JsonView annotation.
 */
public class Views {
    /** Denotes parameters within Ds3Object which are unique to get object request payloads */
    public static class GetObject { }

    /** Denotes parameters within Ds3Object which are unique to get object request payloads */
    public static class PutObject { }
}