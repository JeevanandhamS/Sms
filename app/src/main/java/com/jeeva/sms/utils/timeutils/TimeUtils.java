package com.jeeva.sms.utils.timeutils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Jeeva on 23/3/16.
 */
public class TimeUtils {

    private static final String JUST_NOW = "Just Now";

    private static final String MINUTE = "minute";

    private static final String HOUR = "hour";

    private static final String DAY = "day";

    private static final String WEEK = "week";

    private static final String MONTH = "month";

    private static final String YEAR = "year";

    private static final String AGO = "ago";

    public static String getTimeAgoString(String startDate, String currentFormat,
                                          String timeZone) throws ParseException {
        return getTimeAgoString(getTimeAgo(startDate, currentFormat, timeZone));
    }

    public static String getTimeAgoString(TimeAgo timeAgo) {
        String timeUnitString = "";
        switch (timeAgo.timeUnit) {
            case MILLISECOND:
            case SECOND:
                return JUST_NOW;
            case MINUTE:
                timeUnitString = MINUTE;
                break;
            case HOUR:
                timeUnitString = HOUR;
                break;
            case DAY:
                timeUnitString = DAY;
                break;
            case WEEK:
                timeUnitString = WEEK;
                break;
            case MONTH:
                timeUnitString = MONTH;
                break;
            case YEAR:
                timeUnitString = YEAR;
                break;
        }
        return timeAgo.timeDiff + " " + timeUnitString + (timeAgo.timeDiff > 1 ? "s" : "") + " " + AGO;
    }

    public static TimeAgo getTimeAgo(String startDate, String currentFormat,
                                     String timeZone) throws ParseException {
        return calcTimeAgo(getMillisecondsFromDateString(startDate,currentFormat, timeZone),
                Calendar.getInstance().getTimeInMillis());
    }

    public static TimeAgo calcTimeAgo(long startTime, long endTime) {
        long milliSecondDiff = endTime - startTime;
        long daysDifference = getDaysDifference(milliSecondDiff);

        if (daysDifference <= 0) {
            long[] splitTime = getTimeFromLongValue(milliSecondDiff);
            if (splitTime[0] > 0) {
                return new TimeAgo(splitTime[0], TimeUnit.HOUR);
            } else if (splitTime[1] > 0) {
                return new TimeAgo(splitTime[1], TimeUnit.MINUTE);
            } else if (splitTime[2] > 0) {
                return new TimeAgo(splitTime[2], TimeUnit.SECOND);
            } else {
                return new TimeAgo(splitTime[3], TimeUnit.MILLISECOND);
            }
        } else {
            if (daysDifference < 7) {
                return new TimeAgo(daysDifference, TimeUnit.DAY);
            } else if (daysDifference < 30) {
                return new TimeAgo(daysDifference / 7, TimeUnit.WEEK);
            }

            int monthsDifference = ((int) daysDifference / 30);
            if (monthsDifference < 12) {
                return new TimeAgo(monthsDifference, TimeUnit.MONTH);
            } else {
                return new TimeAgo(findYearDiff(startTime, endTime), TimeUnit.YEAR);
            }
        }
    }

    public static String calcTimeAgoAndReturnText(long startTime, long endTime) {
        return getTimeAgoString(calcTimeAgo(startTime, endTime));
    }

    public static long[] getTimeFromLongValue(long value) {
        long milliseconds = value % 1000;
        long seconds = (value / 1000) % 60;
        long minutes = (value / (1000 * 60)) % 60;
        long hours = (value / (1000 * 60 * 60)) % 24;
        return new long[]{hours, minutes, seconds, milliseconds};
    }

    public static int findYearDiff(long startTime, long endTime) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endTime);

        int yearDiff = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);

        if (endCalendar.get(Calendar.DAY_OF_YEAR) < startCalendar.get(Calendar.DAY_OF_YEAR)) {
            yearDiff--;
        }

        return yearDiff;
    }

    public static long getFormattedUnitDiff(long diff, java.util.concurrent.TimeUnit currentUnit,
                                            java.util.concurrent.TimeUnit neededUnit) {
        return neededUnit.convert(diff, currentUnit);
    }

    public static long getDaysDifference(long diff) {
        return getFormattedUnitDiff(diff, java.util.concurrent.TimeUnit.MILLISECONDS,
                java.util.concurrent.TimeUnit.DAYS);
    }

    public static long getMillisecondsFromDateString(String dateString, String currentFormat, String timeZone)
            throws ParseException {
        Date date = getDateFromDateString(dateString, currentFormat, timeZone);
        return date.getTime();
    }

    public static String getDateStringFromDate(Date date, String neededFormat) {
        SimpleDateFormat format = new SimpleDateFormat(neededFormat);
        return format.format(date);
    }

    public static Date getDateFromDateString(String dateString, String format,
                                             String timeZone) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(format);
        if (null != timeZone) {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        return simpleDateFormat.parse(dateString);
    }

    public static String getDateStringFromDate(Date date, String format, String timeZone) {
        DateFormat gmtSimpleDateFormat = new SimpleDateFormat(format);
        if (null != timeZone) {
            gmtSimpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        return gmtSimpleDateFormat.format(date);
    }

    public static String getDateStringFromMs(long time, String neededFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return getDateStringFromDate(calendar.getTime(), neededFormat);
    }

    public static String formatDateString(String dateString, String currentFormat, String requiredFormat,
                                          String currentFormatTimeZone, String requiredFormatTimeZone)
            throws ParseException {
        return getDateStringFromDate(getDateFromDateString(dateString, currentFormat, currentFormatTimeZone),
                requiredFormat, requiredFormatTimeZone);
    }

    public static Date getDateFromDateString(String date, String currentFormat) {
        try {
            return (new SimpleDateFormat(currentFormat).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFormattedDateString(String date, String neededFormat,
                                                String currentFormat, String timeZone)
            throws ParseException {
        Date formDate = getDateFromDateString(date, currentFormat, timeZone);
        String finalFormat = new SimpleDateFormat(neededFormat).format(formDate);
        return finalFormat;
    }
}