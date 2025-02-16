//package com.test.service.serviceimpl;
//
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.test.mapper.LogMapper;
////import com.test.mapper.LogRepository;
//import com.test.pojo.LogEntry;
////import com.test.pojo.log.ESLog;
//import com.test.service.LogService;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.stereotype.Service;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//@Service
//public class LogServiceImpl extends ServiceImpl<LogMapper,LogEntry> implements LogService {
//
//    @Autowired
//    private LogMapper logMapper;
////    @Autowired
////    private LogRepository LogRepository;
//    //原LogEntry
//
//    public void saveLog(LogEntry log) {
//        logMapper.insert(log);
//    }
//
//    @Override
//        public void exportLogsToExcel(List<LogEntry> logs, String filePath) throws IOException {
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
//            for (LogEntry log : logs) {
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(log.getId());
//                row.createCell(1).setCellValue(log.getRequestMethod());
//                row.createCell(2).setCellValue(log.getRequestHeader());
//                row.createCell(3).setCellValue(log.getParamType());
//                row.createCell(3).setCellValue(log.getRequestBody());
//                row.createCell(4).setCellValue(log.getRequestTime().toString());
//                row.createCell(5).setCellValue(log.getResponseHeaders());
//                row.createCell(6).setCellValue(log.getResponseBody());
//                row.createCell(7).setCellValue(log.getResponseTime().toString());
//                row.createCell(8).setCellValue(log.getDuration());
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
//        }
//
//    @Override
//    public List<LogEntry> getAllLogs() {
//        return logMapper.selectList(null);
//    }
//}
//
