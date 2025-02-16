package com.test.es;

import lombok.val;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElasticsearchClientProviderTest {

    @Test
    public void getClient() {
        RestHighLevelClient client = ESClient.getClient();
        System.out.println("ok");
    }
}