package com.cyrusinnovation.flatwiki;

import java.util.Calendar;

public class TimeUtils {
    public static long timeInMillis(int year, int month, int day, int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hours, minutes);
        return calendar.getTimeInMillis();
    }
}
