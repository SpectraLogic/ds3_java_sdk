DS3 Integration Tests
---------------------

This module will run through a suite of tests which uses the SDK to interact with a real DS3 endpoint.

In order to run the tests the same environment variables that can be used with the CLI **must** be used to configure which DS3 appliance to run the tests against.

## Run

To run the integrations tests you must be at the root of the project `ds3_java_sdk` and not in `ds3_java_sdk/integration`.

Then execute:

    ./gradlew clean integration:test
    
This will run only the integration tests.  If you want to run the integration tests and the unit tests execute:

    ./gradlew clean test
    
The integration tests **should** clean up after themselves.  If they do not, that is considered an error.