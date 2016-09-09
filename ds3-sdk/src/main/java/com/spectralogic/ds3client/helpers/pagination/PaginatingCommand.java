package com.spectralogic.ds3client.helpers.pagination;

import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationRequest;
import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationResponse;

import java.io.IOException;
import java.util.List;

public interface PaginatingCommand<E, T extends AbstractPaginationRequest, F extends AbstractPaginationResponse> {
    T createRequest();
    F invokeCommand(final T paginationRequest) throws IOException;
    List<E> getResponseContents(F response);
}
