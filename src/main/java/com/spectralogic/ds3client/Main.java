package com.spectralogic.ds3client;


import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.utils.DateFormatter;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) throws Exception {

        final Ds3ClientBuilder builder = new Ds3ClientBuilder("192.168.56.102", new Credentials("cnlhbg==","SSd6R45e"));
        final Ds3Client client = builder.withHttpSecure(false).withPort(8080).withProxy("http://192.168.56.104").build();
        //final Ds3Client client = builder.withHttpSecure(false).withPort(8080).build();

        final ListAllMyBucketsResult result = client.getService();
        System.out.println(result.toString());

        /*
        final String bucket = "remoteTest03";
        //client.createBucket(bucket);
        final ListBucketResult objectList = client.listBucket(bucket);
        System.out.println(objectList);



        client.listJobs(bucket);


        final List<Ds3Object> objects = new ArrayList<Ds3Object>();
        objects.add(new Ds3Object("/user/hduser/gutenberg/20417.txt.utf-8",256));
        objects.add(new Ds3Object("/user/hduser/gutenberg/5000.txt.utf-8",1202));
        objects.add(new Ds3Object("/user/hduser/gutenberg/4300.txt.utf-8",2523));

        System.out.println("Writing out files: " + objects);
        final MasterObjectList masterObjectList =  client.bulkPut(bucket, objects);
        System.out.println(masterObjectList.getJobid());
        System.out.println(masterObjectList);


        client.listJobs(bucket);


        System.out.println(DateFormatter.dateToRfc882());

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
