package com.test.ESservice;

import com.test.pojo.log.ESLog;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface LogService {
    List<Map<String, Object>> matchAllQuery() throws IOException;

    List<Map<String, Object>> LogSearch(String keyword,String data) throws IOException;

    void exportLogsToExcel(String indexName, String filePath) throws IOException;

    List<Map<String, Object>> shouldMatchQuery(Map<String, String> params) throws IOException;

    List<Map<String, Object>> mustMatchQuery(Map<String, String> params) throws IOException;

    Integer queryProductPreview(Map<String, String> params) throws IOException;

    List<ESLog> LogsSearch(String RequestMethod, String RequestHeader, String ParamType);
}
