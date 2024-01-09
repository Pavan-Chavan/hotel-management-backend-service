//package com.teams.config;
//
//import org.apache.http.Header;
//import org.apache.http.HttpHeaders;
//import org.apache.http.HttpHost;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
//import org.apache.http.message.BasicHeader;
//import org.apache.http.ssl.SSLContextBuilder;
//import org.apache.http.ssl.SSLContexts;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//
//import javax.net.ssl.SSLContext;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.KeyManagementException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.CertificateException;
//
//
//@Configuration
//public class ElasticConfiguration {
//    @Value("${elasticsearch.port}")
//    private String elasticPort;
//    @Value("${elasticsearch.host}")
//    private String elasticHost;
//    @Value("${elasticsearch.username}")
//    private String username;
//    @Value("${elasticsearch.password}")
//    private String password;
//    @Value("${elasticsearch.keystore}")
//    private String keystorePath;
//
//    @Bean
//    public RestHighLevelClient elasticSearchClient() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
////        ClientConfiguration clientConfiguration =ClientConfiguration.builder()
////                .connectedTo(elasticHost+":"+elasticPort).build();
////        ClientConfiguration clientConfiguration = ClientConfiguration.localhost();
////        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(elasticHost, Integer.parseInt(elasticPort),"https"));
//
//        // Optionally, configure additional settings
////        restClientBuilder.setMaxRetryTimeoutMillis(10000); // Set maximum retry timeout
////
////        // Configure Sniffer for node discovery (optional)
////        SniffOnFailureListener sniffOnFailureListener = new SniffOnFailureListener();
////        Sniffer sniffer = Sniffer.builder(restClientBuilder).setSniffIntervalMillis(60000).setSniffAfterFailureDelayMillis(30000).build();
//
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
////
////        Path trustStorePath = Paths.get(keystorePath);
////        KeyStore truststore = KeyStore.getInstance("jks");
////        try (InputStream is = Files.newInputStream(trustStorePath)) {
////            truststore.load(is, "changeit".toCharArray());
////        } catch (CertificateException e) {
////            e.printStackTrace();
////        }
////        SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(truststore, null);
//        final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
//
//
//
////        final SSLContext sslContext = SSLContexts.custom()
////                .loadTrustMaterial(new File(keystorePath), password.toCharArray(),
////                        new TrustSelfSignedStrategy())
////                .build();
//        // Build RestHighLevelClient with ClientConfiguration
////        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
////                .connectedTo(elasticHost + ":" + elasticPort)
////                .usingSsl()  // Use SSL if your Elasticsearch cluster is secured
////                .withConnectTimeout(5000) // Connection timeout in milliseconds
////                .withSocketTimeout(60000)
////                .withBasicAuth(username,password)
////                .build();
//
//        RestClientBuilder builder = RestClient.builder(
//                        new HttpHost(elasticHost, 9200, "http")).setDefaultHeaders(compatibilityHeaders())
//                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
//                    public HttpAsyncClientBuilder customizeHttpClient(
//                            final HttpAsyncClientBuilder httpAsyncClientBuilder) {
//                        return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider).setSSLContext(sslContext);
//                    }
//                });
//
//        RestHighLevelClient client = new RestHighLevelClient(builder);
////        RestHighLevelClient client = RestClients.create(clientConfiguration).rest();
//        return  client;
//    }
//
//    private Header[] compatibilityHeaders() {
//        return new Header[]{new BasicHeader(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7"), new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7")};
//    }
//
//}
