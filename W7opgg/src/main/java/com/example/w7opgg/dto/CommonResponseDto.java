package com.example.w7opgg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDto<T> {
    private boolean result;
    private int code;
    private T data;

//    public static <T> CommonResponseDto <T> result(T data){
//
//        return new CommonResponseDto<>(true, (Integer) data, null);
//    }
 }
