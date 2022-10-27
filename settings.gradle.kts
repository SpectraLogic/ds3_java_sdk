/*
 * ******************************************************************************
 *   Copyright 2022 Spectra Logic Corporation. All Rights Reserved.
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

rootProject.name = "ds3-java-sdk"

include("ds3-interfaces")
include("ds3-utils")
include("ds3-sdk")
include("ds3-metadata")
include("ds3-sdk-samples")
include("ds3-sdk-integration")
include("ds3-bom")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.6.10")
            version("commons-codec", "1.15")
            version("commons-io", "2.11.0")
            version("commons-lang", "3.12.0")
            version("findbugs", "3.0.1")
            version("git-version-plugin", "0.12.2") // stuck on this version until we move from building with java 8
            version("guava", "31.1-jre")
            version("hamcrest", "2.2")
            version("httpclient", "4.5.13")
            version("jackson", "2.13.4.20221013")
            version("jna", "5.12.1")
            version("junit", "4.13.2")
            version("junit-jupiter", "5.9.0")
            version("mockito", "4.7.0")
            version("shadow-plugin", "7.1.2")
            version("slf4j", "1.7.36")
            version("woodstox", "4.4.1")

            library("commonsCodec", "commons-codec", "commons-codec").versionRef("commons-codec")
            library("commonsIo", "commons-io","commons-io").versionRef("commons-io")
            library("findbugs", "com.google.code.findbugs", "annotations").versionRef("findbugs")
            library("guava", "com.google.guava", "guava").versionRef("guava")
            library("httpclient", "org.apache.httpcomponents", "httpclient").versionRef("httpclient")
            library("jacksonBom", "com.fasterxml.jackson", "jackson-bom").versionRef("jackson")
            library("jacksonDatatypeJdk8", "com.fasterxml.jackson.datatype", "jackson-datatype-jdk8").withoutVersion()
            library("jacksonDataformatXml", "com.fasterxml.jackson.dataformat", "jackson-dataformat-xml").withoutVersion()
            library("jna", "net.java.dev.jna", "jna").versionRef("jna")
            library("jnaPlatform", "net.java.dev.jna", "jna-platform").versionRef("jna")
            library("kotlinStdLib", "org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin")
            library("slf4jApi", "org.slf4j", "slf4j-api").versionRef("slf4j")
            library("woodstoxCoreAsl", "org.codehaus.woodstox", "woodstox-core-asl").versionRef("woodstox")

            // test only libraries
            library("commonsLang", "org.apache.commons","commons-lang3").versionRef("commons-lang")
            library("hamcrest", "org.hamcrest", "hamcrest").versionRef("hamcrest")
            library("junit", "junit", "junit").versionRef("junit")
            library("junitJupiterApi", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit-jupiter")
            library("junitVintageEngine", "org.junit.vintage", "junit-vintage-engine").versionRef("junit-jupiter")
            library("mockitoBom", "org.mockito", "mockito-bom").versionRef("mockito")
            library("mockitoCore", "org.mockito", "mockito-core").withoutVersion()
            library("slf4jSimple", "org.slf4j", "slf4j-simple").versionRef("slf4j")

            // gradle plugins
            plugin("gitVersionPlugin", "com.palantir.git-version").versionRef("git-version-plugin")
            plugin("kotlinJvmPlugin", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("shadowPlugin", "com.github.johnrengelman.shadow").versionRef("shadow-plugin")
            // Looking for the owasp dependency check plug-in? It lives in buildSrc/build.gradle.kts
        }
    }
}
