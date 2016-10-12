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

package com.spectralogic.ds3client.helpers.events;

import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.utils.Guard;

public class FailureEvent {
    private final String doingWhat;
    private final String objectName;
    private final String endpoint;
    private final Throwable causalException;

    private FailureEvent(final String what, final String objectName, final String endpoint, final Throwable causalException) {
        this.doingWhat = what;
        this.objectName = objectName;
        this.endpoint = endpoint;
        this.causalException = causalException;
    }

    public String doingWhat() {
        return doingWhat;
    }

    public String withObjectNamed() {
        return objectName;
    }

    public String usingSystemWithEndpoint() {
        return endpoint;
    }

    public Throwable getCausalException() {
        return causalException;
    }

    @Override
    public String toString() {
        return "Failure " + doingWhat + " with object named \"" + objectName + "\" using system with endpoint " + endpoint;
    }

    public static class Builder {
        private String doingWhat;
        private String withObjectNamed;
        private String usingSystemWithEndpoint;
        private Throwable causalException;

        public Builder() { }

        public Builder doingWhat(final String what) {
            this.doingWhat = what;
            return this;
        }

        public Builder withObjectNamed(final String objectName) {
            this.withObjectNamed = objectName;
            return this;
        }

        public Builder usingSystemWithEndpoint(final String endpoint) {
            this.usingSystemWithEndpoint = endpoint;
            return this;
        }

        public Builder withCausalException(final Throwable causalException) {
            this.causalException = causalException;
            return this;
        }

        public FailureEvent build() {
            Guard.throwOnNullOrEmptyString(doingWhat, "The failed activity may not be null or empty.");
            Guard.throwOnNullOrEmptyString(withObjectNamed, "The name of the object involved in the activity may not be null or empty.");
            Guard.throwOnNullOrEmptyString(usingSystemWithEndpoint, "The endpoint referenced in the activity may not be null or empty.");
            Preconditions.checkNotNull(causalException, "The exception causing a failure may not be null.");

            return new FailureEvent(doingWhat, withObjectNamed, usingSystemWithEndpoint, causalException);
        }
    }
}
