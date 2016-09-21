/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.helpers.pagination;

import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationRequest;
import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationResponse;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.TooManyRetriesException;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * This class is used as the base implementation for all Spectra S3 paginating calls.  It requires a {@link PaginatingCommand} which implements the
 * call specific logic for each paginated request.
 * @param <T> The type returned by the iterator
 * @param <E> The {@link com.spectralogic.ds3client.commands.interfaces.AbstractPaginationRequest} this loader will invoke
 * @param <F> The {@link com.spectralogic.ds3client.commands.interfaces.AbstractPaginationResponse} that the {@link com.spectralogic.ds3client.commands.interfaces.AbstractPaginationRequest} will return.
 */
public class SpectraS3PaginationLoader<T, E extends AbstractPaginationRequest, F extends AbstractPaginationResponse> implements LazyIterable.LazyLoader<T> {

    private final PaginatingCommand<T, E, F> paginatingCommand;
    private final int pageLength;
    private final int retries;

    private int pageOffset = 0;
    private int pagingTotalResultCount;
    private int totalItems = 0;

    public SpectraS3PaginationLoader(final PaginatingCommand<T, E, F> paginatingCommand, final int pageLength, final int retries) {
        this.paginatingCommand = paginatingCommand;
        this.pageLength = pageLength;
        this.retries = retries;
    }

    @Override
    public List<T> getNextValues() {

        int retryAttempt = 0;

        while(true) {
            if (totalItems > 0 && totalItems >= pagingTotalResultCount) {
                return Collections.emptyList();
            }

            final E request = paginatingCommand.createRequest();

            request.withPageLength(pageLength)
                    .withPageOffset(pageOffset);

            try {
                final F response = paginatingCommand.invokeCommand(request);
                pagingTotalResultCount = response.getPagingTotalResultCount();

                final List<T> newValues = paginatingCommand.getResponseContents(response);

                totalItems += newValues.size();

                pageOffset++;

                return newValues;
            } catch (final FailedRequestException e) {
                // failure
                throw new RuntimeException("Encountered a failure when attempting to get object list", e);
            } catch (final IOException e) {
                //error
                if (retryAttempt >= retries) {
                    //TODO need a proxied test to validate this retry logic
                    throw new TooManyRetriesException("Failed to get the next set of objects from the getBucket request after " + retries + " retries", e);
                }
                retryAttempt++;
            }
        }

    }
}
