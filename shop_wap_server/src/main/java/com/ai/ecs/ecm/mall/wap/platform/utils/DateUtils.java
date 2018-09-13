package com.ai.ecs.ecm.mall.wap.platform.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	//一天的毫秒数 86400000 = 24*60*60*1000;
    private static final int millisPerDay = 86400000 ;
	
    public static Date StringToDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(date);
        return date1;
    }

    public static Date StringToDateTime(String date)  {
    	try{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = sdf.parse(date);
        return date1;
    	}catch(Exception e){
    		return null;
    	}
    }

    public static String getDateByTimes(Long time) {
        if (time == null) {
            return "";
        }
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(d);
        return date;
    }
    
    public static String getDateTimeByTimes(Long time) {
        if (time == null) {
            return "";
        }
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(d);
        return date;
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    
    public static String formatHms(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }

    public static String formatDateHMS(Date date) {
        if(date==null){
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /***
     * 与当前日期比较
     * 
     * @param date
     * @return
     * 
     * @author Hou.Hui
     * @createDate 2014年10月17日
     */
    public static boolean compareToNowDate(String date) {
        boolean b = false;
        if (date.compareTo(formatDate(new Date())) >= 0) {
            b = true;
        }
        return b;
    }
    
    /**
     * 获取多少天后到期
     * @param date
     * @return
     * @throws ParseException 
     */
    public static long timeToNowDate(String date) throws ParseException {
    	if(StringUtils.isEmpty(date)){
    		return 0L;
    	}
    	    long to = StringToDateTime(date)==null?0L:StringToDateTime(date).getTime();
    	    long from =new Date().getTime();
    	    long distance=(to - from) / (1000 * 60 * 60 * 24);
    	    return distance;
    }

    /***
     * 获取N天后的日期
     * 
     * @param num
     * @return
     * 
     * @author Hou.Hui
     * @createDate 2014年10月17日
     */
    public static String getDateAfterDays(int num) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, num);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    /***
     * 获取N天后的日期
     *
     * @param num
     * @return
     *
     * @author Hou.Hui
     * @createDate 2014年10月17日
     */
    public static String getDateAfterDaysByDate(int num,String day)throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=sdf.parse(day);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, num);
        return sdf.format(cal.getTime());
    }


    /***
     * 与30天后日期比较
     * 
     * @param date
     * @return
     * 
     * @author Hou.Hui
     * @createDate 2014年10月17日
     */
    public static boolean compareToMonthDate(String date) {
        boolean b = false;
        String afterDate = getDateAfterDays(30);
        if (date.compareTo(afterDate) >= 0) {
            b = true;
        }
        return b;
    }
    
    /**
     * 跟当前时间相差天数
     * @param date
     * @return
     */
    public static String differDays(String date){
    	Calendar calNow = Calendar.getInstance();
    	int ret = -1;
		Date endDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			endDate = sdf.parse(date);
			 if(null != endDate && endDate.after(calNow.getTime())){
				  //获得指定时间的Calendar
		          Calendar calTar = Calendar.getInstance();
		          calTar.setTime(endDate);
		 
		          long millisNow = calNow.getTimeInMillis();
		          long millisTar = calTar.getTimeInMillis();
		 
		          //指定时间小于系统时间才处理， 否则返回空字符串
		          if(millisTar > millisNow){
		               ret = (int)((millisTar - millisNow ) /(millisPerDay));
		           }
		          if(ret == 0){
		        	  ret++;
		          }
		         
			 }
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return String.valueOf(ret);
    	
    }


    public static String differTimes(Date date) {
        String resultStr="";
        try {
            Date startDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE,1);
            Date endDate =cal.getTime();
            long diff = endDate.getTime() - startDate.getTime(); // 得到的差值是微秒级别，可以忽略
            long days = diff / millisPerDay;
            long hours = (diff - days * (millisPerDay)) / (1000 * 60 * 60);
            long minutes = (diff - days * (millisPerDay) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (diff - days * (millisPerDay) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
            resultStr= hours + ":"
                    + minutes + ":"
                    + seconds ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /**
     * 跟当前时间相差小时
     * @param startDate
     * @return
     */
    public static int differHours(Date startDate){
        Calendar calNow = Calendar.getInstance();
        int ret = -1;
        try {
            if(null != startDate && startDate.before(calNow.getTime())){
                //获得指定时间的Calendar
                Calendar calTar = Calendar.getInstance();
                calTar.setTime(startDate);

                long millisNow = calNow.getTimeInMillis();
                long millisTar = calTar.getTimeInMillis();

                if(millisTar <= millisNow){
                    ret = (int)((millisNow - millisTar ) /(1000*60*60));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;

    }

    /**
     * 跟当前时间相差分钟
     * @param startDate
     * @return
     */
    public static int differMinute(Date startDate){
        Calendar calNow = Calendar.getInstance();
        int ret = -1;
        try {
            if(null != startDate && startDate.before(calNow.getTime())){
                //获得指定时间的Calendar
                Calendar calTar = Calendar.getInstance();
                calTar.setTime(startDate);

                long millisNow = calNow.getTimeInMillis();
                long millisTar = calTar.getTimeInMillis();

                if(millisTar <= millisNow){
                    ret = (int)((millisNow - millisTar ) /(1000*60));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;

    }

    public static void main(String[] args) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date=format.parse("2018-08-15 8:15:27");
            System.out.println(DateUtils.differMinute(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
