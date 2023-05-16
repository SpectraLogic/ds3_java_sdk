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
    alias(libs.plugins.shadowPlugin)
}

description = "This module holds the classes for converting file system " +
    "metadata to BlackPearl object store metadata so you can preserve and " +
    "restore file times and OS specific metadata."

dependencies {
    api(project(":ds3-interfaces"))
    api(project(":ds3-utils"))

    implementation(libs.guava)
    implementation(libs.jna)
    implementation(libs.jnaPlatform)
    implementation(libs.slf4jApi)

    testImplementation(libs.commonsIo)
    testImplementation(libs.commonsLang3)
    testImplementation(libs.junit)
    testImplementation(libs.junitJupiterApi)
    testImplementation(libs.httpclient)

    testRuntimeOnly(libs.junitVintageEngine)
    testRuntimeOnly(libs.slf4jSimple)
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    relocate("org.apache", "ds3metafatjar.org.apache")
    relocate("com.google", "ds3metafatjar.com.google")
    relocate("com.sun.jna", "ds3metafatjar.net.java.dev.jna")
    relocate("org.jetbrains", "ds3metafatjar.org.jetbrains")
    relocate("org.intellij", "ds3metafatjar.org.intellij")
    relocate("org.codehaus", "ds3metafatjar.org.codehaus")
    dependencies {
        exclude(project(":ds3-interfaces")) // this is being excluded since it must be used with the sdk, which already has this dependency included
        exclude(project(":ds3-utils")) // this is being excluded since it must be used with the sdk, which already has this dependency included
        exclude(dependency(libs.slf4jApi.get().toString()))
        exclude(dependency(libs.slf4jSimple.get().toString()))
        exclude(dependency(libs.hamcrest.get().toString()))
        exclude(dependency(libs.mockitoCore.get().toString()))
        exclude(dependency(libs.junit.get().toString()))
        exclude(dependency(libs.junitJupiterApi.get().toString()))
        exclude(dependency(libs.commonsLang3.get().toString()))
    }
    dependsOn(tasks.jar)
}
