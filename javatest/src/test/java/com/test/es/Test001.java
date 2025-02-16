package com.test.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.pojo.Product;
import com.test.pojo.ProductTest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.elasticsearch.xcontent.json.JsonXContent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Test001 {
    ObjectMapper mapper = new ObjectMapper();
    RestHighLevelClient client=ESClient.getClient();
    String index = "product";
    String type = "test";

    @Test
    public void test() throws IOException {
        //准备索引的设置
        Settings.Builder settings = Settings.builder()
                .put("number_of_shards", 3)// 分片数
                .put("number_of_replicas", 1);// 副本数

        // 准备索引的映射
        XContentBuilder mappings = JsonXContent.contentBuilder()
                .startObject()
                .startObject("properties")
                .startObject("name")
                .field("type", "text")
                .endObject()
                .startObject("price")
                .field("type", "double")
                .endObject()
                .endObject()
                .endObject();

        //将索引的设置和映射封装
        CreateIndexRequest request = new CreateIndexRequest(index)
                .settings(settings)
                .mapping(type,mappings);

        System.out.println(1);
        //创建索引
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);

        System.out.println("createIndexResponse: "+createIndexResponse.toString());
    }

//    @Test
//    public void createDocument() throws IOException {
//        //创建文档
//        ProductTest productTest = new ProductTest(1, "小米手机", 3499.00);
//        String json = mapper.writeValueAsString(productTest);
//
//        //将文档放入请求中
//        IndexRequest indexRequest = new IndexRequest(index,type,productTest.getId().toString())
//              .source(json, XContentType.JSON);
//
//        //发送请求
//        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
//
//        System.out.println(indexResponse.getResult());
//    }

    @Test
    public void updateDocument() throws IOException {
        Map<String,Object> doc = new HashMap<>();
        doc.put("name","华为手机");
        String docId="1";

        //将文档放入请求中
        UpdateRequest updateRequest = new UpdateRequest(index,type,docId);
        updateRequest.doc(doc);

        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);

        System.out.println(update.getResult().toString());
    }

    @Test
    public void deleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index,type,"1");

        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);

        System.out.println(deleteResponse.getResult().toString());

    }

    @Test
    public void bulkCreate() throws IOException {
        //创建文档
        ProductTest p1 = new ProductTest(1, "小米手机", 3499.00);
        ProductTest p2 = new ProductTest(2, "华为手机", 4299.00);
        ProductTest p3 = new ProductTest(3, "苹果手机", 5299.00);

        //将文档放入请求中
        String json1 = mapper.writeValueAsString(p1);
        String json2 = mapper.writeValueAsString(p2);
        String json3 = mapper.writeValueAsString(p3);

        //将请求放入批量请求中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest(index,type,p1.getId().toString()).source(json1, XContentType.JSON));
        bulkRequest.add(new IndexRequest(index,type,p2.getId().toString()).source(json2, XContentType.JSON));
        bulkRequest.add(new IndexRequest(index,type,p3.getId().toString()).source(json3, XContentType.JSON));

        //发送请求
        BulkResponse bulkItemResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        System.out.println(bulkItemResponses.toString());

    }

    @Test
    public void bulkDelete() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        //将请求放入批量请求中
        bulkRequest.add(new DeleteRequest(index,type,"1"));
        bulkRequest.add(new DeleteRequest(index,type,"2"));
        bulkRequest.add(new DeleteRequest(index,type,"3"));

        BulkResponse bulkItemResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        System.out.println(bulkItemResponses.toString());
    }

}
