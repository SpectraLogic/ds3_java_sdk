package com.spectralogic.ds3client;


import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.utils.DateFormatter;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) throws Exception {

        final Ds3ClientBuilder builder = new Ds3ClientBuilder("192.168.6.156:8080", new Credentials("cnlhbg==","4iDEhFRV"));
        //final Ds3Client client = builder.withHttpSecure(false).withPort(8080).withProxy("http://192.168.56.104:8080").build();
        final Ds3Client client = builder.withHttpSecure(false).build();


        final ListAllMyBucketsResult result = client.getService();
        System.out.println(result.toString());




        //final String bucket = "test";
        //client.createBucket(bucket);
        /*
        final ListBucketResult objectList = client.listBucket(bucket);
          System.out.println(objectList);

        //client.listJobs(bucket);

        final List<Ds3Object> objects = new ArrayList<Ds3Object>();
        //objects.add(new Ds3Object("/user/hduser/gutenberg/20417.txt.utf-8",256));
        objects.add(new Ds3Object("user/hduser/books/huckfinn.txt",610157));
        //objects.add(new Ds3Object("/user/hduser/gutenberg/4300.txt.utf-8",2523));

        //final MasterObjectList masterObjectList =  client.bulkGet(bucket, objects.iterator());

        final InputStream io = client.getObject(bucket,"user/hduser/books/huckfinn.txt");

        FileOutputStream out = new FileOutputStream("test.txt");


        IOUtils.copy(io,out);

        out.close();
        io.close();

        //client.listJobs(bucket);

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
