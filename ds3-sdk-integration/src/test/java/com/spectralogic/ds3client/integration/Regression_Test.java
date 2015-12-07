package com.spectralogic.ds3client.integration;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.DeleteObjectRequest;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.security.SignatureException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class Regression_Test {

    private static final Logger LOG = LoggerFactory.getLogger(Regression_Test.class);

    private static Ds3Client client;

    @BeforeClass
    public static void startup() {
        client = Util.fromEnv();
    }

    @AfterClass
    public static void teardown() throws IOException {
        client.close();
    }

    @Test
    public void testMarkerWithSpaces() throws IOException, SignatureException, XmlProcessingException {
        final String bucketName = "marker_with_spaces";
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        try {
            helpers.ensureBucketExists(bucketName);

            final List<Ds3Object> objects = Lists.newArrayList(
                    new Ds3Object("obj1_no_spaces.txt", 1024),
                    new Ds3Object("obj2_no_spaces.txt", 1024),
                    new Ds3Object("obj3 has spaces.txt", 1024),
                    new Ds3Object("obj4 also has spaces.txt", 1024));

            final Ds3ClientHelpers.Job putJob = helpers.startWriteJob(bucketName, objects);

            putJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(String key) throws IOException {
                    final byte[] randomData = IOUtils.toByteArray(new RandomDataInputStream(120, 1024));
                    final ByteBuffer randomBuffer = ByteBuffer.wrap(randomData);

                    final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(1024);
                    channel.write(randomBuffer);

                    return channel;
                }
            });

            final Iterable<Contents> objs = helpers.listObjects(bucketName, null, "obj3 has spaces.txt");
            boolean foundObj4 = false;
            for( Contents obj : objs ) {
                LOG.info("marker with spaces name: " + obj.getKey());
                if (obj.getKey().equals("obj4 also has spaces.txt")) foundObj4 = true;
                LOG.info("marker with spaces size: " + obj.getSize());
            }
            assertTrue(Iterables.size(objs) == 1);
            assertTrue(foundObj4);
        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void testPrefixWithSpaces() throws IOException, SignatureException, XmlProcessingException {
        final String bucketName = "prefix_with_spaces";
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        try {
            helpers.ensureBucketExists(bucketName);

            final List<Ds3Object> objects = Lists.newArrayList(
                    new Ds3Object("obj1_no_spaces.txt", 1024),
                    new Ds3Object("has spaces obj2.txt", 1024),
                    new Ds3Object("obj3_no_spaces.txt", 1024),
                    new Ds3Object("has spaces obj4.txt", 1024));

            final Ds3ClientHelpers.Job putJob = helpers.startWriteJob(bucketName, objects);

            putJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(String key) throws IOException {
                    final byte[] randomData = IOUtils.toByteArray(new RandomDataInputStream(120, 1024));
                    final ByteBuffer randomBuffer = ByteBuffer.wrap(randomData);

                    final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(1024);
                    channel.write(randomBuffer);

                    return channel;
                }
            });

            final Iterable<Contents> objs = helpers.listObjects(bucketName, "has spaces");
            boolean foundObj2 = false;
            boolean foundObj4 = false;
            for( Contents obj : objs ) {
                LOG.info("prefix with spaces name: " + obj.getKey());
                LOG.info("prefix with spaces size: " + obj.getSize());
                if (obj.getKey().equals("has spaces obj2.txt")) foundObj2 = true;
                if (obj.getKey().equals("has spaces obj4.txt")) foundObj4 = true;
            }
            assertTrue(Iterables.size(objs) == 2);
            assertTrue(foundObj2);
            assertTrue(foundObj4);

        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }
}
