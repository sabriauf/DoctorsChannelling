package lk.hemas.ayubo.util;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lk.hemas.ayubo.R;

/**
 * Created by Sabri on 3/12/2018. time modifier
 */

public class TimeFormatter {

    public static final String TIME_FORMAT_DAY = "d";
    public static final String TIME_FORMAT_WEEK_DAY = "EEEE, MMM yyyy";
    public static final String TIME_FORMAT_WEEK_SHORT = "EE";
    public static final String TIME_FORMAT_APPOINTMENT_DATE = "MMMM d, yyyy";
    public static final String DATE_FORMAT_SHORT = "d-MM-yyyy";
    public static final String TIME_FORMAT = "hh:mm aa";
    public static final String CHANNEL_DATE_FORMAT = "MM/d/yyyy hh:mm:ss aa";
    public static final String SCHEDULE_DATE_FORMAT = "yyyy/MM/d";
    public static final String DOCTOR_DATE_FORMAT = "d/MM/yyyy";
    public static final String DATE_FORMAT_VIDEO = "yyyy-MM-d";
    public static final String DATE_FORMAT_DOB = "dd-MMM-yyyy";

    public final static long ONE_SECOND = 1000;
    private final static long SECONDS = 60;

    private final static long ONE_MINUTE = ONE_SECOND * SECONDS;
    private final static long MINUTES = 60;

    private final static long ONE_HOUR = ONE_MINUTE * MINUTES;
    private final static long HOURS = 24;

    private final static long ONE_DAY = ONE_HOUR * HOURS;
    private final static long DAYS_PER_MONTH = 30;

    private final static long ONE_MONTH = ONE_DAY * DAYS_PER_MONTH;
    private final static long MONTHS_PER_YEAR = 12;

    private final static long ONE_YEAR = ONE_MONTH * MONTHS_PER_YEAR;

    public static String millisecondsToString(long milliseconds, String format) {
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(new Date(milliseconds));
    }

//    public static long getMilisecondsForYear(String year) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Integer.parseInt(year), 0, 1);
//        return calendar.getTime().getTime();
//    }

    public static Date stringToDate(String dateString, String format) {
        Date date = null;
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getRelativeDay(Context context, long duration) {
        long days = getDifferenceInDays(duration);
        if (days == 0) {
            return context.getString(R.string.today);
        } else if (days == 1) {
            return "Tomorrow";
        } else {
            return millisecondsToString(duration, DOCTOR_DATE_FORMAT);
        }
    }

    private static long getDifferenceInDays(long duration) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(new Date(duration));
        long days = (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 365;
        return cal2.get(Calendar.DAY_OF_YEAR) - cal1.get(Calendar.DAY_OF_YEAR) + days;
    }

    public static String getRelativeShort(Context context, long duration) {
        long difference = duration - System.currentTimeMillis();

        if (difference / ONE_DAY > 0) {
            return getRelativeDay(context, duration);
        }

        if (difference / ONE_HOUR > 0) {
            if (difference / ONE_HOUR == 1)
                return "1 Hour";
            else
                return String.valueOf(difference / ONE_HOUR) + " hours";
        }

        if (difference / ONE_MINUTE > 0) {
            if (difference / ONE_MINUTE == 1)
                return "1 min";
            else
                return String.valueOf(difference / ONE_MINUTE) + " mins";
        }

        if (difference / ONE_SECOND > 1) {
            return String.valueOf(difference / ONE_SECOND) + " seconds";
        }

        return "Not Available";
    }
}
