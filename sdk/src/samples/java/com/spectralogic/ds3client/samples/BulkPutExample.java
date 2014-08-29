package samples.java.com.spectralogic.ds3client.samples;

import main.java.com.spectralogic.ds3client.Ds3Client;
import main.java.com.spectralogic.ds3client.Ds3ClientBuilder;
import main.java.com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import main.java.com.spectralogic.ds3client.helpers.FileObjectPutter;
import main.java.com.spectralogic.ds3client.models.Credentials;
import main.java.com.spectralogic.ds3client.models.bulk.Ds3Object;
import main.java.com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.SignatureException;

public class BulkPutExample {

    public static void main(final String args[]) throws IOException, SignatureException, XmlProcessingException {
        final Ds3Client client = Ds3ClientBuilder.create("endpoint:8080",
                new Credentials("accessId", "secretKey"))
                .withHttpSecure(false)
                .build();

        // Wrap the Ds3Client with the helper functions
        final Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(client);

        // The bucket that we will be writing to
        final String bucketName = "my_bucket";

        // Make sure that the bucket exists, if it does not this will create it
        helper.ensureBucketExists(bucketName);

        // Our input path which contains all the files that we want to transfer
        final Path inputPath = FileSystems.getDefault().getPath("input");

        // Get the list of files that are contained in the inputPath
        final Iterable<Ds3Object> objects = helper.listObjectsForDirectory(inputPath);

        // Create the write job with the bucket we want to write to and the list
        // of objects that will be written
        final Ds3ClientHelpers.WriteJob job = helper.startWriteJob(bucketName, objects);

        // Start the write job using an Object Putter that will read the files
        // from the local file system.
        job.write(new FileObjectPutter(inputPath));
    }
}