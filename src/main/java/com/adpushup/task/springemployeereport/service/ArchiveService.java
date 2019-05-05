package com.adpushup.task.springemployeereport.service;

import com.adpushup.task.springemployeereport.handler.FileHandler;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
public class ArchiveService {
    
    private final FileHandler fileHandler;
    
    public ArchiveService(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }
    
    public long getReportCount(YearMonth fromDate,
                               YearMonth toDate) {
        
        return fileHandler.getReportStream()
                .map(fileName -> fileName.substring(0, 7))
                .map(yearMonthString -> YearMonth.parse(yearMonthString))
                .filter(yearMonth -> yearMonth.compareTo(fromDate) >= 0
                        && yearMonth.compareTo(toDate) <= 0)
                .count();
    }
    
    public byte[] getReportBytes(YearMonth fromDate, YearMonth toDate) {
        return fileHandler.downloadReport(fromDate, toDate);
    }
    
}
