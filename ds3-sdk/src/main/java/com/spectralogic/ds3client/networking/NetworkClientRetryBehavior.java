package com.spectralogic.ds3client.networking;

import com.spectralogic.ds3client.commands.interfaces.Ds3Request;

public interface NetworkClientRetryBehavior {
    /**
     * Implement shouldRetry() to determine whether you want to continue trying to call
     * @see com.spectralogic.ds3client.networking.NetworkClient#getResponse(Ds3Request)
     *
     * @param webResponse The response returned from a call to
     *                    @see com.spectralogic.ds3client.networking.NetworkClient#getResponse(Ds3Request)
     * @return true to call @see com.spectralogic.ds3client.networking.NetworkClient#getResponse(Ds3Request)
     * again, false otherwise.
     */
    boolean shouldRetry(final WebResponse webResponse);
}
