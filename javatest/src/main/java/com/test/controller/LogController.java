package com.test.controller;

//import com.test.pojo.LogEntry;
//import com.test.pojo.LogEntry;
import com.test.ESservice.LogService;
import com.test.es.LogRepository;
import com.test.pojo.Result;
//import com.test.service.LogService;
import com.test.pojo.log.ESLog;
//import com.test.utils.LogUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
public class LogController {
    @Autowired
    private LogService logService;
    @Autowired
    private LogRepository logRepository;

    @GetMapping("/getLogs")
    public Result<?> getAllLogs()  {
        try {
            return Result.success(logService.matchAllQuery());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("获取日志失败"); //
        }
    }

    @PostMapping("/should")
    public Result<?> searchLogsByShould(@RequestBody Map<String, String> params){
        try {
            return Result.success(logService.shouldMatchQuery(params));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("获取日志失败");
        }
    }
    @PostMapping("/must")
    public Result<?> searchLogsByMust(@RequestBody Map<String, String> params){
        try {
            return Result.success(logService.mustMatchQuery(params));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("获取日志失败");
        }
    }


    @GetMapping("/exportLogs")
    public String exportLogs() throws IOException {
        try {
            logService.exportLogsToExcel("log", "logs.xlsx"); // 导出日志到指定文件路径
            return "Logs exported successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to export logs!";
        }
    }

////    @DeleteMapping("/deleteLogs")
////    public void deleteLog(@RequestParam Long id) {
////        // 删除指定 ID 的日志记录
////        logService.removeById(id);
////    }
}
