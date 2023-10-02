/*
 * ****************************************************************************
 *   Copyright 2023 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */

rootProject.name = "buildSrc"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        versionCatalogs {
            create("libs") {
                from(files("../libs.versions.toml"))
            }
        }
    }
}