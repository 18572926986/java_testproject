package com.test.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.BoostingQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * from+size查询数据的方式
 * 1.将用户指定的关键字进行分词
 * 2.将词汇拿到分词库进行检索，得到多个文档的id
 * 3.去各个分片拉取到指定的数据，需要耗时
 * 4.将数据根据score进行排序，需要耗时
 * 5.根据from的值，将查询到的数据舍弃一部分
 * 6.将数据返回给客户端
 *
 * scroll+size查询
 * 1.将用户指定的关键字进行分词
 * 2.将词汇拿到分词库进行检索，得到多个文档的id
 * 3.将文档id存在es的上下文中
 * 4.根据指定的size去es检索指定个数的数据，拿完数据的文档id，从上下文去除
 * 5.需要下一页的数据，直接去es的上下文中找数据
 * 6.将数据返回给客户端
 */
public class Test002 {
    ObjectMapper mapper = new ObjectMapper();
    RestHighLevelClient client=ESClient.getClient();
    String index = "log";
    String type = "_doc";

    /**
     * term查询时代表完全匹配，搜索之前按不会对搜索字段进行分词，对搜索字段进行分词后，再进行匹配
     * from和size指定查询的起始位置和查询的数量
     * @throws IOException
     */
    @Test
    public void termQuery() throws IOException {
        // 创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        // 构建搜索源对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(5);
        sourceBuilder.query(QueryBuilders.termQuery("requestMethod", "POST"));

        // 搜索请求中设置搜索源对象
        searchRequest.source(sourceBuilder);
        // 执行搜索请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }

    /**
     * terms和term查询机制一样，但是terms可以匹配多个值，term只能匹配一个值
     * terms:where 字段 = 1 or 字段 = 2 or 字段 = 3
     * terms同样可以分词搜索，example:product或002可以通过terms搜索，但是product/002不能
     * term:where 字段 = 1
     */
    @Test
    public void termsQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termsQuery("requestMethod", "POST","GET"));

        searchRequest.source(builder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    /**
     * match查询默认只查询10条数据，查询更多需要添加size
     * 如果是查询日期或者数值，会基于字符串查询内容转换为日期或者数值
     * 如果是分词内容，会指定查询内容进行分词匹配，如奋斗新时代，可分词：奋斗,奋,斗,新时代,时代
     * @throws IOException
     */
    @Test
    public void matchAllQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());

        searchRequest.source(builder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void updateDocument() throws IOException {
        Map<String,Object> doc = new HashMap<>();
        doc.put("requestBody","18572826852");
        String docId="3";

        //将文档放入请求中
        UpdateRequest updateRequest = new UpdateRequest(index,type,docId);
        updateRequest.doc(doc);

        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);

        System.out.println(update.getResult().toString());
    }

    @Test
    public void matchQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("responseBody", "奋斗新时代"));

        searchRequest.source(builder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    /**
     * 布尔match查询
     * 基于一个字段匹配的内容，采用and或者or进行匹配
     */
    @Test
    public void boolmatchQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();

        builder.query(QueryBuilders.matchQuery("requestHeader","product 002").operator(Operator.OR));

        searchRequest.source(builder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    /**
     * multi_match查询(类似与并集)
     * match针对一个字段进行检索，multi_match针对多个字段进行检索,多个字段对应一个值
     */
    @Test
    public void multiMatchQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.multiMatchQuery("POST","requestMethod","requestHeader"));
        searchRequest.source(builder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());}
    }

    @Test
    public void findById() throws IOException {
        GetRequest getRequest = new GetRequest(index,type,"1");

        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

        System.out.println(getResponse.getSourceAsMap());
    }

    @Test
    public void findByIds() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();

        builder.query(QueryBuilders.idsQuery().addIds("1","2","3"));

        searchRequest.source(builder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    /**
     * prefix查询
     * 基于前缀进行匹配
     */
    @Test
    public void prefixQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.prefixQuery("paramType","product"));
        searchRequest.source(builder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    /**
     * fuzzy查询
     * 基于模糊匹配
     */
    @Test
    public void fuzzyQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.fuzzyQuery("requestMethod","GETT"));
        searchRequest.source(builder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());}
    }

    /**
     * wildcard查询
     * 基于通配符匹配,product*匹配product开头的所有内容
     */
    @Test
    public void wildcardQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.wildcardQuery("requestMethod", "*GET"));
        searchRequest.source(builder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());}
    }

    /**
     * range查询
     * 基于范围匹配,只针对数值类型，对某一个字段进行大于或小于的指定
     */
    @Test
    public void rangeQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.rangeQuery("id").gte(16).lte(25));
        searchRequest.source(builder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());}
    }

    /**
     * regexp查询
     * 基于正则表达式匹配,prefix,fuzzy,wildcard,regexp查询效率都比较低，要求效率高的情况下，不建议使用
     */
    @Test
    public void regexpQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.regexpQuery("requestBody","185[0-9]{8}"));
        searchRequest.source(builder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());}
    }

    /**
     * 深分页Scoll
     * es的深分页，需要指定scroll的时间，每次查询的数量，以及查询的条件
     * es对from+size的限制，二者之和不能超过10000
     */
    @Test
    public void scrollQuery() throws IOException {
        // 1.查询请求
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        // 2.指定scroll的信息
        request.scroll(TimeValue.timeValueMinutes(1L));

        // 3.指定查询的内容
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(4);
        builder.sort("id", SortOrder.DESC);
        builder.query(QueryBuilders.matchAllQuery());

        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 4.获取scrollId
        String scrollId = response.getScrollId();
        System.out.println("----------首页----------");
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }

        while (true){
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            // 5.指定scrollId的的生存时间
            scrollRequest.scroll(TimeValue.timeValueMinutes(1L));

            // 6.获取scrollId的信息
            SearchResponse scrollResp = client.scroll(scrollRequest, RequestOptions.DEFAULT);

            // 7.判断是否还有数据
            SearchHit[] hits = scrollResp.getHits().getHits();
            if (hits!=null && hits.length>0){
                System.out.println("----------下一页----------");
                for (SearchHit hit : hits) {
                    System.out.println(hit.getSourceAsMap());
                }
            }else {
                System.out.println("----------没有数据了----------");
                break;
            }
        }

        // 8.清除scrollId
        ClearScrollRequest clearRequest = new ClearScrollRequest();
        // 9.指定scrollId
        clearRequest.addScrollId(scrollId);
        // 10.删除scrollId
        ClearScrollResponse clearResponse = client.clearScroll(clearRequest, RequestOptions.DEFAULT);

        System.out.println("删除结果"+clearResponse.isSucceeded());
    }

    @Test
    public void deleteByQuery() throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.types(type);

        request.setQuery(QueryBuilders.rangeQuery("id").gte(25).lte(27));

        BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);

        System.out.println(response.toString());
    }

    /**
     * bool查询
     * 基于bool查询，对多个查询条件进行组合，组合的方式有must,must_not,should
     * must：必须满足的条件
     * must_not：必须不满足的条件
     * should：满足其中一个条件即可
     *
     * #bool查询
     * #requestMethod为POST或GET
     * #responseBody不是奋斗新时代
     * #paramType为productId=1且id=18
     */
    @Test
    public void boolQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        boolQuery.should(QueryBuilders.termQuery("requestMethod","GET"));
        boolQuery.should(QueryBuilders.termQuery("requestMethod","POST"));

//      boolQuery.mustNot(QueryBuilders.termQuery("responseBody","奋斗新时代"));
//      boolQuery.must(QueryBuilders.termQuery("paramType","productId=1"));
//      boolQuery.must(QueryBuilders.termQuery("id","18"));

        builder.query(boolQuery);
        searchRequest.source(builder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    /**
     * boosting查询
     * 对某个字段进行权重设置，权重（分数）越高，匹配的结果越靠前
     * positive:只有匹配上positive的查询内容，才会被放到返回的结果中
     * negative:匹配上negative的查询内容，会被放到返回的结果中，但是权重会降低
     * negative_boost:指定系数，必须小于1
     *
     * #boosting查询
     * 搜索的关键词出现的频次越高，分数越高
     * 指定的文档内容越短，分数越高
     * 搜索时，指定的关键字会被分词，被分词库匹配的个数越多，分数越高
     */
    @Test
    public void boostingQuery() throws IOException {
        // 1.查询请求
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        // 2.指定查询的内容
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoostingQueryBuilder boostingQuery=QueryBuilders.boostingQuery(
                QueryBuilders.matchQuery("requestMethod","GET"),
                QueryBuilders.termQuery("id","18")
        ).negativeBoost(0.1f);

        builder.query(boostingQuery);
        searchRequest.source(builder);

        //    3.执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        // 4.处理结果
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    /**
     * filter查询
     * 对某个字段进行过滤，过滤的内容不会被放到返回的结果中
     */
    @Test
    public void filterQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("requestMethod","GET"));
        boolQuery.must(QueryBuilders.rangeQuery("id").lte(25));

        builder.query(boolQuery);
        searchRequest.source(builder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());}
    }

    /**
     * hightlight查询
     * 对某个字段进行高亮显示
     *
     * fragment_size：指定高亮显示的长度
     * pre_tags：指定高亮显示的前缀
     * post_tags：指定高亮显示的后缀
     */
    @Test
    public void hightlightQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("requestMethod","GET"));

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("requestMethod",3)
                .preTags("<span style='color:red'>")
              .postTags("</span>");

        builder.highlighter(highlightBuilder);

        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());}
    }

    /**
     * 聚合查询
     * 1.cardinality：去重查询,将返回的文档指定的字段去重，统计去重后的数量
     * 2.范围统计
     * 统计一定范围的文档数量，可针对普通数值，日期，时间等
     */
    @Test
    public void cardinalityQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.aggregation(AggregationBuilders.terms("requestMethod").field("requestMethod").size(10));

        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());}
    }
    @Test
    public void range() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.aggregation(AggregationBuilders.range("aggs").field("id")
                .addRange(0,10)
                .addRange(10,20)
                .addRange(20,30));

        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());}
    }
}
