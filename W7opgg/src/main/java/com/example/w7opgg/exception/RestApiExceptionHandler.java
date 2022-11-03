package com.example.w7opgg.exception;

import com.example.w7opgg.dto.CommonResponseDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { RequestException.class })
    public CommonResponseDto handleApiRequestException(RequestException e) {
        return new CommonResponseDto<String>(false, e.getCode(), e.getMessage());
    }
}
