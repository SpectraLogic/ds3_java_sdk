import org.gradle.api.publish.maven.MavenPublication

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
    `maven-publish`
    signing
}

publishing {
    repositories {
        maven {
            name = "internal"
            val releasesRepoUrl = "https://artifacts.eng.sldomain.com/repository/spectra-releases/"
            val snapshotsRepoUrl = "https://artifacts.eng.sldomain.com/repository/spectra-snapshots/"
            url = uri(if (extra["isReleaseVersion"] as Boolean) releasesRepoUrl else snapshotsRepoUrl)
            credentials {
                username = extra.has("artifactsUsername").let {
                    if (it) extra.get("artifactsUsername") as String else null
                }
                password = extra.has("artifactsPassword").let {
                    if (it) extra.get("artifactsPassword") as String else null
                }
            }
        }
        maven {
            name = "OSSRH"
            val releasesOssrhRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsOssrhRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = uri(if (extra["isReleaseVersion"] as Boolean) releasesOssrhRepoUrl else snapshotsOssrhRepoUrl)
            credentials {
                username = extra.has("ossrhUsername").let {
                    if (it) extra.get("ossrhUsername") as String else null
                }
                password = extra.has("ossrhPassword").let {
                    if (it) extra.get("ossrhPassword") as String else null
                }
            }
        }
    }
}

tasks.register("publishToInternalRepository") {
    group = "publishing"
    description = "Publishes all Maven publications to the internal Maven repository."
    dependsOn(tasks.withType<PublishToMavenRepository>().matching {
        it.repository == publishing.repositories["internal"]
    })
}

tasks.register("publishToSonatypeOSSRH") {
    group = "publishing"
    description = "Publishes all Maven publications to Sonatype's OSSRH repository."
    dependsOn(tasks.withType<PublishToMavenRepository>().matching {
        it.repository == publishing.repositories["OSSRH"]
    })
}

val augmentPom = tasks.register("augmentPom") {
    publishing.publications.filterIsInstance<MavenPublication>().forEach { pub ->
        pub.pom {
            name.set("${project.group}:${project.name}")
            url.set("https://github.com/SpectraLogic/ds3_java_sdk")
            description.set("${project.description}")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    name.set("Spectra Logic Developers")
                    email.set("developer@spectralogic.com")
                    organization.set("Spectra Logic")
                    organizationUrl.set("https://spectralogic.com/")
                }
            }
            scm {
                connection.set("scm:git:https://github.com/SpectraLogic/ds3_java_sdk.git")
                developerConnection.set("scm:git:https://github.com/SpectraLogic/ds3_java_sdk.git")
                url.set("https://github.com/SpectraLogic/ds3_java_sdk")
            }
        }
    }
}

tasks.withType<GenerateMavenPom>().configureEach {
    dependsOn(augmentPom)
}

tasks.withType<Sign>().configureEach {
    onlyIf("isReleaseVersion is set") { project.extra["isReleaseVersion"] as Boolean }
}
