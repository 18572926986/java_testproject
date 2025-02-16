package com.test.es;

import com.test.pojo.log.ESLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Map;

public interface LogRepository extends ElasticsearchRepository<ESLog, Integer> {
//    List<Map<String, Object>> findByRequestMethod(String requestMethod);
//    List<Map<String, Object>> findByRequestHeader(String requestHeader);
//    List<Map<String, Object>> findByParamType(String paramType);
    List<ESLog> findByRequestMethodOrRequestHeaderOrParamType(String requestMethod, String requestHeader, String paramType);
}
