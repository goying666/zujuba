package renchaigao.com.zujuba.util;

import android.content.Intent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class dateUse {

    public static String GameBeginCountDown(String dateString){
        //日期 + 时间 ，返回 天数、小时数、分钟数、秒数四部分中的两部分，以左边为准。
//        String retDay,retHour,retMin,retSecond;
        Date beginGameDate = StringToDate(dateString);
//        Date beginGameDate = StringToDate("2018-09-15 16:37:52");
        Calendar calBeginGame = Calendar.getInstance();
        Calendar calNow = Calendar.getInstance();
        calBeginGame.setTime(beginGameDate);
        calNow.setTime(new Date());
        Long retHour,retMin,retSecond;
        retHour = (calBeginGame.getTimeInMillis() - calNow.getTimeInMillis())/(1000*60*60);
        retMin = (calBeginGame.getTimeInMillis() - calNow.getTimeInMillis())%(1000*60*60)/(1000*60);
        retSecond = (calBeginGame.getTimeInMillis() - calNow.getTimeInMillis())%(1000*60)/1000;
        if (retHour > 0){
            return retHour.toString()+"时"+retMin.toString()+"分";
        }else {
            if (retMin>0){
                return retMin.toString()+"分"+retSecond.toString()+"秒";
            }else
                return  retSecond.toString()+"秒";
        }
    }

    public static String getTodayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(formatter.format(new Date()));
        return formatter.format(new Date());
    }

    public static String DateToString(Date dateValue) {
        if (dateValue != null) {
            DateFormat mediumDateFormat = DateFormat.getDateTimeInstance
                    (DateFormat.MEDIUM, DateFormat.MEDIUM);
            return mediumDateFormat.format(dateValue);
        } else {
            return null;
        }
    }

    public static Date StringToDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateret = formatter.parse(dateStr);
            return dateret;
        } catch (ParseException e) {
            System.out.println(e);
        }
        return null;
    }

    public static String GetStringDateNow() {
        return DateToString(new Date());
    }

    //    public static Date GetFormatDateNow(){
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    }
    /* 功能：比较token创建时间是否超时
     *  参数：默认超过2天不登录就算超时 */
    public static boolean tokenDateCompare(Date date) {
        Calendar calendarToken = Calendar.getInstance();
        Calendar calendarNow = Calendar.getInstance();
        calendarToken.setTime(date);
        calendarNow.setTime(new Date());
        if ((calendarNow.getTimeInMillis() - calendarToken.getTimeInMillis()) / (1000 * 60 * 60 * 24) < 2)
            return true;
        else
            return false;
    }
}
