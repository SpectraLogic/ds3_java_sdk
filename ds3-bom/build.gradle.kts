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
    `ds3-java-sdk-version`
    `maven-publish`
    `java-platform`
}

description = "The ds3-bom generates a Bill of Materials (BOM) for the published DS3 SDK artifacts."

dependencies {
    constraints {
        api(project(":ds3-interfaces"))
        api(project(":ds3-utils"))
        api(project(":ds3-metadata"))
        api(project(":ds3-sdk"))
    }
}

publishing {
    publications {
        create<MavenPublication>("Ds3Bom") {
            from(components["javaPlatform"])
        }
    }
}
