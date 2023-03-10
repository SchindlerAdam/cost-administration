package com.schindler.costadministration.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ExceptionDto {

    private HttpStatus errorCode;
    private String errorDetails;
}
