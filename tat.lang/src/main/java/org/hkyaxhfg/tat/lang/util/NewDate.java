package org.hkyaxhfg.tat.lang.util;

import org.apache.commons.lang3.ArrayUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 作为{@link java.util.Date}的扩展.
 *
 * @author: wjf
 * @date: 2022/1/10
 */
public class NewDate extends Date {

    private static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    private static final String yyyy_MM_dd = "yyyy-MM-dd";
    private static final String HH_mm_ss = "HH:mm:ss";

    // ***** PUBLIC *****

    public NewDate() {}

    public NewDate(Date date) {
        super(date.getTime());
    }

    /**
     * 格式化日期。默认为[yyyy-MM-dd].
     *
     * @param pattern 格式化模板字符串.
     * @return yyyy-MM-dd格式的时间字符串表示.
     */
    public String formatDate(String... pattern) {
        return formatDate(this, pattern);
    }

    /**
     * 格式化时间。默认为[HH:mm:ss].
     *
     * @param pattern 格式化模板字符串.
     * @return HH:mm:ss格式的时间字符串表示.
     */
    public String formatTime(String... pattern) {
        return formatTime(this, pattern);
    }

    /**
     * 格式化日期时间。默认为[yyyy-MM-dd HH:mm:ss].
     *
     * @param pattern 格式化模板字符串.
     * @return yyyy-MM-dd HH:mm:ss格式的时间字符串表示.
     */
    public String formatDateTime(String... pattern) {
        return formatDateTime(this, pattern);
    }

    /**
     * 获取当前时间.
     *
     * @return NewDate.
     */
    public static NewDate now() {
        return new NewDate();
    }

    /**
     * 格式化日期。默认为[yyyy-MM-dd].
     *
     * @param pattern 格式化模板字符串.
     * @return yyyy-MM-dd格式的时间字符串表示.
     */
    public static String formatDate(Date date, String... pattern) {
        pattern = ArrayUtils.isEmpty(pattern) ? new String[]{yyyy_MM_dd} : pattern;
        return newDateFormat(pattern[0]).format(date);
    }

    /**
     * 格式化时间。默认为[HH:mm:ss].
     *
     * @param pattern 格式化模板字符串.
     * @return HH:mm:ss格式的时间字符串表示.
     */
    public static String formatTime(Date date, String... pattern) {
        pattern = ArrayUtils.isEmpty(pattern) ? new String[]{HH_mm_ss} : pattern;
        return newDateFormat(pattern[0]).format(date);
    }

    /**
     * 格式化日期时间。默认为[yyyy-MM-dd HH:mm:ss].
     *
     * @param pattern 格式化模板字符串.
     * @return yyyy-MM-dd HH:mm:ss格式的时间字符串表示.
     */
    public static String formatDateTime(Date date, String... pattern) {
        pattern = ArrayUtils.isEmpty(pattern) ? new String[]{yyyy_MM_dd_HH_mm_ss} : pattern;
        return newDateFormat(pattern[0]).format(date);
    }

    /**
     * 解析日期。默认为[yyyy-MM-dd].
     *
     * @param pattern 格式化模板字符串.
     * @return yyyy-MM-dd格式的时间表示.
     */
    public static Date parseDate(String date, String... pattern) {
        pattern = ArrayUtils.isEmpty(pattern) ? new String[]{yyyy_MM_dd} : pattern;
        return internalParse(date, pattern[0]);
    }

    /**
     * 解析时间。默认为[HH:mm:ss].
     *
     * @param pattern 格式化模板字符串.
     * @return HH:mm:ss格式的时间表示.
     */
    public static Date parseTime(String date, String... pattern) {
        pattern = ArrayUtils.isEmpty(pattern) ? new String[]{HH_mm_ss} : pattern;
        return internalParse(date, pattern[0]);
    }

    /**
     * 解析日期时间。默认为[yyyy-MM-dd HH:mm:ss].
     *
     * @param pattern 格式化模板字符串.
     * @return yyyy-MM-dd HH:mm:ss格式的时间字符串表示.
     */
    public static Date parseDateTime(String date, String... pattern) {
        pattern = ArrayUtils.isEmpty(pattern) ? new String[]{yyyy_MM_dd_HH_mm_ss} : pattern;
        return internalParse(date, pattern[0]);
    }

    /**
     * 获取给定时间的那天的开始时间.
     *
     * @param date date.
     * @return date-start.
     */
    public static Date startOfDay(Date... date) {
        date = ArrayUtils.isEmpty(date) ? new Date[]{ now() } : date;
        Calendar calendar = getCalendar(date[0]);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取给定时间的那天的结束时间.
     *
     * @param date date.
     * @return date-end.
     */
    public static Date endOfDay(Date... date) {
        date = ArrayUtils.isEmpty(date) ? new Date[]{ now() } : date;
        Calendar calendar = getCalendar(date[0]);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取给定时间的那周的开始时间.
     *
     * @param date date.
     * @return date-start.
     */
    public static Date startOfWeek(Date... date) {
        date = ArrayUtils.isEmpty(date) ? new Date[]{ now() } : date;
        Calendar calendar = getCalendar(date[0]);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return startOfDay(calendar.getTime());
    }

    /**
     * 获取给定时间的那周的结束时间.
     *
     * @param date date.
     * @return date-end.
     */
    public static Date endOfWeek(Date... date) {
        date = ArrayUtils.isEmpty(date) ? new Date[]{ now() } : date;
        Calendar calendar = getCalendar(date[0]);
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        return endOfDay(calendar.getTime());
    }

    /**
     * 获取给定时间的那月的开始时间.
     *
     * @param date date.
     * @return date-start.
     */
    public static Date startOfMonth(Date... date) {
        date = ArrayUtils.isEmpty(date) ? new Date[]{ now() } : date;
        Calendar calendar = getCalendar(date[0]);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return startOfDay(calendar.getTime());
    }

    /**
     * 获取给定时间的那月的结束时间.
     *
     * @param date date.
     * @return date-end.
     */
    public static Date endOfMonth(Date... date) {
        date = ArrayUtils.isEmpty(date) ? new Date[]{ now() } : date;
        Calendar calendar = getCalendar(date[0]);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return endOfDay(calendar.getTime());
    }

    /**
     * 获取给定时间的那周的开始时间。(中国人习惯[周一 ---> 周日]).
     *
     * @param date date.
     * @return date-start.
     */
    public static Date startOfChineseWeek(Date... date) {
        date = ArrayUtils.isEmpty(date) ? new Date[]{ now() } : date;
        Calendar calendar = getCalendar(date[0]);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -(dayOfWeek - Calendar.MONDAY));
        return startOfDay(calendar.getTime());
    }

    /**
     * 获取给定时间的那周的结束时间。(中国人习惯[周一 ---> 周日]).
     *
     * @param date date.
     * @return date-end.
     */
    public static Date endOfChineseWeek(Date... date) {
        date = ArrayUtils.isEmpty(date) ? new Date[]{ now() } : date;
        Calendar calendar = getCalendar(date[0]);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return endOfDay(date);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, (Calendar.SATURDAY - dayOfWeek) + 1);
        return endOfDay(calendar.getTime());
    }

    /**
     * 创建一个新的日期格式化.
     *
     * @param pattern 格式化模板字符串.
     * @return DateFormat.
     */
    public static DateFormat newDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 获取一个指定时间的日历.
     *
     * @param date date.
     * @return Calendar.
     */
    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 格式化日期时间。默认为[yyyy-MM-dd HH:mm:ss].
     *
     * @param pattern 格式化模板字符串.
     * @return yyyy-MM-dd HH:mm:ss格式的时间字符串表示.
     */
    public String format(String... pattern) {
        pattern = ArrayUtils.isEmpty(pattern) ? new String[]{yyyy_MM_dd_HH_mm_ss} : pattern;
        return newDateFormat(pattern[0]).format(this);
    }

    // ***** PRIVATE *****

    /**
     * 解析日期.
     *
     * @param date 日期字符串.
     * @param pattern 格式化模板字符串.
     * @return NewDate.
     */
    private static NewDate internalParse(String date, String pattern) {
        try {
            return new NewDate(newDateFormat(pattern).parse(date));
        } catch (ParseException e) {
            throw TatException.newEx(e.toString());
        }
    }

    // ***** CLASS *****


}
