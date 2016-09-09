package com.spectralogic.ds3client.helpers.pagination;

import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationRequest;
import com.spectralogic.ds3client.networking.HttpVerb;

import java.util.UUID;

public class StubbedRequest extends AbstractPaginationRequest {
    @Override
    public AbstractPaginationRequest withLastPage(final boolean lastPage) {
        return this;
    }

    @Override
    public AbstractPaginationRequest withPageLength(final int pageLength) {
        return this;
    }

    @Override
    public AbstractPaginationRequest withPageOffset(final int pageOffset) {
        return this;
    }

    @Override
    public AbstractPaginationRequest withPageStartMarker(final UUID pageStartMarker) {
        return this;
    }

    @Override
    public AbstractPaginationRequest withPageStartMarker(final String pageStartMarker) {
        return this;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public HttpVerb getVerb() {
        return null;
    }
}
