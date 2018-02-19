/*
 * ******************************************************************************
 *   Copyright 2014-2018 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates a channel builder that generates random data. All object keys and sizes need to be specified.
 */
public class RandomDataChannelBuilder implements Ds3ClientHelpers.ObjectChannelBuilder {

    private static final int seed = 12345;
    private final Map<String, Long> objectMap = new HashMap();

    public RandomDataChannelBuilder() {
    }

    public RandomDataChannelBuilder(final List<Ds3Object> objects) {
        for (final Ds3Object obj : objects) {
            this.objectMap.put(obj.getName(), obj.getSize());
        }
    }

    RandomDataChannelBuilder withObject(final String key, final Long size) {
        this.objectMap.put(key, size);
        return this;
    }

    @Override
    public SeekableByteChannel buildChannel(final String key) throws IOException {
        if (!this.objectMap.containsKey(key)) {
            throw new IllegalArgumentException(String.format("Object with name '%s' was not defined for this channel builder.", key));
        }

        final Long size = this.objectMap.get(key);
        final byte[] randomData = IOUtils.toByteArray(new RandomDataInputStream(seed, size));
        final ByteBuffer randomBuffer = ByteBuffer.wrap(randomData);

        final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(size.intValue());
        channel.write(randomBuffer);

        return channel;
    }
}
