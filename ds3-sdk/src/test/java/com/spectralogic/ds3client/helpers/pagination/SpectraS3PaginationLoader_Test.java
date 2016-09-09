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