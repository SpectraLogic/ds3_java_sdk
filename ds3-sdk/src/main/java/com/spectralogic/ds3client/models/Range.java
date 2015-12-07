package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Objects;

public final class Range implements Comparable<Range> {
    @JacksonXmlProperty(localName = "Offset", isAttribute = true)
    private final long start;

    @JsonIgnore
    private final long end;

    @JacksonXmlProperty(localName = "Length", isAttribute = true)
    private final long length;

    public static Range byPosition(final long start, final long end) {
        return new Range(start, end);
    }

    public static Range byLength(final long offset, final long length) {
        return new Range(offset, offset + length - 1);
    }

    public Range(final long start, final long end) {
        this.start = start;
        this.end = end;
        this.length = end - start + 1;
    }

    public long getLength() {
        return length;
    }

    public long getStart() {
         return this.start;
    }

    public long getEnd() {
       return this.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, length);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Range) {
            final Range range = (Range) obj;
            if (range.start == this.start && range.end == this.end) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(final Range o) {
        return Long.compare(this.getStart(), o.getStart());
    }
}