/*
 * ******************************************************************************
 *   Copyright 2002 Spectra Logic Corporation. All Rights Reserved.
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

plugins {
    `ds3-java-sdk-library-convention`
}

description = "This module holds classes for utilities used to manage files and " +
    "metadata."

dependencies {
    api(project(":ds3-interfaces"))

    implementation(libs.commonsCodec)
    implementation(libs.commonsIo)
    implementation(libs.guava)

    testImplementation(libs.junit)
    testImplementation(libs.junitJupiterApi)

    testRuntimeOnly(libs.junitVintageEngine)
    testRuntimeOnly(libs.slf4jSimple)
}
