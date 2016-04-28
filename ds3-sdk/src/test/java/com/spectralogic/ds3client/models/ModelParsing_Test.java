package com.spectralogic.ds3client.models;

import com.spectralogic.ds3client.serializer.XmlOutput;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ModelParsing_Test {

    @Test
    public void listBucketResult_Parse_Test() throws IOException {
        final String input = "<ListBucketResult><Contents><ETag>test</ETag><Key>movies/movie1.mov</Key><LastModified/>" +
                "<Owner><DisplayName>jason</DisplayName><ID>04531ac9-6639-4bee-8c09-9c8f0fbdbdcb</ID></Owner>" +
                "<Size>0</Size><StorageClass/></Contents><Contents><ETag/><Key>movies/movie2.mov</Key><LastModified/>" +
                "<Owner><DisplayName>jason</DisplayName><ID>04531ac9-6639-4bee-8c09-9c8f0fbdbdcb</ID></Owner>" +
                "<Size>0</Size><StorageClass/></Contents><CreationDate>2016-01-21T01:14:32.000Z</CreationDate>" +
                "<Delimiter>/</Delimiter><IsTruncated>true</IsTruncated><Marker>movies/cover.jpg</Marker>" +
                "<MaxKeys>2</MaxKeys><Name>bucket1</Name><NextMarker>movies/movie2.mov</NextMarker>" +
                "<Prefix>movies/</Prefix></ListBucketResult>";

        final ListBucketResult result = XmlOutput.fromXml(input, ListBucketResult.class);
        assertThat(result.getName(), is("bucket1"));
        assertThat(result.getObjects().size(), is(2));
        assertThat(result.getObjects().get(0).getETag(), is("test"));
        assertThat(result.getObjects().get(0).getKey(), is("movies/movie1.mov"));
        assertThat(result.getObjects().get(1).getETag(), is(nullValue()));
    }

    @Test
    public void listBucketResult_CommonPrefixes_Test() throws IOException {
        final String input = "<ListBucketResult>" +
                "<CommonPrefixes><Prefix>movies/</Prefix></CommonPrefixes>" +
                "<CommonPrefixes><Prefix>scores/</Prefix></CommonPrefixes>" +
                "<Contents><ETag/><Key>music.mp3</Key><LastModified/><Owner><DisplayName>jason</DisplayName>" +
                "<ID>04531ac9-6639-4bee-8c09-9c8f0fbdbdcb</ID></Owner><Size>0</Size><StorageClass/></Contents>" +
                "<Contents><ETag/><Key>song.mp3</Key><LastModified/><Owner><DisplayName>jason</DisplayName>" +
                "<ID>04531ac9-6639-4bee-8c09-9c8f0fbdbdcb</ID></Owner><Size>0</Size><StorageClass/></Contents>" +
                "<CreationDate>2016-01-21T01:14:32.000Z</CreationDate><Delimiter>/</Delimiter>" +
                "<IsTruncated>false</IsTruncated><Marker/><MaxKeys>1000</MaxKeys><Name>bucket1</Name><NextMarker/>" +
                "<Prefix/></ListBucketResult>";

        final ListBucketResult result = XmlOutput.fromXml(input, ListBucketResult.class);
        assertThat(result.getCommonPrefixes().size(), is(2));
        assertThat(result.getCommonPrefixes().get(0).getPrefix(), is("movies/"));
        assertThat(result.getCommonPrefixes().get(1).getPrefix(), is("scores/"));
    }

    @Test
    public void namedDetailedTapeList_Test() throws IOException {
        final String input = "<Data><Tape><AssignedToStorageDomain>false</AssignedToStorageDomain>" +
                "<AvailableRawCapacity>10000</AvailableRawCapacity><BarCode>c3f74180-630f-48fa-bc5a-a2a61fa149a8</BarCode>" +
                "<BucketId/><DescriptionForIdentification/><EjectDate/><EjectLabel/><EjectLocation/><EjectPending/>" +
                "<FullOfData>false</FullOfData><Id>5d426192-9490-4f56-95cb-347bedaef948</Id><LastAccessed/>" +
                "<LastCheckpoint>new</LastCheckpoint><LastModified/><LastVerified/><MostRecentFailure>" +
                "<Date>2016-01-27T00:29:48.000Z</Date><ErrorMessage>OOPSIES</ErrorMessage>" +
                "<Id>21f873ec-dd18-479e-b463-e727954767a0</Id><TapeDriveId>0dc4da03-9d18-47fe-ba5f-73f7384b21b9</TapeDriveId>" +
                "<TapeId>5d426192-9490-4f56-95cb-347bedaef948</TapeId><Type>BAR_CODE_CHANGED</Type></MostRecentFailure>" +
                "<PartitionId>18984f3d-29b4-436f-86d4-d159743b9e61</PartitionId><PreviousState/><SerialNumber/>" +
                "<State>NORMAL</State><StorageDomainId/><TakeOwnershipPending>false</TakeOwnershipPending>" +
                "<TotalRawCapacity>20000</TotalRawCapacity><Type>LTO5</Type><VerifyPending/>" +
                "<WriteProtected>false</WriteProtected></Tape><Tape><AssignedToStorageDomain>false</AssignedToStorageDomain>" +
                "<AvailableRawCapacity>10000</AvailableRawCapacity><BarCode>fcf21335-53a6-4ec2-84fe-0708b36f358c</BarCode>" +
                "<BucketId/><DescriptionForIdentification/><EjectDate/><EjectLabel/><EjectLocation/><EjectPending/>" +
                "<FullOfData>false</FullOfData><Id>1edb0b0f-ff7c-43d2-aebd-8b520a4a4854</Id><LastAccessed/><LastCheckpoint/>" +
                "<LastModified/><LastVerified/><MostRecentFailure/><PartitionId>18984f3d-29b4-436f-86d4-d159743b9e61</PartitionId>" +
                "<PreviousState/><SerialNumber/><State>FOREIGN</State><StorageDomainId/><TakeOwnershipPending>false</TakeOwnershipPending>" +
                "<TotalRawCapacity>20000</TotalRawCapacity><Type>LTO5</Type><VerifyPending/><WriteProtected>false</WriteProtected></Tape>" +
                "</Data>";

        final NamedDetailedTapeList result = XmlOutput.fromXml(input, NamedDetailedTapeList.class);
        assertThat(result.getNamedDetailedTapes().size(), is(2));
    }
}
