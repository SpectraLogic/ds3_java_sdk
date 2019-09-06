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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.models.S3TargetBucketNameList;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationResponse;

public class GetS3TargetBucketNamesSpectraS3Response extends AbstractPaginationResponse {

    private final S3TargetBucketNameList s3TargetBucketNameListResult;

    public GetS3TargetBucketNamesSpectraS3Response(final S3TargetBucketNameList s3TargetBucketNameListResult, final Integer pagingTotalResultCount, final Integer pagingTruncated, final String checksum, final ChecksumType.Type checksumType) {
        super(pagingTotalResultCount, pagingTruncated, checksum, checksumType);
        this.s3TargetBucketNameListResult = s3TargetBucketNameListResult;
    }

    public S3TargetBucketNameList getS3TargetBucketNameListResult() {
        return this.s3TargetBucketNameListResult;
    }

}