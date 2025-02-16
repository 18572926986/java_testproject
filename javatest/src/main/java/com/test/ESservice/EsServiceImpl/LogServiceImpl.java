package com.test.ESservice.EsServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.ESservice.LogService;
import com.test.es.ESClient;
import com.test.es.LogRepository;
import com.test.pojo.log.ESLog;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    RestHighLevelClient client= ESClient.getClient();
    String index = "log";
    String type = "_doc";

    @Override
    public List<Map<String, Object>> matchAllQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());

        searchRequest.source(builder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

//        List<ESLog> results = new ArrayList<>();
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            results.add(hit.getSourceAsMap());
        }
        return results;
    }

//    @Override
//    public void exportLogsToExcel(List<ESLog> logs, String filePath) throws IOException {
//            Workbook workbook = new XSSFWorkbook();
//            Sheet sheet = workbook.createSheet("Logs");
//
//            // 创建表头
//            Row headerRow = sheet.createRow(0);
//            String[] columns = {"ID", "Request Method", "Request Header","ParamType", "Request Body", "Request Time", "Response Headers", "Response Body", "Response Time", "Duration"};
//            for (int i = 0; i < columns.length; i++) {
//                Cell cell = headerRow.createCell(i);
//                cell.setCellValue(columns[i]);
//            }
//
//            // 填充数据
//            int rowNum = 1;
//            for (ESLog log : logs) {
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(log.getId());
//                row.createCell(1).setCellValue(log.getRequestMethod());
//                row.createCell(2).setCellValue(log.getRequestHeader());
//                row.createCell(3).setCellValue(log.getParamType());
//                row.createCell(4).setCellValue(log.getRequestBody());
//                row.createCell(5).setCellValue(log.getRequestTime().toString());
//                row.createCell(6).setCellValue(log.getResponseHeaders());
//                row.createCell(7).setCellValue(log.getResponseBody());
//                row.createCell(8).setCellValue(log.getResponseTime().toString());
//                row.createCell(9).setCellValue(log.getDuration());
//            }
//
//            // 自动调整列宽
//            for (int i = 0; i < columns.length; i++) {
//                sheet.autoSizeColumn(i);
//            }
//
//            // 写入文件
//            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//                workbook.write(fileOut);
//            } finally {
//                workbook.close();
//            }
//    }

    @Override
    public void exportLogsToExcel(String indexName, String filePath) throws IOException {
        // 创建工作簿和工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Logs");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Request Method", "Request Header", "ParamType", "Request Body", "Request Time", "Response Headers", "Response Body", "Response Time", "Duration"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // 查询Elasticsearch数据
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery()); // 可以根据需要修改查询条件
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();

        // 填充数据
        int rowNum = 1;
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue((String) sourceAsMap.get("id"));
            row.createCell(1).setCellValue((String) sourceAsMap.get("requestMethod"));
            row.createCell(2).setCellValue((String) sourceAsMap.get("requestHeader"));
            row.createCell(3).setCellValue((String) sourceAsMap.get("paramType"));
            row.createCell(4).setCellValue((String) sourceAsMap.get("requestBody"));
            row.createCell(5).setCellValue((String) sourceAsMap.get("requestTime"));
            row.createCell(6).setCellValue((String) sourceAsMap.get("responseHeaders"));
            row.createCell(7).setCellValue((String) sourceAsMap.get("responseBody"));
            row.createCell(8).setCellValue((String) sourceAsMap.get("responseTime"));
            row.createCell(9).setCellValue((Integer) sourceAsMap.get("duration"));
        }

        // 自动调整列宽
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 写入文件
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }
    }


    @Override
    public List<Map<String, Object>> shouldMatchQuery(Map<String, String> params) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, String> entry : params.entrySet()) {// 遍历参数
            boolQueryBuilder.should(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));// 构建should查询条件
        }

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(boolQueryBuilder);

        searchRequest.source(builder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            results.add(hit.getSourceAsMap());
        }
        return results;
    }

    @Override
    public List<Map<String, Object>> mustMatchQuery(Map<String, String> params) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, String> entry : params.entrySet()) {// 遍历参数
            boolQueryBuilder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));// 构建should查询条件
        }

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(boolQueryBuilder);

        searchRequest.source(builder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            results.add(hit.getSourceAsMap());
        }
        return results;
    }


    @Override
    public Integer queryProductPreview(Map<String, String> params) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.query(QueryBuilders.wildcardQuery(entry.getKey(), "*" + entry.getValue()));
        }
        searchRequest.source(builder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        List<Map<String, Object>> results = new ArrayList<>();
        int cont = 0;
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            results.add(hit.getSourceAsMap());
            cont++;
        }
        return cont;
    }
    @Override
    public List<ESLog> LogsSearch(String RequestMethod, String RequestHeader, String ParamType) {
        return logRepository.findByRequestMethodOrRequestHeaderOrParamType(RequestMethod, RequestHeader, ParamType);
    }
    @Override
    public List<Map<String, Object>> LogSearch(String keyword, String data) throws IOException {
        return List.of();
    }

}
