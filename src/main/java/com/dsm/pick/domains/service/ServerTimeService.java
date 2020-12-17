package com.dsm.pick.domains.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ServerTimeService {

    public String getMonthAndDate(LocalDate date) {
        int intMonth = date.getMonth().getValue();
        int intDayOfMonth = date.getDayOfMonth();

        String month;
        String dayOfMonth;

        if(intMonth < 10) {
            month = "0" + intMonth;
        } else {
            month = String.valueOf(intMonth);
        }

        if(intDayOfMonth < 10) {
            dayOfMonth = "0" + intDayOfMonth;
        } else {
            dayOfMonth = String.valueOf(intDayOfMonth);
        }

        return month + dayOfMonth;
    }

    public String getDayOfWeek(LocalDate date) {
        String[] dayOfWeeks = {"월", "화", "수", "목", "금", "토", "일"};

        int intDayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeeks[intDayOfWeek - 1];
    }
}
