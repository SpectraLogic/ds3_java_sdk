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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("0.7.0")
}

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
            from(files("libs.versions.toml"))
        }
    }
}
