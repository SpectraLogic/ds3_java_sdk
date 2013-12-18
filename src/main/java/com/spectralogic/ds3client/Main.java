package com.spectralogic.ds3client;


import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.ListAllMyBucketsResult;
import com.spectralogic.ds3client.models.MasterObjectList;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) throws Exception {
        final Ds3ClientBuilder builder = new Ds3ClientBuilder("192.168.56.101",new Credentials("cnlhbg==","R8ATmVhzTyGX"));
        final Ds3Client client = builder.withHttpSecure(false).withPort(8080).build();

        final String bucket = "remoteTest16";


        client.listBucket(bucket);

        //client.createBucket(bucket);

        //final ListAllMyBucketsResult result = client.getService();

        //System.out.println(result.toString());



        final List<Ds3Object> objects = new ArrayList<Ds3Object>();
        objects.add(new Ds3Object("user/hduser/gutenberg/20417.txt.utf-8",256));
        objects.add(new Ds3Object("user/hduser/gutenberg/5000.txt.utf-8",1202));
        objects.add(new Ds3Object("user/hduser/gutenberg/4300.txt.utf-8",2523));

        System.out.println("Writing out files: " + objects);
        final MasterObjectList masterObjectList =  client.bulkGet("/" + bucket + "/", objects);
        System.out.println(masterObjectList);

        /*
        System.out.println("================= Starting put =================");
        //client.putObject("testBucket2", "object2", new File("src/main/resources/testFile.txt"));

        final InputStream inputStream = client.getObject("testBucket2", "object2");

        final StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        System.out.println("Result: " + writer.toString());

        client.listBucket("testBucket2");

        */
    }
}
