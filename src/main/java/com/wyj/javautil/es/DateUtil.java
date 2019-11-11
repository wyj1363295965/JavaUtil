/**
 * @工程名称：cargo-ramping
 * @程序包名：com.shenzhenair.ecargo.ramping.support.util
 * @程序类名：DateUtil.java
 * @创建日期：2016年6月27日
 */
package com.wyj.javautil.es;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <pre>
 *
 *       日期操作
 *
 *       大家在使用Date对象输出日期字符串表示的时候，大多会遇见一些不算困难的困难。说他不算困难是因为，大家不是不会做，只是一般繁琐的转化工作使人很烦恼，而大多数人不太原意考虑。有些人可能对这方面有所了解，但可能也不够充分，所以我在这里把我所知道的写出来，一来供大家参考，二来也算是抛砖引玉，希望能有更多的人讨论这个话题。
 *       在JAVA中有一个很有用的类：SimpleDateFormat，它使用模板方法以文本形式格式化输出日期对象。一般的用法如下：
 *       SimpleDateFormat f=new SimpleDateFormat(&quot;MMM dd,yyyy&quot;);
 *       f.format(new Date());
 *
 *       构造SimpleDateFormat使用的字符串就是模扳。这个模扳的表示即
 *
 *       为&quot;February 21th, 2004&quot;的式样。
 *       其他模扳符号定义为：
 *       符号            意义                      显示形式                       举例
 *       G              公元                        文本                         AD
 *       y              年份                        数字                         2001
 *       M              月                          文本或数字                   July或07
 *       d              日                          数字                        10
 *       h              A.M./P.M.制的钟点（1～12）   数字                         12
 *       H              24小时制的钟点（0～23）      数字                           0
 *       m              分钟                        数字                         30
 *       s              秒                          数字                         43
 *       S              毫妙                        数字                         234
 *       E              星期几                      文本                         Tuesday
 *       D              一年中的第几天               数字                          360
 *       F              某月中的第几个星期几         数字                          2
 *       w              一年中的第几个星期           数字                           40
 *       W              一个月中的第几个星期         数字                           1
 *       a              A.M./P.M.标记               文本                         PM
 *       k              24小时制的钟点（1～24）      数字                          24
 *       K              A.M./P.M.制的钟点（0～11）   数字                          0
 *       z              时区                        文本                        Eastern
 *       '              转义符                      分隔符
 *       '             单引号                      符号                        '
 *
 *       这些符号是可以随意组合的，其中，显示形式为&quot;文本&quot;的符号，使用4个或以上的重复组合将输出长文本字符串，反之输出短字符串。
 *       如：E产生Mon,而EEEE则产生Monday。对于显示形式为&quot;文本和数字&quot;的符号，两次以下包括两次的复用产生数字，而两次以上则产生文本。
 *       另外，字符串模板不单可以在构造函数中使用，更可以在程序中动态的指定、变换。
 * </PRE>
 */

public class DateUtil {
    public static String DATE_DATE = "yyyy-M-d";

    public static String DATE_DATEHOUR = "yyyy-M-d H";

    public static String DATE_DATEMINUTE = "yyyy-M-d H:m";
    // 永久时间
    public static String PermanentTime = "2036-01-01 01:01:01";
    /**
     * 一些常用的日期变量定义
     */
    public static String DATE_DATEMONTH = "yyyy-M";

    public static String DATE_DATETIME = "yyyy-M-d H:m:s";

    public static String LONGDATE_DATE = "yyyy-MM-dd";

    public static String LONGDATE_DATEHOUR = "yyyy-MM-dd HH";

    public static String LONGDATE_DATEMINUTE = "yyyy-MM-dd HH:mm";

    /**
     * 24小时制时间, 即例如上面的分钟允许为 5, 8, 在此则为 05 08
     */
    public static String LONGDATE_DATEYEAR = "yyyy";

    public static String LONGDATE_DATEMONTH = "yyyy-MM";

    public static String LONGDATE_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将 date 由 format1 格式转换到 format2 格式
     *
     * @param date        要处理的日期(符合 dateformat1格式)
     * @param dateformat1 格式1
     * @param dateformat2 格式2
     * @param def         如果转换出错,返回 def
     * @return 根据dateformat2格式转换而来的日期字符串
     */
    public static String changeDate(String date, String dateformat1, String dateformat2, String def) {
        String nDate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateformat1);
            Date d = sdf.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat(dateformat2);
            nDate = formatter.format(d);
        } catch (Exception e) {
            nDate = def;
        }

        return nDate;
    }

    /**
     * 根据日期格式取当前日期
     *
     * @param dateformat 日期格式
     * @return 日期时间
     */
    public static String getDate(String dateformat) {
        SimpleDateFormat f = new SimpleDateFormat(dateformat);
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        f.setTimeZone(zone);
        return f.format(new Date());
    }

    /**
     * 日期相加.<BR>
     *
     * @param datetime 日期(格式:YYYY-MM-DD)
     * @param day      加上N天
     * @return 相加后的日期YYYY-MM-DD
     */
    public static String addDate(String datetime, long day) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = formatter.parse(datetime);
            long Time = (date1.getTime() / 1000) + 60 * 60 * day * 24;
            date1.setTime(Time * 1000);
            return formatter.format(date1);
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * Add month
     *
     * @param date
     * @param month
     * @return
     */
    public static Date dateAddMonth(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.MONTH, month);

        return cal.getTime();
    }

    public static Date dateAddDate(Date date, int number, String dateType) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (dateType.equals("y")) {
            cal.add(Calendar.YEAR, number);
        } else if (dateType.equals("m")) {
            cal.add(Calendar.MONTH, number);
        } else {
            cal.add(Calendar.DAY_OF_YEAR, number);
        }


        return cal.getTime();
    }

    /**
     * 时间相加.<BR>
     *
     * @param datetime 日期(格式:YYYY-MM-DD HH:MM:SS)
     * @param minute   加上N分钟
     * @return 返回 YYYY-MM-DD HH:MM:SS
     */
    public static String addTime(String datetime, long minute) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = formatter.parse(datetime);
            long Time = (date1.getTime() / 1000) + 60 * 30;
            date1.setTime(Time * 1000);
            return formatter.format(date1);
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 时间相加.<BR>
     *
     * @param datetime 日期(格式:YYYY-MM-DD HH:MM:SS)
     * @param day      加上N天
     * @return 返回 YYYY-MM-DD HH:MM:SS
     */
    public static String addTimeForDay(String datetime, long day) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = formatter.parse(datetime);
            long Time = (date1.getTime() / 1000) + 60 * 60 * 24 * day;
            date1.setTime(Time * 1000);
            return formatter.format(date1);
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * @函数名称：addDateMinut
     * @创建日期：2017年9月26日
     * @功能说明：在某个日期上面加上或者减去多少小时
     * @参数说明： day 日期(格式:YYYY-MM-DD HH:MM:SS)
     * @参数说明： hour 加上N小时
     * @返回说明：String
     */
    public static String addDateMinut(String day, int hour) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        cal = null;
        return format.format(date);
    }

    /**
     * @函数名称：getDatesBetweenTwoDate
     * @创建日期：2017年9月26日
     * @功能说明：根据开始时间和结束时间返回时间段内的时间集合
     * @参数说明：beginDate 开始时间（格式：yyyy-MM-dd HH:mm:ss）
     * @参数说明：endDate 结束时间（格式：yyyy-MM-dd HH:mm:ss）
     * @参数说明：textFormat 返回数据格式
     * @返回说明：List
     */
    public static List<String> getDatesBetweenTwoDate(Date beginDate, Date endDate, String textFormat) {
        List<String> lDate = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat(textFormat);
        lDate.add(sdf.format(beginDate));// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.MINUTE, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {

                lDate.add(sdf.format(cal.getTime()));
            } else {
                break;
            }
        }
        lDate.add(sdf.format(endDate));// 把结束时间加入集合
        return lDate;
    }

    /**
     * 检查日期,不合法反回真
     *
     * @param date
     * @return boolean 真,假
     */
    public static boolean checkDate(String date) {
        if (date == null || date == "")
            return true;

        try {
            DateFormat fmt = DateFormat.getDateInstance(DateFormat.DEFAULT);
            fmt.parse(date.trim());
        } catch (ParseException e) {
            return true;
        }
        return false;
    }

    /**
     * 检查日期是否正确 YYYYMM
     *
     * @param date 日期
     */
    public static boolean checkYearMonth(String date) {
        if (date == null || date.equalsIgnoreCase(""))
            return true;
        if (date.length() != 6)
            return true;
        try {
            float f1 = new Float(date).floatValue();
            if (f1 < 195001 || f1 > 210001)
                return true;
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    /**
     * 根据传入的format格式化日期.<BR>
     *
     * @param datetime 时期时间
     * @param format   日期格式
     * @return 返回处理后的日期时间字符串
     */
    public static String formatDate(String datetime, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            TimeZone zone = TimeZone.getTimeZone("GMT+8");
            formatter.setTimeZone(zone);
            Date date1 = formatter.parse(datetime);
            return formatter.format(date1);
        } catch (Exception e) {
        }
        return "";
    }

    public static String formatDate(Date datetime, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            TimeZone zone = TimeZone.getTimeZone("GMT+8");
            formatter.setTimeZone(zone);
            return formatter.format(datetime);
        } catch (Exception e) {
        }
        return "";
    }

    public static Long datetimeStrToLong(String datetime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(LONGDATE_DATETIME);
            Date date = formatter.parse(datetime);
            return date.getTime();
        } catch (Exception e) {
        }
        return 0l;
    }

    public static Date parse(String text, String format) {
        Date result = null;

        if (StringUtils.isEmpty(format)) {
            format = LONGDATE_DATETIME;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        sdf.setTimeZone(zone);
        try {
            result = sdf.parse(text);
        } catch (ParseException ex) {
            result = new Date();
        }

        return result;
    }

    public static Timestamp parseTimestamp(String text, String format) {
        return new Timestamp(parse(text, format).getTime());
    }

    /**
     * 取当前日期函数 yyyy-MM-dd.<BR>
     *
     * @return 返回当前日期字符串
     */
    public static String getCurrentDate() {
        return getDate(0);
    }

    /**
     * 取当前日期时间函数 yyyy-MM-dd HH:mm:ss.<BR>
     *
     * @return 返回当前日期时间字符串
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        f.setTimeZone(zone);
        String d = f.format(new Date());
        return d;
    }

    /**
     * 取当前时间函数 HH:mm:ss.<BR>
     *
     * @return 返回当前时间字符串
     */
    public static String getCurrentTime() {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        f.setTimeZone(zone);
        String d = f.format(new Date());
        return d;
    }

    /**
     * 取当前时间函数 HH:mm:ss.<BR>
     *
     * @return 返回当前时间字符串
     */
    public static String getCurrentTimeGMT() {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        String d = f.format(new Date());
        return d;
    }

    /**
     * @函数名称：getCurrentDateStr
     * @创建日期：2009-7-31
     * @功能说明：得到
     * @参数说明：
     * @返回说明：
     */
    public static String getCurrentDateStr() {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        f.setTimeZone(zone);
        String d = f.format(new Date());
        return d;
    }

    /**
     * @函数名称：getCurrentTimeStr
     * @创建日期：2009-7-31
     * @功能说明：得到格式为“yyyyMMddHHmmss”的日期时间
     * @参数说明：
     * @返回说明：
     */
    public static String getCurrentTimeStr() {
        SimpleDateFormat f = new SimpleDateFormat("yyMMddHHmmss");
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        f.setTimeZone(zone);
        String d = f.format(new Date());
        return d;
    }

    /**
     * @函数名称：getCurrentTimeStrAll
     * @创建日期：2009-7-31
     * @功能说明：得到格式为“yyyyMMddHHmmssSSS”的GMT+8时区的当前日期时间
     * @参数说明：
     * @返回说明：
     */
    public static String getCurrentTimeStrAll() {
        SimpleDateFormat f = new SimpleDateFormat("yyMMddHHmmssSSS");
        TimeZone zone = TimeZone.getTimeZone("GMT+8");//
        f.setTimeZone(zone);
        String d = f.format(new Date());
        return d;
    }

    /**
     * @函数名称：getCurrentTimeStrAllGMT
     * @创建日期：2009-7-31
     * @功能说明：得到格式为“yyyyMMddHHmmssSSS”的GMT时区的当前日期时间
     * @参数说明：
     * @返回说明：
     */
    public static String getCurrentTimeStrAllGMT() {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        TimeZone zone = TimeZone.getTimeZone("GMT+8");// +8
        f.setTimeZone(zone);
        String d = f.format(new Date());
        return d;
    }

    /**
     * @return
     * @函数名称：getDate
     * @创建日期：2008-8-1
     * @功能说明：
     * @参数说明：dayCount long 当前天的前面第几天
     * @返回说明：指定日期字符串
     */
    public static String getDate(long dayCount) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        formatter.setTimeZone(zone);

        Date myDate = new Date();
        try {
            long myTime = (myDate.getTime() / 1000) - 60 * 60 * dayCount * 24;
            myDate.setTime(myTime * 1000);
        } catch (Exception e) {
        }
        return formatter.format(myDate);
    }

    /**
     * @return
     * @函数名称：getDate
     * @创建日期：2008-8-1
     * @功能说明：
     * @参数说明：dayCount long 当前天的前面第几天
     * @参数说明：format String 日期格式
     * @返回说明：指定日期字符串
     */
    public static String getDate(long dayCount, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        formatter.setTimeZone(zone);

        Date myDate = new Date();
        try {
            long myTime = (myDate.getTime() / 1000) + 60 * 60 * dayCount * 24;
            myDate.setTime(myTime * 1000);
        } catch (Exception e) {
        }
        return formatter.format(myDate);
    }

    /**
     * @函数名称：getDate
     * @创建日期：2008-8-1
     * @功能说明：得到参考日期的过去或者将来的日期
     * @参数说明： dateTime String 参考日期 <BR>
     * dayCount int 过去或者将来日期的周期数（正数表示将来，负数表示过去） <BR>
     * dayType String 周期性（YEAR-年，MONTH-月，DAY-日，HOUR－时，MINUTE－分，SECOND－秒）
     * @返回说明：
     */
    public static String getDate(String dateTime, int periodCount, String dayType) {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+8"));

        calendar.setTime(strToDate(dateTime, LONGDATE_DATETIME));

        String formatStr = LONGDATE_DATETIME;
        if (dayType.equals("YEAR")) {// 如果周期是“年”
            calendar.add(Calendar.YEAR, periodCount);
            // formatStr = LONGDATE_DATEYEAR;
        } else if (dayType.equals("MONTH")) {// 如果周期是“月”
            calendar.add(Calendar.MONTH, periodCount);
            // formatStr = LONGDATE_DATEMONTH;
        } else if (dayType.equals("DAY")) {// 如果周期是“日”
            calendar.add(Calendar.DAY_OF_MONTH, periodCount);
            // formatStr = LONGDATE_DATE;
        } else if (dayType.equals("HOUR")) {// 如果周期是“时”
            calendar.add(Calendar.HOUR, periodCount);
            // formatStr = LONGDATE_DATEHOUR;
        } else if (dayType.equals("MINUTE")) {// 如果周期是“分”
            calendar.add(Calendar.MINUTE, periodCount);
            // formatStr = LONGDATE_DATEMINUTE;
        } else if (dayType.equals("SECOND")) {// 如果周期是“秒”
            calendar.add(Calendar.SECOND, periodCount);
            // formatStr = LONGDATE_DATETIME;
        }

        Date date = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat(formatStr);
        String dateStr = df.format(date);

        return dateStr;
    }

    /**
     * 取一个yyyy-mm-dd日期的日
     *
     * @param date yyyy-mm-dd
     * @return 2位日期或者""
     */
    public static String getDateDay(String date) {
        String str = "";
        try {
            str = date.substring(8, 10);
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * 取一个yyyy-mm-dd日期的月
     *
     * @param date yyyy-mm-dd
     * @return 2位月或者""
     */
    public static String getDateMonth(String date) {
        String str = "";
        try {
            str = date.substring(5, 7);
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * 取当前日期时间
     *
     * @return 日期时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        f.setTimeZone(zone);
        String d = f.format(new Date());
        return d;

    }

    /**
     * 取当前日期时间
     *
     * @param d_type 1/取-号分隔的日期 2/取:号分隔的日期 3/取-及:分隔的日期时间
     * @return 日期时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime(int d_type) {

        String DateTime = "";
        Calendar now = new GregorianCalendar();
        int y = now.get(Calendar.YEAR);
        int m = now.get(Calendar.MONTH) + 1;
        int d = now.get(Calendar.DAY_OF_MONTH);
        int h = now.get(Calendar.HOUR_OF_DAY);
        int mi = now.get(Calendar.MINUTE);
        int s = now.get(Calendar.SECOND);

        String vm = m < 10 ? "0" + m : "" + m;
        String vd = d < 10 ? "0" + d : "" + d;
        String vh = d < 10 ? "0" + h : "" + h;
        String vmi = mi < 10 ? "0" + mi : "" + mi;
        String vs = s < 10 ? "0" + s : "" + s;

        if (d_type == 1) {
            DateTime = DateTime + y + "-"; // yeah
            DateTime = DateTime + vm + "-"; // month
            DateTime = DateTime + vd; // day
        } else if (d_type == 2) {
            DateTime = DateTime + vh + ":"; // hour
            DateTime = DateTime + vmi + ":"; // minute
            DateTime = DateTime + vs; // second
        } else if (d_type == 3) {
            DateTime = DateTime + y + "-"; // yeah
            DateTime = DateTime + vm + "-"; // month
            DateTime = DateTime + vd + " "; // day
            DateTime = DateTime + vh + ":"; // hour
            DateTime = DateTime + vmi + ":"; // minute
            DateTime = DateTime + vs; // second
        }
        return DateTime;

    }

    /**
     * 取一个yyyy-mm-dd日期的年
     *
     * @param date yyyy-mm-dd
     * @return 4位年或者""
     */
    public static String getDateYear(String date) {
        String str = "";
        try {
            str = date.substring(0, 4);
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * 取两个日期之间的天数.<BR>
     * 参数格式为 yyyy-mm-dd 例如 date1 = 2002-12-11, date2 = 2002-12-13, 返回 2 例如 date1 =
     * 2002-12-13, date2 = 2002-12-11, 返回 -2
     *
     * @param date1 日期一
     * @param date2 日期二
     * @return 整数, 相差天数
     */
    public static long getDiffDate(String date1, String date2) {

        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = myFormatter.parse(date2);
            Date mydate = myFormatter.parse(date1);
            long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
            return day;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @函数名称：getDiffMonthList<br>
     * @创建日期：2016年9月23日<br>
     * @功能说明：根据开始时间和结束时间，求出中间的时间段集合 <br>
     * @参数说明： begin 开始时间<br>
     * @参数说明： end 结束时间<br>
     * @返回说明：List
     */
    public static List getDiffMonthList(String begin, String end, String formats) {

        List<String> dateList = new ArrayList<String>();
        dateList.add(formatDate(parse(begin, formats), formats));
        while (true) {
            begin = formatDate(dateAddMonth(parse(begin, formats), 1), formats);
            dateList.add(begin);
            if (parse(begin, formats).getTime() >= parse(end, formats).getTime()) {
                break;
            }

        }

        return dateList;
    }

    /**
     * 根据开始时间和结束时间，求出中间的时间段集合
     *
     * @param begin    开始时间
     * @param end      结束时间
     * @param formats  传入时间的格式
     * @param dateType 相差时间段类型，y 年，m 月，d 天
     * @return
     */
    public static List getDiffList(String begin, String end, String formats, String dateType) {

        List<String> dateList = new ArrayList<String>();
        dateList.add(formatDate(parse(begin, formats), formats));
        while (true) {
            begin = formatDate(dateAddDate(parse(begin, formats), 1, dateType), formats);

            dateList.add(begin);
            if (parse(begin, formats).getTime() >= parse(end, formats).getTime()) {
                break;
            }

        }

        return dateList;
    }

    public static String formatDate(long datetime, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(datetime);
            TimeZone zone = TimeZone.getTimeZone("GMT+8");
            formatter.setTimeZone(zone);
            return formatter.format(cal.getTime());
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * @函数名称：getDiffMonthList<br>
     * @创建日期：2016年9月23日<br>
     * @功能说明：根据开始时间和结束时间，求出中间的时间段集合 <br>
     * @参数说明： begin 开始时间<br>
     * @参数说明： end 结束时间<br>
     * @返回说明：List
     */
    public static List getDiffMonthList(Date begin, Date end, String format) {
        return getDiffMonthList(formatDate(begin, format), formatDate(end, format), format);
    }

    /**
     * 取两个日期时间之间相隔的分钟数.<BR>
     * 参数格式为 yyyy-MM-dd HH:mm:ss<BR>
     * 例如 date1 = 2002-12-12 22:22:00, date2 = 2002-12-12 22:23:00, 返回 1<BR>
     * 例如 date1 = 2002-12-12 22:23:00, date2 = 2002-12-12 22:22:00, 返回 -1<BR>
     *
     * @param datetime1 日期时间一
     * @param datetime2 日期时间二
     * @return 整数, 相差分钟数
     */
    public static long getDiffMinute(String datetime1, String datetime2) {

        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = myFormatter.parse(datetime2);
            Date mydate = myFormatter.parse(datetime1);
            long day = (date.getTime() - mydate.getTime()) / (1000 * 60);
            return day;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 取两个日期时间之间相隔的秒数.<BR>
     * 参数格式为 yyyy-MM-dd HH:mm:ss<BR>
     * 例如 date1 = 2012-12-12 22:22:22, date2 = 2012-12-12 22:22:23, 返回 1<BR>
     * 例如 date1 = 2012-12-12 22:22:23, date2 = 2012-12-12 22:22:22, 返回 -1<BR>
     *
     * @param datetime1 日期时间一
     * @param datetime2 日期时间二
     * @return 整数, 相差分钟数
     */
    public static long getDiffSecond(String datetime1, String datetime2) {

        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = myFormatter.parse(datetime2);
            Date mydate = myFormatter.parse(datetime1);
            long day = (date.getTime() - mydate.getTime()) / 1000;
            return day;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 按提供的格式取当前日期.<BR>
     *
     * @param get_format 日期格式
     * @return 按格式返回的日期
     */
    public static String getNowDate(String get_format) {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(get_format);
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        sf.setTimeZone(zone);
        String temp = sf.format(date);

        return temp;
    }

    /**
     * 取当前周数<BR>
     * .
     *
     * @param type 1/取本天是一周的第几天 2/取本周是月的第几周 3/取本周是一年的第几周
     * @return 日期时间 yyyy-MM-dd HH:mm:ss
     */
    public static int getWeek(int type) {
        Calendar calendar = new GregorianCalendar();
        int dWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int mWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        int yWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int theWeek = 0;
        if (type == 1) {
            theWeek = dWeek;
        } else if (type == 2) {
            theWeek = mWeek;
        } else if (type == 3) {
            theWeek = yWeek;
        }
        return theWeek;
    }

    /**
     * 是否为正确的日期.<BR>
     * strDate "YYYY-MM-DD" 或者 "YYYY/MM/DD"<BR>
     * allowNull 是否允许为空<BR>
     *
     * @param strDate
     * @param allowNull
     * @return 真或者假
     */
    public static boolean isDate(String strDate, boolean allowNull) {
        int intY, intM, intD;
        int[] standardDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int[] leapyearDays = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (strDate == null || strDate.trim().equals(""))
            return true;

        if (strDate.trim().length() != 10)
            return false;
        strDate = strDate.trim();
        try {
            intY = Integer.parseInt(strDate.substring(0, 4));
            intM = Integer.parseInt(strDate.substring(5, 7));
            intD = Integer.parseInt(strDate.substring(8));
        } catch (Exception e) {
            return false;
        }
        if (intM > 12 || intM < 1 || intY < 1 || intD < 1)
            return false;
        if ((intY % 4 == 0 && intY % 100 != 0) || intY % 400 == 0)
            return (intD <= leapyearDays[intM - 1]);
        return (intD <= standardDays[intM - 1]);
    }

    /**
     * 功能：将字符串型日期转换为指定格式的Date型日期
     *
     * @param dateStr
     * @param dateFormat
     * @return
     */
    public static Date strToDate(String dateStr, String dateFormat) {
        if ("".equals(dateStr) || null == dateStr) {
            return null;
        }
        Date date = null;
        SimpleDateFormat myFormatter = new SimpleDateFormat(dateFormat);
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        myFormatter.setTimeZone(zone);
        try {
            date = myFormatter.parse(dateStr);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return date;
    }

    /**
     * 功能：将字符串型日期转换为Date型日期(yyyy-MM-dd HH:mm:SS)
     *
     * @param dateStr String 字符串型日期
     * @return Date
     */
    public static Date strToLongDate(String dateStr) {
        return strToDate(dateStr, LONGDATE_DATETIME);
    }

    /**
     * @函数名称：getWeek
     * @创建日期：2014-10-25
     * @功能说明：根据日期取得星期几
     * @参数说明：
     * @返回说明：String
     */
    public static String getWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        sdf.setTimeZone(zone);
        String week = sdf.format(date);
        return week;
    }

    /**
     * @函数名称：getTime
     * @创建日期：2017年3月10日
     * @功能说明：根据当前时间获取的时间戳
     * @参数说明：
     * @返回说明：String
     */
    public static long getTime(int dff, int unit) {

        Calendar calendar = Calendar.getInstance();
        if (Calendar.MINUTE == unit) {
            calendar.add(Calendar.MINUTE, dff);
        } else if (Calendar.HOUR == unit) {
            calendar.add(Calendar.HOUR, dff);
        } else if (Calendar.MONTH == unit) {
            calendar.add(Calendar.MONTH, dff);
        } else if (Calendar.DATE == unit) {
            calendar.add(Calendar.DATE, dff);
        } else if (Calendar.YEAR == unit) {
            calendar.add(Calendar.YEAR, dff);
        }

        return calendar.getTime().getTime();
    }

    /**
     * @函数名称：getTimeByMinute
     * @创建日期：2017年3月10日
     * @功能说明：根据当前时间获取相隔多少分钟的时间
     * @参数说明：minute:相隔多少分钟(正数为后面多少分钟，负数为前面多少分钟),formatText：日期格式
     * @返回说明：String
     */
    public static String getTimeFormLong(long time, Integer minute, String formatText) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MINUTE, minute);
        return new SimpleDateFormat(formatText).format(calendar.getTime());
    }

    /**
     * @函数名称：getTenMinutePoint
     * @创建日期：2017年5月16日
     * @功能说明：
     * @参数说明：
     * @返回说明：String
     */
    public static String getTenMinutePoint(Long time, String formatText, Integer interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int minute = calendar.get(Calendar.MINUTE);
        minute = Math.round(minute / interval * interval);// 计算5的整数分钟
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return new SimpleDateFormat(formatText).format(calendar.getTime());
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        // System.out.println(strToDate("2008-07-16 14:20:21:543",
        // DATE_DATETIME));
        // System.out.println(strToDate(MyDate.getCurrentDateTime(),
        // DATE_DATETIME));
        // System.out.println(strToLongDate(MyDate.getCurrentDateTime()));
        // System.out.println(MyDate.getNowDate("yyyy-MM-dd HH:mm:SS:SSS"));
        Calendar cld = Calendar.getInstance();
        int Year = cld.get(Calendar.YEAR);
        int Month = cld.get(Calendar.MONTH) + 1;
        int Day = cld.get(Calendar.DATE);
        int Hour = cld.get(Calendar.HOUR_OF_DAY);
        int Minute = cld.get(Calendar.MINUTE);
        int Second = cld.get(Calendar.SECOND);
        int MilliSecond = cld.get(Calendar.MILLISECOND);

        String currentTime = "" + MilliSecond;
        System.out.println("##"
                + DateUtil.formatDate(DateUtil.parse("1988-05-13 00:00:00", DateUtil.LONGDATE_DATE), "yyyyMMdd"));
        // System.out.println(DateUtil.getDate(DateUtil.getCurrentDateTime(),
        // -8,
        // "HOUR"));
        // System.out.println(DateUtil.strToDate(
        // DateUtil.getDate(DateUtil.getCurrentDateTime(), 8, "HOUR")
        // .substring(0, 10), DateUtil.LONGDATE_DATE));
        // System.out.println(DateUtil.getCurrentTimeStrAll());
        // System.out.println(getCurrentTimeStrAllGMT());
        // System.out.println(DateUtil.getDate(DateUtil.getCurrentDateTime(),
        // -120, "SECOND"));
        // System.out.println(DateUtil.getWeek(new Date()));

        if (false) {
            System.out.println(DateUtil.getDate(DateUtil.getCurrentDateTime(), 3, "MONTH"));
            System.out.println(DateUtil.getDate(DateUtil.getCurrentDateTime(), -3 * 60 * 60, "SECOND"));
            System.out.println(DateUtil.addDate("2007-02-28", 1));
            System.out.println(getCurrentTimeGMT());
        }
        // System.out.println("2010-09-14 03:21:47".replaceAll("-",
        // "").replaceAll(" ", "").replaceAll(":", ""));
        // System.out.println(MyDate.getCurrentTimeStr());
        // System.out.println(MyDate.getDiffSecond("2010-04-07 23:19:20",
        // MyDate.getCurrentDateTime()));
        // System.out.println(MyDate.getDiffSecond("2010-08-30 13:03:10",
        // MyDate.getCurrentDateTime()) < 60 * 10);

        else if (false) {
            Map<String, Integer> keyV = new LinkedHashMap<String, Integer>();

            String[] date = {"2016-01", "2016-08", "2016-12"};

            String cDateS = date[0];
            for (int i = 0; i < date.length; i++) {

                String netDateS = date[i];
                if (netDateS.equals(cDateS)) {
                    keyV.put(date[i], i + 1);
                } else {

                    while (true) {
                        cDateS = formatDate(dateAddMonth(parse(cDateS, LONGDATE_DATEMONTH), 1), LONGDATE_DATEMONTH);

                        if (netDateS.equals(cDateS)) {
                            keyV.put(cDateS, 0);
                            break;
                        } else {
                            keyV.put(cDateS, i + 1);
                        }

                    }
                }

            }

            // System.out.println(JsonUtil.object2Json(keyV));
            //
            // System.out.println(JsonUtil.object2Json(getDiffMonthList("2016-01",
            // "2016-08", LONGDATE_DATEMONTH)));
        }
    }

    /**
     * @函数名称：getBeforMinuteFifteen
     * @创建日期：2017年3月24日
     * @功能说明：获取最近15分钟间隔1分钟的时间数组
     * @参数说明：
     * @返回说明：String[]
     */
    public static String[] getBeforMinuteFifteen(Date date) {
        // 指定小时的多少个15分钟
        int minutePeriod = 15;
        String[] strs = new String[minutePeriod];
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        strs[minutePeriod - 1] = sdf.format(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        Date date_ = null;
        for (int i = 1; i < minutePeriod; i++) {
            calendar.add(calendar.MINUTE, -1);
            date_ = calendar.getTime();
            String putDate = sdf.format(date_);
            strs[minutePeriod - 1 - i] = putDate;
        }

        return strs;
    }

    /**
     * @函数名称：getBeforMinuteStr
     * @创建日期：2017年3月23日
     * @功能说明：获取最近多少个1分钟数组(h/m)
     * @参数说明：dateSize 多少个1分钟
     * @返回说明：String[]
     */
    public static String[] getBeforMinuteStrByDateSize(Date endDate, int dateSize) {
        // 指定15个1分钟
        int minutePeriod = dateSize;
        String[] strs = new String[minutePeriod];
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-HH-mm");

        strs[minutePeriod - 1] = sdf.format(endDate);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(endDate);

        Date date_ = null;
        for (int i = 1; i < minutePeriod; i++) {
            calendar.add(calendar.MINUTE, -1);
            date_ = calendar.getTime();
            String putDate = sdf.format(date_);
            strs[minutePeriod - 1 - i] = putDate;
        }

        return strs;
    }

    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @param format
     * @return
     */
    /**
     * @函数名称：convert2String
     * @创建日期：2018年4月13日
     * @功能说明：长整型数字转换为日期格式的字符串
     * @参数说明：time 长整型日期
     * @参数说明：format 自定义日期格式
     * @返回说明：String
     */
    public static String convert2String(long time, String format) {
        if (time > 0l) {
            if (StringUtils.isEmpty(format))
                format = DateUtil.LONGDATE_DATETIME;
            SimpleDateFormat sf = new SimpleDateFormat(format);
            Date date = new Date(time);
            return sf.format(date);
        }
        return "";
    }

    /**
     * 添加多少天的時間
     *
     * @param day
     * @param days
     * @return
     */
    public static String addDateDay(String day, int days) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        //	System.out.println("front:" + format.format(date)); //显示输入的日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);// 24小时制
        date = cal.getTime();
        //System.out.println("after:" + format.format(date));  //显示更新后的日期
        cal = null;
        return format.format(date);

    }

}