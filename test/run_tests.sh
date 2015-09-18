#!/bin/bash
cd /opt
git clone https://github.com/SpectraLogic/ds3_java_sdk.git
cd ds3_java_sdk

echo "env:"
env|grep DS3
./gradlew test
