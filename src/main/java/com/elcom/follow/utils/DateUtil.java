package com.elcom.follow.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    public static long getDaysBetweenTwoDates(String strDate1, String strDate2) {

        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date1 = myFormat.parse(strDate1);
            Date date2 = myFormat.parse(strDate2);
            return TimeUnit.DAYS.convert(date2.getTime() - date1.getTime(), TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static long minutesFromTwoTimes(DateTimeFormatter fmt, String firstTime, String secondTime) {
        try {
            LocalTime t1 = LocalTime.parse(firstTime, fmt);
            LocalTime t2 = LocalTime.parse(secondTime, fmt);
            long result = ChronoUnit.MINUTES.between(t1, t2);
            return result < 0 ? 0 : result;
        } catch (Exception ex) {
            System.out.println("DateUtil.minutesFromTwoTimes.ex: " + ex.toString());
        }
        return 0;
    }

    public static boolean validateFormat(String s, String format) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(s);
            if (!s.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }

    public static Date getDayOfThisMonth(int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static Date cacularDate(Date dateFrom, int value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFrom);
        cal.add(Calendar.DATE, value);
        return cal.getTime();
    }

    public static Date stringToDateReport(String dateInString) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {

            date = formatter.parse(dateInString);
            System.out.println(date);
            System.out.println(formatter.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String changeFormat(String s, String inputFormat, String outFormat) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        return toString(formatter.parse(s), outFormat);
    }

    public static Date toDate(String s, String format, Date defaultVal) throws ParseException {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.parse(s);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static Date toDate(String s, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(s);
    }

    public static String toString(Date s, String format) throws ParseException {
        if (s == null) {
            return "";
        }

        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(s);
    }

    public static Date add(Date dt, int calendar, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, amount);
        dt = c.getTime();
        return dt;
    }

    public static Date addSecond(Date dt, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.SECOND, amount);
        dt = c.getTime();
        return dt;
    }

    public static Date addMiliSecond(Date dt, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MILLISECOND, amount);
        dt = c.getTime();
        return dt;
    }

    public static Date addHour(Date dt, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.HOUR, amount);
        dt = c.getTime();
        return dt;
    }

    public static Date addDay(Date dt, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, amount);
        dt = c.getTime();
        return dt;
    }

    public static boolean isValidDate(String value) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            df.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidFormat(String value) {
        String format = "yyyy-MM-dd";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public static Date addMonth(Date dt, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, amount);
        dt = c.getTime();
        return dt;
    }

    public static int getNextMonthIntValue(Date currDate) {

        Calendar c = Calendar.getInstance();
        c.setTime(currDate);
        c.add(Calendar.MONTH, 1);
        return Integer.parseInt(new SimpleDateFormat("MM").format(c.getTime()));
    }

    public static String today(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    public static Integer subtract(Date dt1, Date dt2) {
        long diff = Math.abs(dt1.getTime() - dt2.getTime());
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return Integer.valueOf(String.valueOf(diffDays));
    }

    public static String getPartitionNameOfNextMonth() {
        String yyyyMmDd = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth()).toString();
        String[] arr = yyyyMmDd.split("-");
        String year = arr[0];
        String month = arr[1];
        if( month.length() == 1 )
            month = "0" + month;
        return "p_" + year + "_" + month;
    }
    
    public static String getPartitionValueOfNextMonth() {
        return LocalDate.now().plusMonths(1).with(TemporalAdjusters.firstDayOfNextMonth()).toString() + " 00:00:00";
    }
    
//    public static void main(String[] args) {
//        LocalDate localDate = LocalDate.now();
//        System.out.println("Day of Month: " + localDate.getDayOfMonth());
//        System.out.println("Month: " + localDate.getMonth());
//        System.out.println("Year: " + localDate.getYear());
//
//        System.out.printf("first day of Month: %s%n",
//                localDate.with(TemporalAdjusters.firstDayOfMonth()));
//        System.out.printf("first Monday of Month: %s%n", localDate
//                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)));
//        System.out.printf("last day of Month: %s%n",
//                localDate.with(TemporalAdjusters.lastDayOfMonth()));
//        System.out.printf("first day of next Month: %s%n",
//                localDate.with(TemporalAdjusters.firstDayOfNextMonth()).toString());
//        System.out.printf("first day of next Year: %s%n",
//                localDate.with(TemporalAdjusters.firstDayOfNextYear()));
//        System.out.printf("first day of Year: %s%n",
//                localDate.with(TemporalAdjusters.firstDayOfYear()));
//
//        LocalDate tomorrow = localDate.plusDays(1);
//        System.out.println("Day of Month: " + tomorrow.getDayOfMonth());
//        System.out.println("Month: " + tomorrow.getMonth());
//        System.out.println("Year: " + tomorrow.getYear());
//    }
    
    public static String getMonthAndYearForSelectPartition(Date from) {
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        String month = (cal.get(Calendar.MONTH) + 1) + "";
        if( month.length()==1 )
            month = "0" + month;
        
        String year = cal.get(Calendar.YEAR) + "";

        return "p_" + year + "_" + month;
    }
    
    public static Integer getDayOfMonth(Date from) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Integer getMonth(Date from) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        return cal.get(Calendar.MONTH);
    }

    public static Integer getYear(Date from) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        return cal.get(Calendar.YEAR);
    }

    public static Date getLastDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public Timestamp toTimestamp(Date data) {
        return new java.sql.Timestamp(data.getTime());
    }

    //compute by milisecons
    public static long getDateDiff(Date startDate, Date endDate, TimeUnit timeUnit) {
        long diffInMillies = endDate.getTime() - startDate.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static long dateToLong(String format, String date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        Date inputDate;
        try {
            inputDate = simpleDateFormat.parse(date);
            return inputDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static Date stringToDateByForm(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        try {

            date = formatter.parse(dateString);
            System.out.println(date);
            System.out.println(formatter.format(date));
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
}
