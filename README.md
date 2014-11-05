ds3_java_sdk
============

## Install

To install the latest ds3_java_sdk either download the latest release jar file from the [Releases](../../releases) page or clone the repository with `git clone https://github.com/SpectraLogic/ds3_java_sdk.git`, cd to `ds3_java_sdk` and run `./gradlew clean sdk:install` to install the sdk into your local maven repository.

The SDK can also be included directly into a Maven or Gradle build.  To include the SDK  into maven add the following to the `pom.xml` file:

```xml

<project>
  ...
  <repositories>
    <repository>
      <id>Spectra-Github</id>
      <url>https://spectralogic.github.io/java/repository</url>
    </repository>
  </repositories>
  ...
    <dependencies>
      ...
      <dependency>
        <groupId>com.spectralogic.ds3</groupId>
        <artifactId>ds3-sdk</artifactId>
        <version>0.7.6-SNAPSHOT</version>
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
        url 'https://spectralogic.github.io/java/repository'
    }
    ...
}

dependencies {
    ...
    compile 'com.spectralogic.ds3:ds3-sdk:0.7.6-SNAPSHOT'
    ...
}

```

## Tests

In addition to unit tests in the main `sdk` module, there are additional integration tests in the `integration` module.  Please see the integration [README](ds3-sdk-integration/README.md) for details on running the tests.  To just run the SDK's unit tests use:

    ./gradlew clean ds3-sdk:test

## Javadoc

The latest javadoc is located at [http://spectralogic.github.io/ds3_java_sdk/](http://spectralogic.github.io/ds3_java_sdk/)

## Examples

All the examples are listed in the [ds3-sdk-samples](ds3-sdk-samples/src/main/java/com/spectralogic/ds3client/samples/) module.
