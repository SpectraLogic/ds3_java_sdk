/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3client.helpers.pagination;

import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationRequest;
import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationResponse;

import java.io.IOException;
import java.util.List;

/**
 * This interface is used to implement the call specific logic for any Spectra S3 paginated request.
 * @param <E> The type the iterator will return.
 * @param <T> The {@link AbstractPaginationRequest} that the command will invoke
 * @param <F> The {@link AbstractPaginationResponse} that will be returned by the {@link AbstractPaginationRequest}
 */
public interface PaginatingCommand<E, T extends AbstractPaginationRequest, F extends AbstractPaginationResponse> {
    /**
     * Creates an initial request.  This should not modify any of the pagination values for the request
     */
    T createRequest();

    /**
     * Invokes the request and returns the response
     */
    F invokeCommand(final T paginationRequest) throws IOException;

    /**
     * Parses the response into the correct payload to be consumed by the iterator
     */
    List<E> getResponseContents(F response);
}
