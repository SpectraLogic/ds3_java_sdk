#!/bin/bash
cd /opt
git clone https://github.com/SpectraLogic/ds3_java_sdk.git

cd ds3_java_sdk

./gradlew test
