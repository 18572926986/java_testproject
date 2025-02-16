package com.test.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class ESClient {
//    private static final RestHighLevelClient client = new RestHighLevelClient(
//            RestClient.builder(
//                    new HttpHost("localhost", 9200, "http")
//            )
//    );
//
//    public static RestHighLevelClient getClient() {
//        return client;
//    }
    public static RestHighLevelClient getClient() {

        HttpHost httpHost = new HttpHost("localhost", 9200, "http");

        RestClientBuilder clientBuilder = RestClient.builder(httpHost);

        RestHighLevelClient client = new RestHighLevelClient(clientBuilder);

        return client;
    }
}