package com.adpushup.task.springemployeereport;

import com.adpushup.task.springemployeereport.models.DateParam;
import com.adpushup.task.springemployeereport.models.ReportCount;
import com.adpushup.task.springemployeereport.service.ArchiveService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ArchiveWebService")
public class ArchiveController {
    
    private final ArchiveService archiveService;
    
    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }
    
    @GetMapping("/logCount")
    public ReportCount logCount(@Valid DateParam dateParam) {
        
        long reportCount = archiveService
                .getReportCount(dateParam.getFromDate(), dateParam.getToDate());
        
        return ReportCount.builder().count(reportCount).build();
    }
    
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadReport(@Valid DateParam dateParam) {
        
        byte[] archiveBytes = archiveService
                .getReportBytes(dateParam.getFromDate(), dateParam.getToDate());
        
        return ResponseEntity.ok()
                .header("Content-disposition", "attachment; filename=report.tar.gz")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(archiveBytes));
    }
    
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    private Map<String, String> handleBindingException(BindException ex) {
        
        return ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> (FieldError) error)
                .collect(Collectors.toMap(error -> error.getField(), error -> error.getDefaultMessage()));
    }
}
