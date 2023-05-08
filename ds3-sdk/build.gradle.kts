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

import java.time.Instant
// bug in IntelliJ in which `libs` shows up as not being accessible
// see https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `ds3-java-sdk-library-convention`
    alias(libs.plugins.shadowPlugin)
    alias(libs.plugins.gitVersionPlugin)
}

dependencies {
    implementation(platform(libs.jacksonBom))

    api(project(":ds3-interfaces"))
    api(project(":ds3-utils"))

    implementation(libs.kotlinStdLib)
    implementation(libs.commonsIo)
    implementation(libs.guava)
    implementation(libs.httpclient)
    implementation(libs.jacksonDatatypeJdk8)
    implementation(libs.jacksonDataformatXml)
    implementation(libs.slf4jApi)
    implementation(libs.findbugs)
    implementation(libs.woodstoxCoreAsl)

    testImplementation(platform(libs.mockitoBom))

    testImplementation(libs.junit)
    testImplementation(libs.junitJupiterApi)
    testImplementation(libs.hamcrest)
    testImplementation(libs.mockitoCore)

    testRuntimeOnly(libs.junitVintageEngine)
    testRuntimeOnly(libs.slf4jSimple)
}

val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra

val genConfigProperties by tasks.registering(WriteProperties::class,) {
    group = BasePlugin.BUILD_GROUP
    description = "Create properties file with build information."
    val getProdBuild = { value: String? -> value ?: "false" }
    val gitDetails = versionDetails()
    property("productionBuild", getProdBuild(System.getenv("productionBuild")))
    property("version", version.toString())
    property("build.date", Instant.now())
    property("git.commitHash", gitDetails.gitHash)
    outputFile = file("${buildDir}/ds3_sdk.properties")
    encoding = "UTF-8"
}

tasks.jar {
    dependsOn(genConfigProperties)
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    relocate("com.ctc", "ds3fatjar.com.ctc")
    relocate("com.fasterxml", "ds3fatjar.com.fasterxml")
    relocate("com.google", "ds3fatjar.com.google")
    relocate("edu.umd", "ds3fatjar.edu.emd")
    relocate("kotlin", "ds3fatjar.kotlin")
    relocate("net.jcip", "ds3fatjar.net.jcip")
    relocate("org.apache", "ds3fatjar.org.apache")
    relocate("org.codehaus", "ds3fatjar.org.codehaus")
    relocate("org.intellij", "ds3fatjar.org.intellij")
    relocate("org.jetbrains", "ds3fatjar.org.jetbrains")
    dependencies {
        exclude(dependency(libs.slf4jApi.get().toString()))
    }
    mergeServiceFiles()
    dependsOn(tasks.jar)
}

val allProjectJars by tasks.registering(Zip::class) {
    group = "distribution"
    description = "Create zip file containing all SDK jars and its dependencies."
    archiveFileName.set(project.name + "-" + project.version + ".zip")
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
    from(configurations.runtimeClasspath)
    dependsOn(tasks.jar)
}
