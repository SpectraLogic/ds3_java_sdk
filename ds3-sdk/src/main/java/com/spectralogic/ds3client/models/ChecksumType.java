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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.models;

import org.apache.commons.codec.binary.Base64;
import java.nio.charset.Charset;

public abstract class ChecksumType {

    public enum Type {
        CRC_32,
        CRC_32C,
        MD5,
        SHA_256,
        SHA_512,
        NONE
    }

    private static final ChecksumType none = new None();
    private static final ChecksumType compute = new Compute();

    public static ChecksumType none() {
        return none;
    }

    public static ChecksumType compute() {
        return compute;
    }

    public static ChecksumType value(final String hash) {
        return new Value(hash);
    }

    public static ChecksumType value(final byte[] hash) {
        final String hashStr = Base64.encodeBase64String(hash);
        return new Value(hashStr);
    }

    public abstract <T, E extends Throwable> T match(final MatchHandler<T, E> handler) throws E;

    public interface MatchHandler<T, E extends Throwable> {
        T none() throws E;
        T compute() throws E;
        T value(final byte[] hash) throws E;
    }

    private ChecksumType() {
        // Prevent external instantiation.
    }

    private static class None extends ChecksumType {
        @Override
        public <T, E extends Throwable> T match(final MatchHandler<T, E> handler) throws E {
            return handler.none();
        }
    }

    private static class Compute extends ChecksumType {
        @Override
        public <T, E extends Throwable> T match(final MatchHandler<T, E> handler) throws E {
            return handler.compute();
        }
    }

    private static class Value extends ChecksumType {
        private final String hash;

        public Value(final String hash) {
            this.hash = hash;
        }

        @Override
        public <T, E extends Throwable> T match(final MatchHandler<T, E> handler) throws E {
            return handler.value(this.hash.getBytes(Charset.forName("UTF-8")));
        }
    }
}
