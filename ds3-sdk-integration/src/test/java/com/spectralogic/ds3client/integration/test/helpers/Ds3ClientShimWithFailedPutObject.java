package com.spectralogic.ds3client.integration.test.helpers;

import com.spectralogic.ds3client.Ds3ClientImpl;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.PutObjectResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Ds3ClientShimWithFailedPutObject extends Ds3ClientShim {
    public Ds3ClientShimWithFailedPutObject(final Ds3ClientImpl ds3ClientImpl)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        super(ds3ClientImpl);
    }

    @Override
    public PutObjectResponse putObject(final PutObjectRequest request) throws IOException {
        throw new IOException("A terrible, horrible thing happened!");
    }
}
