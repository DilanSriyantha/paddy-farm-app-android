package com.dtechsolutions.paddyfarm.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeFormatter {
    private static final String PATTERN = "yyyy/MM/dd hh:mm:ss a";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);

    public static DateTimeFormatter instance;

    private DateTimeFormatter() {}

    public static DateTimeFormatter getInstance() {
        if(instance == null)
            instance = new DateTimeFormatter();

        return instance;
    }

    public String format(Date date) {
        return sdf.format(date);
    }

    public String format(String date) {
        return sdf.format(date);
    }
}
