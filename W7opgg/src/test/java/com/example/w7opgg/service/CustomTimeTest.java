package com.example.w7opgg.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CustomTimeTest {


    @Test
    void TimeToStringTest(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time1 = LocalDateTime.of(2020, 8, 20, 18, 30);
        LocalDateTime time2 = LocalDateTime.of(2022, 8, 20, 18, 40);
        LocalDateTime time3 = LocalDateTime.of(2022, 10, 1, 19, 30);
        LocalDateTime time4 = LocalDateTime.of(2022, 10, 30, 19, 30);
        LocalDateTime time5 = LocalDateTime.of(2022, 11, 1, 10, 30);
        LocalDateTime time6 = LocalDateTime.of(2022, 11, 1, 15, 30);


        System.out.println(CustomTime.displayTime(now));
        System.out.println(CustomTime.displayTime(time1));
        System.out.println(CustomTime.displayTime(time2));
        System.out.println(CustomTime.displayTime(time3));
        System.out.println(CustomTime.displayTime(time4));
        System.out.println(CustomTime.displayTime(time5));
        System.out.println(CustomTime.displayTime(time6));
    }
}