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
import com.spectralogic.ds3client.networking.HttpVerb;

import java.util.UUID;

public class StubbedRequest extends AbstractPaginationRequest {
    @Override
    public AbstractPaginationRequest withLastPage(final boolean lastPage) {
        return this;
    }

    @Override
    public AbstractPaginationRequest withPageLength(final int pageLength) {
        return this;
    }

    @Override
    public AbstractPaginationRequest withPageOffset(final int pageOffset) {
        return this;
    }

    @Override
    public AbstractPaginationRequest withPageStartMarker(final UUID pageStartMarker) {
        return this;
    }

    @Override
    public AbstractPaginationRequest withPageStartMarker(final String pageStartMarker) {
        return this;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public HttpVerb getVerb() {
        return null;
    }
}
