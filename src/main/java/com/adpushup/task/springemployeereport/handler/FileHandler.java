package com.adpushup.task.springemployeereport.handler;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileHandler {
    
    private static final String FILE_BASE_PATH = "../reports/";
    
    public Stream<String> getReportStream() {
        
        try {
            return Files.list(Paths.get(FILE_BASE_PATH))
                    .filter(file -> Files.isRegularFile(file))
                    .filter(file -> file.toString().endsWith(".tar.gz"))
                    .map(path -> path.getFileName().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public byte[] downloadReport(YearMonth fromDate,
                                 YearMonth toDate) {
        
        List<String> files = getReportStream()
                .filter(filePath -> isFileCreatedWithinGivenDateRange(fromDate, toDate, filePath))
                .collect(Collectors.toList());
        
        try {
            return getReportBytes(files);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private byte[] getReportBytes(List<String> files) throws IOException {
        
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
             GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(bufferedOutputStream);
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(gzipOutputStream)) {
            
            for (String fileName : files) {
                
                TarArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
                
                byte[] documentBytes = IOUtils.toByteArray(new FileInputStream(FILE_BASE_PATH + fileName));
                
                archiveEntry.setSize(documentBytes.length);
                
                tarArchiveOutputStream.putArchiveEntry(archiveEntry);
                tarArchiveOutputStream.write(documentBytes);
                tarArchiveOutputStream.closeArchiveEntry();
            }
            
            tarArchiveOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        }
    }
    
    private boolean isFileCreatedWithinGivenDateRange(YearMonth fromDate, YearMonth toDate, String filePath) {
        
        String yearMonthString = filePath.substring(0, 7);
        YearMonth yearMonth = YearMonth.parse(yearMonthString);
        
        return yearMonth.compareTo(fromDate) >= 0 && yearMonth.compareTo(toDate) <= 0;
    }
}
