package com.uzm.common.java.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

public class TimeUtils {

    private static List<String> TIME_UNITS;

    static {
        TIME_UNITS = Arrays.stream(TimeUnit.values()).map(name -> name.name().toUpperCase()).collect(Collectors.toList());
        TIME_UNITS.add("WEEKS");
    }

    public static long getTime(String[] args) {
        int modifier = 0;
        String arg = args[2].toLowerCase();

        if (arg.startsWith("horas"))
            modifier = 3600;
        else if (arg.startsWith("minutos"))
            modifier = 60;
        else if (arg.startsWith("segundos"))
            modifier = 1;
        else if (arg.startsWith("semanas"))
            modifier = 604800;
        else if (arg.startsWith("dias"))
            modifier = 86400;
        else if (arg.startsWith("anos"))
            modifier = 31449600;
        else if (arg.startsWith("meses"))
            modifier = 2620800;
        double time;

        time = Double.parseDouble(args[1]);
        return (long) (modifier * time) * 1000;
    }

    public static String getTimeUntil(long epoch) {
        epoch -= System.currentTimeMillis();
        return getTime(epoch);
    }

    public static String getTime(long ms) {
        ms = (long) Math.ceil(ms / 1000.0);
        StringBuilder sb = new StringBuilder(40);

        if (ms / 31449600 > 0) {
            long years = ms / 31449600;
            sb.append(years).append(years == 1 ? " ano " : " anos ");
            ms -= years * 31449600;
        }
        if (ms / 2620800 > 0) {
            long months = ms / 2620800;
            sb.append(months).append(months == 1 ? " mês " : " meses ");
            ms -= months * 2620800;
        }

        if (ms / 604800 > 0) {
            long weeks = ms / 604800;
            sb.append(weeks).append(weeks == 1 ? " semana " : " semanas ");
            ms -= weeks * 604800;
        }
        if (ms / 86400 > 0) {
            long days = ms / 86400;
            sb.append(days).append(days == 1 ? " dia " : " dias ");
            ms -= days * 86400;
        }
        if (ms / 3600 > 0) {
            long hours = ms / 3600;
            sb.append(hours).append(hours == 1 ? " hora " : " horas ");
            ms -= hours * 3600;
        }
        if (ms / 60 > 0) {
            long minutes = ms / 60;
            sb.append(minutes).append(minutes == 1 ? " minuto " : " minutos ");
            ms -= minutes * 60;
        }
        if (ms > 0) {
            sb.append(ms).append(ms == 1 ? " segundo " : " segundos ");
        }
        if (sb.length() > 1) {
            sb.replace(sb.length() - 1, sb.length(), "");
        } else {
            sb = new StringBuilder("...");
        }

        return sb.toString();
    }

    public static String formatDateForKey(long date) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(date));
        return c.get(Calendar.DAY_OF_MONTH) + ":" +
                (c.get(Calendar.MONTH) + 1) + ":" +
                c.get(Calendar.YEAR) + ":" +
                c.get(Calendar.HOUR_OF_DAY) + ":" +
                c.get(Calendar.MINUTE);
    }

    public static String formatDateComplete(long time) {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dt.format(new Date(time));
    }

    public static String formatDate(long time) {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        return dt.format(new Date(time));
    }

    public static long stringToMillis(String toSplit) {
        long millis = 0L;
        if (toSplit.split(" ").length == 2) {
            String[] splitted = toSplit.split(" ");
            if (TIME_UNITS.contains(splitted[1].toUpperCase()) && StringUtils.isNumeric(splitted[0])) {
                if (splitted[1].equalsIgnoreCase("weeks"))
                    millis = TimeUnit.DAYS.toMillis(Integer.parseInt(splitted[0]) * 7L);
                else
                    millis = TimeUnit.valueOf(splitted[1].toUpperCase()).toMillis(Integer.parseInt(splitted[0]));
            }
        }
        return millis;
    }

    public static Calendar toCalendar(long time) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        return c;
    }

    public static String getMonthFormated(long time) {
        return StringUtils.MONTHS[toCalendar(time).get(Calendar.MONTH)];
    }

    public static String getSeasonDateFormat(long time) {
        Calendar c = toCalendar(time);
        return c.get(Calendar.WEEK_OF_MONTH) + "º semana de " + StringUtils.MONTHS[c.get(Calendar.MONTH)];
    }

}
