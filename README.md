ds3_java_sdk
============

[![Build Status](https://travis-ci.org/SpectraLogic/ds3_java_sdk.svg)](https://travis-ci.org/SpectraLogic/ds3_java_sdk)
<a href="https://scan.coverity.com/projects/4624">
  <img alt="Coverity Scan Build Status"
       src="https://scan.coverity.com/projects/4624/badge.svg"/>
</a>
[ ![Download](https://api.bintray.com/packages/spectralogic/ds3/ds3_java_sdk/images/download.svg) ](https://bintray.com/spectralogic/ds3/ds3_java_sdk/_latestVersion)

## Contact Us

Join us at our [Google Groups](https://groups.google.com/d/forum/spectralogicds3-sdks) forum to ask questions, or see frequently asked questions.

## Setup

See our [Setup Guide](./SETUP.md) which explains how to setup Eclipse with all the dependencies that you will need to build the sdk from source.

If using Intellij, simply import the project as a Gradle project.

## Install

To install the latest `ds3_java_sdk` either download the latest release jar file from the [Releases](../../releases) page or clone the repository with `git clone https://github.com/SpectraLogic/ds3_java_sdk.git`, cd to `ds3_java_sdk` and run `./gradlew clean ds3-sdk:install` to install the sdk into your local maven repository.

The SDK can also be included directly into a Maven or Gradle build. There is also a fatjar version that you can optionally use with the `all` classifier, take a look at the commented out code in the examples below.  To include the SDK  into maven add the following to the `pom.xml` file:

```xml

<project>
  ...
  <repositories>
    <repository>
      <id>Spectra-Github</id>
      <url>http://dl.bintray.com/spectralogic/ds3</url>
    </repository>
  </repositories>
  ...
    <dependencies>
      ...
      <dependency>
        <groupId>com.spectralogic.ds3</groupId>
        <artifactId>ds3-sdk</artifactId>
        <version>1.2.0-RC2</version>
        <!-- <classifier>all</classifier> -->
      </dependency>
    ...  
    </dependencies>
</project>

```

To include the sdk into Gradle include the following in the `build.gradle` file:

```groovy

repositories {
    ...
    maven {
        url 'http://dl.bintray.com/spectralogic/ds3'
    }
    ...
}

dependencies {
    ...
    compile 'com.spectralogic.ds3:ds3-sdk:1.2.0-RC2'
    // compile 'com.spectralogic.ds3:ds3-sdk:1.2.0-RC2:all'
    ...
}

```
## Javadoc

The latest javadoc is located at [http://spectralogic.github.io/ds3_java_sdkj/javadoc/v1.2.0-RC2/](http://spectralogic.github.io/ds3_java_sdk/javadoc/v1.2.0-RC2/)

## Examples

All the examples are listed in the [ds3-sdk-samples](ds3-sdk-samples/src/main/java/com/spectralogic/ds3client/samples/) module.

## Tests

In addition to unit tests in the main `ds3-sdk` module, there are additional integration tests in the `ds3-integration` module.  Please see the integration [README](ds3-sdk-integration/README.md) for details on running the tests.  To just run the SDK's unit tests use:

    ./gradlew clean ds3-sdk:test
