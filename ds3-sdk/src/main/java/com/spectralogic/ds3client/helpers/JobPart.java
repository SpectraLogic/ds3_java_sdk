/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.BulkObject;

/**
 * A class that associates a blob with the Black Pearl on which it resides.
 */
public class JobPart {
    private final Ds3Client client;
    private final BulkObject bulkObject;

    public JobPart(final Ds3Client client, final BulkObject bulkObject) {
        this.client = client;
        this.bulkObject = bulkObject;
    }

    public Ds3Client getClient() {
        return client;
    }

    public BulkObject getBulkObject() {
        return bulkObject;
    }

}
