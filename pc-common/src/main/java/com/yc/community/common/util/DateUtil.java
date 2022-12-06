package com.yc.community.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getCurYearAndMonth(){
        String str = getCurrentYear() + "";
        int currentMonth = getCurrentMonth();
        if(currentMonth < 10)
            str = str + "-0" + currentMonth;
        else
            str = str + "-"+ currentMonth;
        return str;
    }

    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        DateUtil.getDaysOfMonth(new SimpleDateFormat("yyyy-MM-dd").parse(month + "-02"));
    }

}
