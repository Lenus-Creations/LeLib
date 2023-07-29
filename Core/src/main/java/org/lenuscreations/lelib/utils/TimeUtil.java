package org.lenuscreations.lelib.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class TimeUtil {

    private static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static String format(Date date) {
        return DEFAULT_DATE_FORMAT.format(date);
    }

    public static String millisToFormattedDate(long millis, DateFormat format) {
        return format.format(new Date(millis));
    }

    public static String millisToFormattedDate(long millis) {
        return millisToFormattedDate(millis, DEFAULT_DATE_FORMAT);
    }

    public static String millisToHHMMSS(long millis) {
        return TIME_FORMAT.format(new Date(millis));
    }

    public static String secondsToFormattedString(int secs) {
        if (secs <= 0) return "0 seconds";

        int rem = secs % 86400;
        int months = secs / 2592000;
        int weeks = secs / 604800;
        int days = secs / 86400;
        int hours = rem / 3600;
        int minutes = rem / 60 - hours * 60;
        int seconds = rem % 3600- minutes * 60;

        String sMonths = (months > 0 ? " " + months + " month" + (months > 1 ? "s" : 0) : "");
        String sWeeks = (weeks > 0 ? " " + weeks + " week" + (weeks > 1 ? "s" : 0) : "");
        String sDays = (days > 0 ? " " + days + " day" + (days > 1 ? "s" : 0) : "");
        String sHours = (hours > 0 ? " " + hours + " hour" + (hours > 1 ? "s" : 0) : "");
        String sMinutes = (minutes > 0 ? " " + minutes + " minute" + (minutes > 1 ? "s" : 0) : "");
        String sSeconds = (seconds > 0 ? " " + seconds + " second" + (seconds > 1 ? "s" : 0) : "");
        return (sMonths + sWeeks + sDays + sHours + sMinutes + sSeconds).trim();
    }

    public static int parseTime(String time) {
        if (!time.equals("0") && !time.equals("")) {
            String[] lifeMatch = new String[]{"w", "d", "h", "m", "s"};
            int[] lifeInterval = new int[]{604800, 86400, 3600, 60, 1};
            int seconds = 0;

            for(int i = 0; i < lifeMatch.length; ++i) {
                Matcher matcher = Pattern.compile("([0-9]*)" + lifeMatch[i]).matcher(time);
                if (matcher.find()) seconds += Integer.parseInt(matcher.group(1)) * lifeInterval[i];

            }

            return seconds;
        } else {
            return 0;
        }
    }


}
