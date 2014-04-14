package com.spectralogic.ds3client.networking;

import java.io.IOException;
import com.spectralogic.ds3client.models.Error;

public class FailedRequestException extends IOException {
    private static final long serialVersionUID = -2070737734216316074L;
    
    private final int statusCode;
    private final int expectedStatusCode;
    private final Error error;
	private final String responseString;

    public FailedRequestException(final int expectedStatusCode,
    							  final int statusCode,
    							  final Error error,
    							  final String responseString) {
        super(buildExceptionMessage(error, expectedStatusCode, statusCode));
        this.statusCode = statusCode;
        this.expectedStatusCode = expectedStatusCode;
        this.error = error;
        this.responseString = responseString;
    }

    public int getStatusCode() {
        return statusCode;
    }
    public int getExpectedStatusCode() {
        return expectedStatusCode;
    }
    public Error getError() {
        return error;
    }
	public String getResponseString() {
		return responseString;
	}
    
    private static String buildExceptionMessage(final Error error,
                                                final int expectedStatusCode,
                                                final int statusCode) {
    	return error == null
            ? String.format(
                "Expected a status code of %d but got %d. Could not parse the response for additional information.",
                expectedStatusCode,
                statusCode
            )
            : String.format(
                "Expected a status code of %d but got %d. Error message: \"%s\"",
                expectedStatusCode,
                statusCode,
                error.getMessage()
            );
    }
}
