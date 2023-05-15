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
    `ds3-java-sdk-internal-convention`
}

dependencies {
    implementation(project(":ds3-utils"))
    implementation(project(":ds3-sdk"))

    implementation(libs.commonsIo)
    implementation(libs.guava)
    implementation(libs.slf4jApi)
    implementation(libs.hamcrest)
    implementation(libs.junit)
    implementation(libs.junitJupiterApi)

    testImplementation(project(":ds3-metadata"))

    testImplementation(libs.httpclient)
    testImplementation(libs.commonsLang3)

    testRuntimeOnly(libs.junitVintageEngine)
}

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val integrationTest = task<Test>("integrationTest") {
    description = "Run integration tests."
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    shouldRunAfter("test")
}

configurations["integrationTestImplementation"].extendsFrom(configurations.testImplementation.get())
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())

tasks.check { dependsOn(integrationTest) }
