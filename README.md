ds3_java_sdk
============

[![Latest release](https://img.shields.io/github/release/SpectraLogic/ds3_java_sdk.svg)](https://github.com/SpectraLogic/ds3_java_sdk/releases/latest)
[![Maven Central](https://img.shields.io/maven-central/v/com.spectralogic.ds3/ds3-bom.svg)](https://search.maven.org/artifact/com.spectralogic.ds3/ds3-bom)
[![Apache V2 License](http://img.shields.io/badge/license-Apache%20V2-blue.svg)](https://github.com/SpectraLogic/ds3_java_sdk/blob/master/LICENSE.md)
[![Open Hub project report for ds3_java_sdk](https://www.openhub.net/p/ds3_java_sdk/widgets/project_thin_badge.gif)](https://www.openhub.net/p/ds3_java_sdk?ref=sample)

## Contact Us

Join us at our [Google Groups](https://groups.google.com/d/forum/spectralogicds3-sdks) forum to ask questions, or see frequently asked questions.

## Setup

See our [Setup Guide](./SETUP.md) which explains how to setup Eclipse with all the dependencies that you will need to build the sdk from source.

If using Intellij, simply import the project as a Gradle project.

## Install

To install the latest `ds3_java_sdk` either download the latest release jar file from the [Releases](../../releases) page or clone the repository with `git clone https://github.com/SpectraLogic/ds3_java_sdk.git`, cd to `ds3_java_sdk` and run `./gradlew clean ds3-sdk:install` to install the sdk into your local maven repository.  It is compatible with Java 8 and is additionally tested against Java 11 and 17.

Starting with the 5.6.0 release, the SDK is available on [Maven Central](https://central.sonatype.com/namespace/com.spectralogic.ds3) and can be included directly into a Maven or Gradle build. There is also a fatjar version that you can optionally use with the `all` classifier, take a look at the commented out code in the examples below.  To include the SDK  into maven add the following to the `pom.xml` file:

```xml
<project>
  ...
    <dependencies>
      ...
      <dependency>
	      <groupId>com.spectralogic.ds3</groupId>
	      <artifactId>ds3-sdk</artifactId>
	      <version>Tag</version>
        <!-- <classifier>all</classifier> -->
	</dependency>
    ...
    </dependencies>
</project>
```

To include the sdk into Gradle include the following in the `build.gradle.kts` file:

```kotlin
dependencies {
    implementation(platform("com.spectralogic.ds3:ds3-bom:Tag"))
    implementation("com.spectralogic.ds3:ds3-sdk")
    //implementation("com.spectralogic.ds3:ds3-sdk::all")
}
```

or in the `build.gradle` file:

```groovy

dependencies {
    implementation platform('com.spectralogic.ds3:ds3-bom:Tag')
    implementation 'com.spectralogic.ds3:ds3-sdk'
    //implementation 'com.spectralogic.ds3:ds3-sdk::all'
}
```

### Previous versions

Versions prior to 5.6.0 are available on [jitpack.io](https://jitpack.io/#SpectraLogic/ds3_java_sdk) under different maven coordinates. Unfortunately, the 5.6.0 version on jitpack.io is a pre-release version and is missing important fixes, and it is recommended that the artifacts on Maven Central are used going forward. To access versions earlier than 5.6.0, please see the instructions at https://jitpack.io/#SpectraLogic/ds3_java_sdk.

## Contributing

If you would like to contribute to the source code, sign the [Contributors Agreement](https://developer.spectralogic.com/contributors-agreement/) and make sure that your source conforms to our [Java Style Guide](https://github.com/SpectraLogic/spectralogic.github.com/wiki/Java-Style-Guide).  For an overview of how we use Github, please review our [Github Workflow](https://github.com/SpectraLogic/spectralogic.github.com/wiki/Github-Workflow).

## Examples

All the examples are listed in the [ds3-sdk-samples](ds3-sdk-samples/src/main/java/com/spectralogic/ds3client/samples/) module.

## Logging

The `ds3_java_sdk` uses SLF4J for logging.  Because of this, a consumer of
the sdk can use many different logging implementations and so long as the
correct SLF4J bridge is installed, have the sdk logs appear.  For more
information on SLF4J and how to add different logging
bridges, please see [SLF4j.org](http://www.slf4j.org/manual.html).

## Tests

In addition to unit tests in the main `ds3-sdk` module, there are additional integration tests in the `ds3-integration` module.  Please see the integration [README](ds3-sdk-integration/README.md) for details on running the tests.  To just run the SDK's unit tests use:

    ./gradlew clean ds3-sdk:test

## Creating a New Release

Update the version of the SDK before creating a new release. The format is `<major>.<minor>.<patch>`, where the 
`<major>.<minor>` numbers must match the version of BP. The `<patch>` is an incrementing number that increments with 
each SDK release for a given major/minor release.

The version is specified in `libs.versions.toml`:

```
[versions]
#
# project version
#
ds3SdkVersion = "5.6.1"
```

Once released, increment the minor version and add the suffix "-SNAPSHOT" to prepare for the next development cycle.
