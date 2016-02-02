/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.models;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

public abstract class Checksum {

    public enum Type {
        MD5, SHA256, SHA512, CRC32, CRC32C, NONE
    }

    private static final Checksum none = new None();
    private static final Checksum compute = new Compute();
    
    public static Checksum none() {
        return none;
    }
    
    public static Checksum compute() {
        return compute;
    }

    public static Checksum value(final String hash) {
        return new Value(hash);
    }

    public static Checksum value(final byte[] hash) {
        final String hashStr = Base64.encodeBase64String(hash);
        return new Value(hashStr);
    }
    
    public abstract <T, E extends Throwable> T match(final MatchHandler<T, E> handler) throws E;
    
    public interface MatchHandler<T, E extends Throwable> {
        T none() throws E;
        T compute() throws E;
        T value(final byte[] hash) throws E;
    }
    
    private Checksum() {
        // Prevent external instantiation.
    }
    
    private static class None extends Checksum {
        @Override
        public <T, E extends Throwable> T match(final MatchHandler<T, E> handler) throws E {
            return handler.none();
        }
    }
    
    private static class Compute extends Checksum {
        @Override
        public <T, E extends Throwable> T match(final MatchHandler<T, E> handler) throws E {
            return handler.compute();
        }
    }
    
    private static class Value extends Checksum {
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
