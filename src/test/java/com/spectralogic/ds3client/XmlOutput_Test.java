package com.spectralogic.ds3client;

import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.serializer.XmlOutput;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class XmlOutput_Test {

    @Test
    public void singleList() throws IOException {
        final String xmlResponse = "<masterobjectlist><objects><object name='file1' size='256'/><object name='file2' size='1202'/><object name='file3' size='2523'/></objects></masterobjectlist>";

        final MasterObjectList masterObjectList = XmlOutput.fromXml(xmlResponse, MasterObjectList.class);
        assertThat(masterObjectList, is(notNullValue()));
        assertThat(masterObjectList.getObjects(), is(notNullValue()));
        final List<Objects> objectsList = masterObjectList.getObjects();
        assertThat(objectsList.size(), is(1));
        final Objects objects = objectsList.get(0);
        final List<Ds3Object> objectList = objects.getObject();
        assertThat(objectList.size(), is(3));
    }
}
