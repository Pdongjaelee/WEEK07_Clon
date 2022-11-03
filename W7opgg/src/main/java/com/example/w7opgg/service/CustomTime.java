package com.example.w7opgg.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

public class CustomTime {
    public static String displayTime(LocalDateTime localDateTime){
        LocalDateTime now = LocalDateTime.now();
        if(localDateTime.getYear()!=now.getYear()){
//            2022 2019
            return now.getYear() - localDateTime.getYear()+"년 전";
        }else if(localDateTime.getDayOfYear()!=now.getDayOfYear()){
            if(now.getDayOfYear() - localDateTime.getDayOfYear()>30){
                return (now.getDayOfYear() - localDateTime.getDayOfYear()) / 30 + "달 전";
            }else if(now.getDayOfYear() - localDateTime.getDayOfYear()>7){
                return (now.getDayOfYear() - localDateTime.getDayOfYear()) / 7 + "주 전";
            }
            return now.getDayOfYear() - localDateTime.getDayOfYear()+"일 전";
        }else if(localDateTime.getHour()!=now.getHour()){
            return now.getHour() - localDateTime.getHour()+"시간 전";
        }else{
            return now.getMinute() - localDateTime.getMinute()+"분 전";
        }
    }
}
