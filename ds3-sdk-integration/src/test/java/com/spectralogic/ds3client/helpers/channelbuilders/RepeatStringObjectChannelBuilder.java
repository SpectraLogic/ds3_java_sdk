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

package com.spectralogic.ds3client.helpers.channelbuilders;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;

import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class RepeatStringObjectChannelBuilder implements Ds3ClientHelpers.ObjectChannelBuilder {
    private final int bufferSize;
    private final long sizeOfFiles;
    private String inputDataHeader;

    public RepeatStringObjectChannelBuilder(final String inputDataHeader, final int bufferSize, final long sizeOfFile) {
        this.bufferSize = bufferSize;
        this.sizeOfFiles = sizeOfFile;
        this.inputDataHeader = inputDataHeader;
    }

    @Override
    public SeekableByteChannel buildChannel(final String key) {
        return new RepeatStringObjectChannelBuilder.RepeatStringByteChannel(inputDataHeader + key, this.bufferSize, this.sizeOfFiles);
    }

    private static class RepeatStringByteChannel implements SeekableByteChannel {
        final private byte[] backingArray;
        final private int bufferSize;
        final private long limit;
        private boolean isOpen;
        private int position;

        RepeatStringByteChannel(final String inputData, final int bufferSize, final long size) {
            this.bufferSize = bufferSize;
            final byte[] bytes = new byte[bufferSize];
            final byte[] stringBytes = inputData.getBytes();
            int stringPosition = 0;
            for (int i = 0; i < bufferSize; i++) {
                bytes[i] = stringBytes[stringPosition];
                if (++stringPosition >= inputData.length()) {
                    stringPosition = 0;
                }
            }

            backingArray = bytes;

            this.position = 0;
            this.limit = size;
            this.isOpen = true;
        }

        public void close() {
            this.isOpen = false;
        }

        public boolean isOpen() {
            return this.isOpen;
        }

        public long position() {
            return (long) this.position;
        }

        public SeekableByteChannel position(final long newPosition) {
            this.position = (int) newPosition;
            return this;
        }

        public int read(final ByteBuffer dst) {
            final int amountToRead = Math.min(dst.remaining(), this.bufferSize);
            dst.put(this.backingArray, 0, amountToRead);
            return amountToRead;
        }

        public long size() {
            return this.limit;
        }

        public SeekableByteChannel truncate(final long size) {
            return this;
        }

        public int write(final ByteBuffer src) {
            final int amountToWrite = Math.min(src.remaining(), this.bufferSize);
            src.get(this.backingArray, 0, amountToWrite);
            return amountToWrite;
        }
    }
}
