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

package com.spectralogic.ds3client.helpers.pagination;

import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationResponse;
import com.spectralogic.ds3client.networking.TooManyRetriesException;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpectraS3PaginationLoader_Test {

    @Test
    public void retry() throws IOException {
        final PaginatingCommand command = mock(PaginatingCommand.class);

        final AbstractPaginationResponse response = mock(AbstractPaginationResponse.class);
        when(response.getPagingTotalResultCount()).thenReturn(100);


        final StubbedRequest stubbedRequest = new StubbedRequest();
        when(command.createRequest()).thenReturn(stubbedRequest);
        when(command.invokeCommand(stubbedRequest)).thenThrow(new IOException());

        final SpectraS3PaginationLoader loader = new SpectraS3PaginationLoader(command, 10, 5);

        try {
            for (int i = 0; i < 6; i++) {
                loader.getNextValues();
            }
            fail("Should have thrown an exception");
        } catch (final Throwable e) {
            assertThat(e, is(instanceOf(TooManyRetriesException.class)));
        }
    }
}