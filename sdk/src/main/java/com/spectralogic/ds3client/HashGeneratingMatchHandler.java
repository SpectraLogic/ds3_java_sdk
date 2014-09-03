/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client;

import com.spectralogic.ds3client.models.Checksum;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class HashGeneratingMatchHandler implements Checksum.MatchHandler<String, IOException> {
    private static final int READ_BUFFER_SIZE = 1024;
    
    private final InputStream content;
    
    public HashGeneratingMatchHandler(final InputStream content) {
        this.content = content;
    }
    
    @Override
    public String none() throws IOException {
        return "";
    }
    
    @Override
    public String compute() throws IOException {
        return Base64.encodeBase64String(this.hashInputStream(this.getMd5Hasher(), this.content));
    }
    
    @Override
    public String value(final byte[] hash) throws IOException {
        return Base64.encodeBase64String(hash);
    }
    
    private byte[] hashInputStream(final MessageDigest digest, final InputStream stream) throws IOException {
        final byte[] buffer = new byte[READ_BUFFER_SIZE];
        int bytesRead = 0;
        
        while (true) {
            bytesRead = stream.read(buffer);
            
            if (bytesRead < 0) {
                break;
            }
            
            digest.update(buffer, 0, bytesRead);
        }
        
        return digest.digest();
    }

    private MessageDigest getMd5Hasher() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
