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

package com.spectralogic.ds3client.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ByteArraySeekableByteChannel implements SeekableByteChannel {
    private static final int DEFAULT_BUFFER_SIZE = 8096;

    private byte[] backingArray;
    private int position = 0;
    private int limit = 0;
    private boolean isOpen = true;
    
    public ByteArraySeekableByteChannel() {
        this(DEFAULT_BUFFER_SIZE);
    }
    
    public ByteArraySeekableByteChannel(final int size) {
        this.backingArray = new byte[size];
    }

    public ByteArraySeekableByteChannel(final byte[] bytes) {
        this.backingArray = Arrays.copyOfRange(bytes, 0, bytes.length);
        this.limit = bytes.length;
    }

    @Override
    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public void close() throws IOException {
        this.isOpen = false;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        final int numberOfBytes = Math.min(dst.remaining(), this.limit - this.position);
        dst.put(this.backingArray, this.position, numberOfBytes);
        this.position += numberOfBytes;
        return numberOfBytes;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        final int amountToWrite = src.remaining();
        expandArray(amountToWrite);
        src.get(this.backingArray, this.position, amountToWrite);
        this.position += amountToWrite;
        if (this.position >= this.limit) {
            this.limit = this.position;
        }
        return amountToWrite;
    }

    @Override
    public long position() throws IOException {
        return this.position;
    }

    @Override
    public SeekableByteChannel position(final long newPosition) throws IOException {
        this.position = (int)newPosition;
        return this;
    }

    @Override
    public long size() throws IOException {
        return this.limit;
    }

    @Override
    public SeekableByteChannel truncate(final long size) throws IOException {
        this.limit = Math.min((int)size, this.limit);
        this.position = Math.min((int)size, this.position);
        return this;
    }

    public byte[] toByteArray() {
        return Arrays.copyOfRange(this.backingArray, 0, this.limit);
    }
    
    @Override
    public String toString() {
        return this.toString(Charset.forName("UTF-8"));
    }
    
    public String toString(final Charset charset) {
        return new String(this.backingArray, 0, this.limit, charset);
    }

    private void expandArray(final int amountToWrite) {
        int newArraySize = this.backingArray.length;
        while (amountToWrite > newArraySize - this.position) {
            newArraySize *= 2;
        }
        if (newArraySize != this.backingArray.length) {
            final byte[] oldArray = this.backingArray;
            this.backingArray = new byte[newArraySize];
            System.arraycopy(oldArray, 0, this.backingArray, 0, oldArray.length);
        }
    }
}
