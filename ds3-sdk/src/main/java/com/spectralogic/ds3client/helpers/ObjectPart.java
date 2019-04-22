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

package com.spectralogic.ds3client.helpers;

import java.util.Objects;

public final class ObjectPart {
    private final long offset;
    private final long length;

    public ObjectPart(final long offset, final long length) {
        this.offset = offset;
        this.length = length;
    }

    public long getOffset() {
        return this.offset;
    }

    public long getLength() {
        return this.length;
    }
    
    public long getEnd() {
        return this.offset + this.length - 1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, length);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ObjectPart) {
            final ObjectPart otherPart = (ObjectPart)obj;
            return
                this.offset == otherPart.offset
                && this.length == otherPart.length;
        } else {
            return false;
        }
    }
}
