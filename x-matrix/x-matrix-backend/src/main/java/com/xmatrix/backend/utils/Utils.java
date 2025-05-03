package com.xmatrix.backend.utils;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

public class Utils {

    // Calculate advancement based on the difference between start and end dates
    public static long calculatePeriodLength(Date start, Date end) {
        // Normalize start and end dates to 00:00:00 of the day
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);

        // Calculate the difference in milliseconds
        long durationInMillis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();

        // Convert milliseconds to days (1 day = 86400000 milliseconds)
        return durationInMillis / (1000 * 60 * 60 * 24);
    }
}
