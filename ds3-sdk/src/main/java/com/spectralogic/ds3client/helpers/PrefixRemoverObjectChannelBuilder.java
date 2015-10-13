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

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Writes files to the local file system with a different path by first stripping off a remote prefix
 * and then adding a local prefix.
 */
public class PrefixRemoverObjectChannelBuilder implements ObjectChannelBuilder {
    final private Ds3ClientHelpers.ObjectChannelBuilder channelBuilder;
    private String prefix;

    public PrefixRemoverObjectChannelBuilder(final Ds3ClientHelpers.ObjectChannelBuilder channelBuilder, final String prefix) {
        this.channelBuilder = channelBuilder;
        this.prefix = prefix;
    }

    @Override
    public SeekableByteChannel buildChannel(final String key) throws IOException {
        final String strippedKey = Ds3ClientHelpers.stripLeadingPath(key, prefix);

        Files.createDirectories(Paths.get(strippedKey).getParent());
        return this.channelBuilder.buildChannel(strippedKey);
    }
}
