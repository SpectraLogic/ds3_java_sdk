package com.spectralogic.ds3client;

import com.spectralogic.ds3client.models.common.Credentials;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.networking.NetworkClientImpl;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Issue625Injection_Test {

    @Test(timeout = 10000)
    public void testHttpClientInjection() {
        final ConnectionDetails connectionDetails = ConnectionDetailsImpl.builder("http://localhost:8080", new Credentials("id", "key")).build();
        final CloseableHttpClient customHttpClient = HttpClients.createDefault();
        
        // This constructor was previously private
        final NetworkClient netClient = new NetworkClientImpl(connectionDetails, customHttpClient);
        
        final Ds3Client ds3Client = new Ds3ClientImpl(netClient);
        
        assertThat(((Ds3ClientImpl)ds3Client).getNetClient(), is(netClient));
    }

    @Test(timeout = 10000)
    public void testBuilderNetworkClientInjection() {
        final NetworkClient netClient = mock(NetworkClient.class);
        
        final Ds3Client ds3Client = Ds3ClientBuilder.create("http://localhost:8080", new Credentials("id", "key"))
                .withNetworkClient(netClient)
                .build();
        
        assertThat(((Ds3ClientImpl)ds3Client).getNetClient(), is(netClient));
    }
}
