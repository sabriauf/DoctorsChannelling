package lk.hemas.ayubo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sabri on 3/12/2018. time modifier
 */

public class TimeFormatter {

    public static final String TIME_FORMAT_DAY = "d";
    public static final String TIME_FORMAT_WEEK_DAY = "EEEE, MMM yyyy";
    public static final String TIME_FORMAT_APPOINTMENT_DATE = "MMMM d, yyyy";
    public static final String DATE_FORMAT_SHORT = "d-MM-yyyy";
    public static final String TIME_FORMAT = "hh:mm aa";
    public static final String CHANNEL_DATE_FORMAT = "MM/d/yyyy hh:mm:ss aa";
    public static final String SCHEDULE_DATE_FORMAT = "yyyy/MM/d";

    public static String millisecondsToString(long milliseconds, String format) {
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(new Date(milliseconds));
    }

    public static long getMilisecondsForYear(String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), 0, 1);
        return calendar.getTime().getTime();
    }

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
}
