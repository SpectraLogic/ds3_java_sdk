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

package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.spectralogic.ds3client.models.common.Range;

import java.util.UUID;

public class PartialDs3Object extends Ds3Object {

    @JsonUnwrapped
    private final Range range;

    public PartialDs3Object(final String name, final Range range) {
        super(name);
        this.range = range;
    }

    public PartialDs3Object(final String name, final Range range, final UUID versionId) {
        super(name, versionId);
        this.range = range;
    }

    public PartialDs3Object(final String name, final Range range, final String versionId) {
        super(name, versionId);
        this.range = range;
    }

    public Range getRange() {
        return range;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.getName(), this.getRange());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PartialDs3Object) {
            final PartialDs3Object partialDs3Object = (PartialDs3Object) obj;
            if (super.equals(obj) && this.getRange().equals(partialDs3Object.getRange())) {
                return true;
            }
        }
        return false;
    }
}
