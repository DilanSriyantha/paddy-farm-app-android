package com.dtechsolutions.paddyfarm.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeFormatter {
    private static final String PATTERN = "yyyy/MM/dd hh:mm:ss a";
    public static DateTimeFormatter instance;

    private DateTimeFormatter() {}

    public static DateTimeFormatter getInstance() {
        if(instance == null)
            instance = new DateTimeFormatter();

        return instance;
    }

    public String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        return sdf.format(date);
    }

    public String format(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        return sdf.format(date);
    }

    public String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public String format(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public String format(Date date, String pattern, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    public String format(String date, String pattern, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }
}
