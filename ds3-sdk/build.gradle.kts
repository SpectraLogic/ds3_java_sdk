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

plugins {
    `ds3-java-sdk-library-convention`
    alias(libs.plugins.shadowPlugin)
    alias(libs.plugins.gitVersionPlugin)
}

description = "The BlackPearl SDK module holds the classes used to " +
    "communicate with the BlackPearl dataport."

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
    // NOTE: do not add org.codehaus.woodstox:woodstox-core-asl back here.
    //
    // jackson-dataformat-xml already brings com.fasterxml.woodstox:woodstox-core with a
    // matching stax2-api, and both Woodstox artifacts publish classes in com.ctc.wstx.*.
    // Declaring both put two Woodstox generations on the classpath: shadowJar merged them
    // (the ASL 4.4.1 classes won), while stax2-api resolved UP to 4.2.1 because the
    // fasterxml woodstox requires it. ASL 4.4.1 is compiled against stax2-api 3.x, so
    // CompactStartElement.getAttributes() calls
    //     EmptyIterator.getInstance():EmptyIterator
    // which does not exist in stax2-api 4.2.1 (it returns Iterator<T> there). Every
    // consumer of the StAX *event* API then died with NoSuchMethodError -- notably the
    // AWS SDK's XmlDomParser, which broke S3 target registration in BlackPearl. Jackson
    // itself uses the streaming API, so the SDK's own tests never noticed.

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
