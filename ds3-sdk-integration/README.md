DS3 Integration Tests
---------------------

This module will run through a suite of tests which uses the SDK to interact with a real DS3 endpoint.

In order to run the tests the following environment variables **must** be used to configure which DS3 appliance to run the tests against:
    DS3_ENDPOINT
    DS3_ACCESS_KEY
    DS3_SECRET_KEY

## Run

To run the integrations tests you must be at the root of the project `ds3_java_sdk` and not in `ds3_java_sdk/ds3-sdk-integration`.

To execute only the unit tests (no device connection required):

    ./gradlew clean ds3-sdk:test    

To execute only the integration tests:
    
    ./gradlew clean ds3-sdk-integration:test
    
To run both the integration tests and the unit tests execute:

    ./gradlew clean test    

The integration tests **should** clean up after themselves.  If they do not, that is considered an error.
