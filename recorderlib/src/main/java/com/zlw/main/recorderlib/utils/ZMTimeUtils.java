package com.zlw.main.recorderlib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class ZMTimeUtils {
    public static final int MINUTES =60;
    public static final int HOURS =MINUTES*60;
    public static final int DAYS =HOURS*24;
    public static final int MONTHS =DAYS*30;
    public static final int YEARS =DAYS*365;
    static String format = "yyyy-MM-dd HH:mm:ss";
    static String formatYearDay = "yyyy-MM-dd";
    static String formatMinute = "HH:mm";
    static String formatSecond = "mm:ss";
    static String formatDayMinute = "MM-dd HH:mm";
    static String formatDay = "MM-dd";

    /**
     * 时间精确到天或者分
     * @param time 时间
     * @param isAccurateMinute 是否精确到分 true精确到分 false精确到天
     * @return
     */
    public static String accurateDayOrMinute(long time,boolean isAccurateMinute) {
        if (isAccurateMinute) {
            return accurateMinute(time);
        }
        return accurateDay(time);


    }
    /**
     * 精确到天 如果是当天，则显示为“今天”；当年非当天，则显示为“mm-dd”；
     * 非当年则显示为“yyyy-mm-dd”
     * @param time
     * @return
     */
    private static String accurateDay(long time) {
        if (IsToday(timeStamp2Date(time, format))){
            return "今天";
        }else if (isToYear(timeStamp2Date(time, format))){
            return timeStamp2Date(time, formatDay);
        }
        return timeStamp2Date(time, formatYearDay);
    }

    /**
     * 根据时间戳得到分秒值
     * @param time
     * @return
     */
    public static String getSecondTimes(long time){
        return timeStamp2Date(time,formatSecond);
    }

    /**
     * 精确到分 例如评论时间，如果是当天，则显示为”hh:mm”;当年非当天，则显示为“mm-dd hh:mm”
     * ;非当年则显示为“yyyy-mm-dd hh:mm”
     * @param time
     * @return
     */
    private static String accurateMinute(long time) {
        if (IsToday(timeStamp2Date(time, format))){
            return timeStamp2Date(time, formatMinute);
        }else if (isToYear(timeStamp2Date(time, format))){
            return timeStamp2Date(time, formatDayMinute);
        }
        return timeStamp2Date(time, format);

    }

    public static String getDurtionTime(long time,String format) {
        String sTime = "";
        long publishTime = time;
        long diffrence = System.currentTimeMillis() - publishTime;
        if(diffrence<0){
            return  "刚刚";
        }
        sTime = timeStamp2Date(publishTime,format);
        if (diffrence > DAYS && diffrence < MINUTES) {
            sTime = diffrence / DAYS + "天前";
        } else if (diffrence < DAYS && diffrence >= HOURS) {
            sTime = diffrence / HOURS + "小时前";
        } else if (diffrence < HOURS && diffrence >= MINUTES) {
            sTime = diffrence / MINUTES + "分钟前";
        } else if (diffrence < MINUTES) {
            sTime = diffrence  + "秒前";
        }
        return sTime;
    }
    public static String timeStamp2Date(long seconds, String format) {

        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf;

        sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }

    /**
     * 判断是否为今天
     * @param day
     * @return
     */
    public static boolean IsToday(String day) {
        try {
            Calendar pre = Calendar.getInstance();
            Date predate = new Date(System.currentTimeMillis());
            pre.setTime(predate);
            Calendar cal = Calendar.getInstance();
            Date date = getDateFormat().parse(day);
            cal.setTime(date);
            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == 0) {
                    return true;
                }
            }
        }catch (Exception ex){

        }
        return false;
    }

    /**
     * 是否是今年
     * @param day
     * @return
     */
    public static boolean isToYear(String day) {
        try {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            return true;
        }
    }catch (Exception ex){

    }
        return false;
    }
    public static SimpleDateFormat getDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf;
    }
}
