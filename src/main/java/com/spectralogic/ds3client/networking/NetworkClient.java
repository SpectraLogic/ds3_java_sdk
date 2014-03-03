package com.spectralogic.ds3client.networking;

import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;
import com.spectralogic.ds3client.models.SignatureDetails;
import com.spectralogic.ds3client.utils.DateFormatter;
import com.spectralogic.ds3client.utils.Signature;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;


public class NetworkClient {

    final static private String HOST = "HOST";
    final static private String DATE = "DATE";
    final static private String AUTHORIZATION = "Authorization";
    final static private String CONTENTMD5 = "Content-MD5";
    final static private String CONTENT_TYPE = "Content-Type";
    final static private String STREAM_TYPE = "application/octet-stream";

    final private ConnectionDetails connectionDetails;

    public NetworkClient(final ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public ConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }

    public CloseableHttpResponse getResponse(final AbstractRequest request) throws IOException, SignatureException {
        final HttpHost host = getHost(connectionDetails);
        final HttpRequest httpRequest = getHttpRequest(request);

        final String date = DateFormatter.dateToRfc882();
        final CloseableHttpClient httpClient = HttpClients.createDefault();

        httpRequest.addHeader(HOST, NetUtils.buildHostField(connectionDetails));
        httpRequest.addHeader(DATE, date);
        httpRequest.addHeader(CONTENT_TYPE, request.getContentType().toString());
        for(final Map.Entry<String, String> header: request.getHeaders().entrySet()) {
            httpRequest.addHeader(header.getKey(), header.getValue());
        }

        final SignatureDetails sigDetails = new SignatureDetails(request.getVerb(), request.getMd5(), request.getContentType().toString(), date, "", request.getPath(),connectionDetails.getCredentials());
        httpRequest.addHeader(AUTHORIZATION, getSignature(sigDetails));

        return httpClient.execute(host, httpRequest);
    }

    private String getSignature(final SignatureDetails details) throws SignatureException {
        return "AWS " + connectionDetails.getCredentials().getClientId() + ':' + Signature.signature(details);
    }

    private RequestConfig getRequestConfig() {
        final URI proxyUri = connectionDetails.getProxy();
        final RequestConfig.Builder configBuilder = RequestConfig.custom();
        if(proxyUri != null) {
            final HttpHost proxyHost = new HttpHost(proxyUri.getHost(), proxyUri.getPort(), proxyUri.getScheme());
            configBuilder.setProxy(proxyHost);
        }
        return configBuilder.build();
    }

    private HttpHost getHost(final ConnectionDetails connectionDetails) throws MalformedURLException {
        final URI proxyUri = connectionDetails.getProxy();
        if(proxyUri != null) {
            return new HttpHost(proxyUri.getHost(), proxyUri.getPort(), proxyUri.getScheme());
        }

        final URL url = NetUtils.buildUrl(connectionDetails, "/");
        final int port = getPort(url);
        return new HttpHost(url.getHost(), port, url.getProtocol());
    }

    private int getPort(final URL url) {
        final int port = url.getPort();
        if(port < 0) {
            return 80;
        }
        return port;
    }

    private HttpRequest getHttpRequest(final AbstractRequest request) {
        final String verb = request.getVerb().toString();
        final InputStream stream = request.getStream();
        final Map<String, String> queryParams = request.getQueryParams();
        final String path;

        if(queryParams.isEmpty()) {
            path = request.getPath();
        }
        else {
            path = request.getPath() + "?" + NetUtils.buildQueryString(queryParams);
        }

        if(stream != null) {
            final HttpEntity entity = EntityBuilder.create()
                    .setStream(stream)
                    .setContentType(request.getContentType())
                    .build();
            final BasicHttpEntityEnclosingRequest httpRequest = new BasicHttpEntityEnclosingRequest(verb, path);
            httpRequest.setEntity(entity);
        }

        return new BasicHttpRequest(verb, path);
    }
}
