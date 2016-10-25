/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.commands.interfaces;

import com.spectralogic.ds3client.models.ChecksumType;

public abstract class AbstractPaginationResponse extends AbstractResponse implements PaginationResponse {

    private Integer pagingTruncated;
    private Integer pagingTotalResultCount;

    public AbstractPaginationResponse(final Integer pagingTotalResultCount, final Integer pagingTruncated, final String checksum, final ChecksumType.Type checksumType) {
        super(checksum, checksumType);
        this.pagingTotalResultCount = pagingTotalResultCount;
        this.pagingTruncated = pagingTruncated;
    }

    @Override
    public Integer getPagingTruncated() {
        return this.pagingTruncated;
    }

    @Override
    public Integer getPagingTotalResultCount() {
        return this.pagingTotalResultCount;
    }
}
