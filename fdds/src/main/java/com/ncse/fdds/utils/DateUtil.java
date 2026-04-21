package com.ncse.fdds.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    public static DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");

    public static Timestamp getCurrentDate() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getCurrentDateStr(String f) {
        SimpleDateFormat sf = new SimpleDateFormat(f);
        String s = sf.format(getCurrentDate());
        return s;
    }

    public static String getCurrentToDate(Date date) {
        String format = dayFormat.format(date);
        return format;
    }


    public static String addDays(String start, int days, String f) {
        SimpleDateFormat sf = new SimpleDateFormat(f);
        Calendar now = Calendar.getInstance();
        try {
            now.setTime(sf.parse(start));
            now.add(Calendar.DAY_OF_YEAR, days);
            return sf.format(now);
        } catch (ParseException e) {
            throw new RuntimeException("日期转换异常");
        }

    }

    public static String getCurrentDateStr() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String s = f.format(getCurrentDate());
        return s;
    }

    public static Date getNowDate() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String s = f.format(getCurrentDate());
        Date date = null;
        try {
            date = DateUtils.parseDate(s, "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getCurrentDateTimeStr() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = f.format(getCurrentDate());
        return s;
    }

    public static String getCurrentDateTimeStr(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = f.format(date);
        return s;
    }

    public static String getOrderSnTime() {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = f.format(getCurrentDate());
        return s;
    }

    public static long milliSecondToNanao(long millisecond) {
        return millisecond * 1000000;
    }

    public static long secondToNanao(int second) {
        return second * 1000000000L;
    }

    public static Date parse(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateString);
            return date;
        } catch (ParseException e) {
            throw new RuntimeException("日期转换异常");
        }
    }

    public static boolean afterNow(String dateString, int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        Calendar complate = Calendar.getInstance();
        complate.setTime(DateUtil.parse(dateString));
        complate.add(Calendar.DAY_OF_YEAR, days);
        return complate.after(now);
    }

    //获取当前时间的前后几天
    public static String addDay(int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.DAY_OF_YEAR, days);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
    }

    //获取当前时间的前后几天
    public static String addDayDate(int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.DAY_OF_YEAR, days);
        return new SimpleDateFormat("yyyy-MM-dd").format(now.getTime());
    }

    public static Date getDateByString(String s) throws ParseException {
        Date date = null;
        SimpleDateFormat formater = new SimpleDateFormat();
        formater.applyPattern("yyyy-MM-dd");
        date = formater.parse(s);
        return date;
    }

    // 获取上个月第一天
    public static String getBeforeBeginCurrentDateStr() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String first = f.format(calendar.getTime());
        return first;
    }

    // 获取上个月最后一天
    public static String getBeforeEndCurrentDateStr() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        String first = f.format(calendar.getTime());
        return first;
    }

    //获取某个月份的最后一天
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
    }

    //获取某个月份的第一天
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
    }

    public static void main(String[] args) {
        try {
            String s = "2018-10-19";
            String date = getDate(s, 12);
            System.out.println(date);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取某年某月有几天
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static long getTimeMillis(String time) {
        try {
            Date currentDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return currentDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

//	// 获取某年某月有几天
//	public static Date getDateAfter(Date d, int day) {
//		Calendar now = Calendar.getInstance();
//		now.setTime(d);
//		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
//		return now.getTime();
//	}

    /**
     * 日期格式为指定格式的字符串
     *
     * @param pattern 样式，如:yyyy-MM-dd HH:mm:ss
     * @param date
     * @param format
     * @return
     * @throws NullPointerException
     */
    public static String date2str(Date date, DateFormat format)
            throws NullPointerException {
        String result = "";
        try {
            result = format.format(date);
        } catch (NullPointerException e) {
            throw e;
        }
        return result;
    }


    public static boolean isOverlap(String formatName, String startdate1, String enddate1, String startdate2, String enddate2) {
        SimpleDateFormat format = new SimpleDateFormat(formatName);
        Date leftStartDate = null;
        Date leftEndDate = null;
        Date rightStartDate = null;
        Date rightEndDate = null;
        try {
            leftStartDate = format.parse(startdate1);
            leftEndDate = format.parse(enddate1);
            rightStartDate = format.parse(startdate2);
            rightEndDate = format.parse(enddate2);
        } catch (ParseException e) {
            return false;
        }
        return ((leftStartDate.getTime() >= rightStartDate.getTime()) && leftStartDate.getTime() < rightEndDate.getTime()) || ((leftStartDate.getTime() > rightStartDate.getTime()) && leftStartDate.getTime() <= rightEndDate.getTime()) || ((rightStartDate.getTime() >= leftStartDate.getTime()) && rightStartDate.getTime() < leftEndDate.getTime()) || ((rightStartDate.getTime() > leftStartDate.getTime()) && rightStartDate.getTime() <= leftEndDate.getTime());
    }

    /**
     * 计算某个时间 之前的几月的时间
     *
     * @param date  某个时间 格式 yyyy-MM-dd
     * @param month 之前的几个月
     * @return
     */
    public static String getDate(String date, Integer month) throws ParseException {
        SimpleDateFormat sidf = new SimpleDateFormat("yyyy-MM-dd");
        Date dNow = sidf.parse(date);   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.MONTH, -month);  //设置为前3月
        dBefore = calendar.getTime();   //得到前3月的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);    //格式化前3月的时间
        String defaultEndDate = sdf.format(dNow); //格式化当前时间
        System.out.println("三个月之前时间=======" + defaultStartDate);
        System.out.println("当前时间===========" + defaultEndDate);
        return defaultStartDate;
    }

    /**
     * 返回某天的起止时间
     *
     * @param date
     * @return
     */
    public static Date[] getDayRange(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date now1 = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        calendar.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) - 1);
        Date now2 = calendar.getTime();
        return new Date[]{now1, now2};
    }

    /**
     * 返回某月的起止时间
     *
     * @param date
     * @return
     */
    public static Date[] getMonthRange(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        Date month1 = calendar.getTime();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
        Date month2 = calendar.getTime();
        return new Date[]{month1, month2};
    }

}

