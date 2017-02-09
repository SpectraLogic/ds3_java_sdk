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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

public class ObjectChannelBuilderLogger implements Ds3ClientHelpers.ObjectChannelBuilder {
    final private static Logger LOG = LoggerFactory.getLogger(ObjectChannelBuilderLogger.class);
    final private Ds3ClientHelpers.ObjectChannelBuilder channelBuilder;

    public ObjectChannelBuilderLogger(final Ds3ClientHelpers.ObjectChannelBuilder channelBuilder) {
        this.channelBuilder = channelBuilder;
    }

    @Override
    public SeekableByteChannel buildChannel(final String s) throws IOException {
        LOG.info("Opening channel for: {}", s);
        return this.channelBuilder.buildChannel(s);
    }
}
