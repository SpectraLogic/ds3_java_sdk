package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.spectralogic.ds3client.models.Range;

public class PartialDs3Object extends Ds3Object {

    @JsonUnwrapped
    private final Range range;

    public PartialDs3Object(final String name, final Range range) {
        super(name);
        this.range = range;
    }

    public Range getRange() {
        return range;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.getName(), this.getRange());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PartialDs3Object) {
            final PartialDs3Object partialDs3Object = (PartialDs3Object) obj;
            if (super.equals(obj) && this.getRange().equals(partialDs3Object.getRange())) {
                return true;
            }
        }
        return false;
    }
}
