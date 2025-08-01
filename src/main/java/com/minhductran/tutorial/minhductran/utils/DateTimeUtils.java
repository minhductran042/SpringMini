package com.minhductran.tutorial.minhductran.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Chuyển đổi milliseconds thành thời gian cụ thể dạng ISO 8601
     */
    public static String millisecondsToDateTime(long milliseconds) {
        Instant instant = Instant.now().plusMillis(milliseconds);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.toString();
    }
    
    /**
     * Chuyển đổi milliseconds thành thời gian cụ thể dạng đẹp
     */
    public static String millisecondsToReadableDateTime(long milliseconds) {
        Instant instant = Instant.now().plusMillis(milliseconds);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.format(FORMATTER);
    }
    
    /**
     * Chuyển đổi milliseconds thành thời gian tương đối (ví dụ: "1 giờ", "7 ngày")
     */
    public static String millisecondsToRelativeTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days + " ngày";
        } else if (hours > 0) {
            return hours + " giờ";
        } else if (minutes > 0) {
            return minutes + " phút";
        } else {
            return seconds + " giây";
        }
    }
} 