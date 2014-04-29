package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;

import com.google.common.base.Function;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

public class Ds3BulkException extends Exception {
    private static final long serialVersionUID = -9211581778560176653L;
    
    private final Throwable exception;

    public Ds3BulkException(final Throwable exception) {
        this.exception = exception;
    }
    
    public void throwInner() throws IOException, SignatureException, XmlProcessingException, Ds3KeyNotFoundException {
        if (this.exception instanceof IOException) {
            throw (IOException)this.exception;
        } else if (this.exception instanceof SignatureException) {
            throw (SignatureException)this.exception;
        } else if (this.exception instanceof XmlProcessingException) {
            throw (XmlProcessingException)this.exception;
        } else if (this.exception instanceof Ds3KeyNotFoundException) {
            throw (Ds3KeyNotFoundException)this.exception;
        } else if (this.exception instanceof RuntimeException) {
            throw (RuntimeException)this.exception;
        } else {
            throw new RuntimeException(this.exception);
        }
    }
    
    public static Function<Exception, Ds3BulkException> buildMapper() {
        return new Function<Exception, Ds3BulkException>() {
            @Override
            public Ds3BulkException apply(Exception input) {
                return new Ds3BulkException(input);
            }
        };
    }
}
