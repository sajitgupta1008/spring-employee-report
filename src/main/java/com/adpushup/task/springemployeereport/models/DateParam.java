package com.adpushup.task.springemployeereport.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

@Data
@NoArgsConstructor
public class DateParam {
    
    @NotNull(message = "The fromDate query parameter is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    YearMonth fromDate;
    
    @NotNull(message = "The toDate query parameter is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    YearMonth toDate;
}

