package com.wyj.javautil.es;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ES工具类
 *
 * @author DELL
 */
@Component
public class EsUtil {

    /**
     * 根据ES索引类型和开始结束时间戳拼出所需索引,如bro_index_2019.04.04*
     *
     * @param indexName(参数可为bro_index_;snort_index_)
     * @param time
     * @return
     */
//    public static String getIndexDate(String indexName, TimeRangeQ time) {
//        if (indexName == null || time == null) {
//            return "";
//        }
//
//        Date beganDate = new Date(time.getBeganTime());
//        Date endDate = new Date(time.getEndTime());
//
//        String beganYear = DateUtil.formatDate(beganDate, "yyyy");
//        String beganMonth = DateUtil.formatDate(beganDate, "MM");
//        String beganDay = DateUtil.formatDate(beganDate, "dd");
//        String beganHour = DateUtil.formatDate(beganDate, "HH");
//
//        String endYear = DateUtil.formatDate(endDate, "yyyy");
//        String endMonth = DateUtil.formatDate(endDate, "MM");
//        String endDay = DateUtil.formatDate(endDate, "dd");
//        String endHour = DateUtil.formatDate(endDate, "HH");
//
//        StringBuilder sb = new StringBuilder(indexName);
//        if (!beganYear.equals(endYear)) {
//            sb.append("*");
//            return sb.toString();
//        }
//
//        if (!beganMonth.equals(endMonth)) {
//            sb.append(beganYear);
//            sb.append("*");
//            return sb.toString();
//        }
//
//        if (!beganDay.equals(endDay)) {
//            sb.append(beganYear);
//            sb.append(".");
//            sb.append(beganMonth);
//            sb.append("*");
//            return sb.toString();
//        }
//
//        //只有小时不一样，那么就返回当天作为条件
//        if (!beganHour.equals(endHour)) {
//            sb.append(beganYear);
//            sb.append(".");
//            sb.append(beganMonth);
//            sb.append(".");
//            sb.append(beganDay);
//            sb.append("*");
//            return sb.toString();
//        }
//
//        //年月日小时也一样了，条件具体到小时，但flow_index除外，因为flow_index的索引名没有具体到
//
//        //流量索引的索引名没有具体到小时，所以还是返回到天
//        if (indexName.toLowerCase().startsWith("flow_index")) {
//            sb.append(beganYear);
//            sb.append(".");
//            sb.append(beganMonth);
//            sb.append(".");
//            sb.append(beganDay);
//            sb.append("*");
//            return sb.toString();
//        }
//
//        //其它小时一样的条件都具体返回到小时了
//        sb.append(beganYear);
//        sb.append(".");
//        sb.append(beganMonth);
//        sb.append(".");
//        sb.append(beganDay);
//        sb.append(".");
//        sb.append(beganHour);
//        return sb.toString();
//    }

    /**
     * v3.4.2 优化
     *
     * @param indexName
     * @param time
     * @return 用，分割的索引串
     */
    public static String getIndexDate(String indexName, TimeRangeQ time) {
        if (indexName == null || time == null) {
            return null;
        }

        Date beganDate = new Date(time.getBeganTime());
        Date endDate = new Date(time.getEndTime());

        String beganYear = DateUtil.formatDate(beganDate, "yyyy");
        String beganMonth = DateUtil.formatDate(beganDate, "MM");
        String beganDay = DateUtil.formatDate(beganDate, "dd");
        String beganHour = DateUtil.formatDate(beganDate, "HH");

        String endYear = DateUtil.formatDate(endDate, "yyyy");
        String endMonth = DateUtil.formatDate(endDate, "MM");
        String endDay = DateUtil.formatDate(endDate, "dd");
        String endHour = DateUtil.formatDate(endDate, "HH");

        StringBuilder sb = new StringBuilder(indexName);
        List<String> result = new ArrayList<>();
        if (!beganYear.equals(endYear)) {
            List<String> years = DateUtil.getDiffList(beganYear, endYear, "yyyy", "y");
            result = years.stream().map(year -> indexName + year + "*").collect(Collectors.toList());
            return pliceWithComma(result);
        }

        if (!beganMonth.equals(endMonth)) {
            String monthIndexName = indexName + beganYear + ".";
            List<String> years = DateUtil.getDiffList(beganMonth, endMonth, "MM", "m");
            result = years.stream().map(month -> monthIndexName + month + "*").collect(Collectors.toList());
            return pliceWithComma(result);
        }

        if (!beganDay.equals(endDay)) {
            String dayIndexName = indexName + beganYear + "." + beganMonth + ".";
            List<String> years = DateUtil.getDiffList(beganDay, endDay, "dd", "d");
            result = years.stream().map(day -> dayIndexName + day + "*").collect(Collectors.toList());
            return pliceWithComma(result);
        }

        //只有小时不一样，那么就返回当天作为条件
        if (!beganHour.equals(endHour)) {
            sb.append(beganYear);
            sb.append(".");
            sb.append(beganMonth);
            sb.append(".");
            sb.append(beganDay);
            sb.append("*");
            result.add(sb.toString());
            return pliceWithComma(result);
        }

        //年月日小时也一样了，条件具体到小时，但flow_index除外，因为flow_index的索引名没有具体到

        //流量索引的索引名没有具体到小时，所以还是返回到天
        if (indexName.toLowerCase().startsWith("flow_index")) {
            sb.append(beganYear);
            sb.append(".");
            sb.append(beganMonth);
            sb.append(".");
            sb.append(beganDay);
            sb.append("*");
            return null;
        }

        //其它小时一样的条件都具体返回到小时了
        sb.append(beganYear);
        sb.append(".");
        sb.append(beganMonth);
        sb.append(".");
        sb.append(beganDay);
        sb.append(".");
        sb.append(beganHour);
        result.add(sb.toString());
        return pliceWithComma(result);
    }


    public static void main(String[] args) {
        Date beganDate = new Date(Long.valueOf("1573005861000"));
        Date endDate = new Date(Long.valueOf("1446775461000"));
        List<String> list = DateUtil.getDiffMonthList(beganDate, endDate, "yyyy-MM-dd");
        List<String> list2 = DateUtil.getDiffList("2019.11.01", "2019.11.23", "yyyy.MM.dd", "d");
        list2.stream().forEach(
                System.out::println
        );
        TimeRangeQ timeRangeQ = new TimeRangeQ();
        timeRangeQ.setBeganTime(Long.valueOf("1572577230000"));
        timeRangeQ.setEndTime(Long.valueOf("1574737230000"));
        String indexs = getIndexDate("bro_", timeRangeQ);

        List<String> listIndex = Arrays.asList(indexs);
        listIndex.stream().forEach(
                System.out::println
        );
        System.out.println(String.valueOf(indexs));
    }


    public static TimeRangeQ getTime(String start, String end, String uid) {
        // 时间范围长整型实体
        TimeRangeQ times = new TimeRangeQ();
        SimpleDateFormat formatter;
        //String startDate = start;
        try {
            formatter = new SimpleDateFormat(DateUtil.LONGDATE_DATETIME);
            Date startD;
            Date endDate;
            if (start != null) {
                startD = formatter.parse(start);
            } else {
                startD = formatter.parse(DateUtil.getCurrentDate() + " 00:00:00");
            }
            Long startTime = startD.getTime();
            if (end != null) {
                endDate = formatter.parse(end);
            } else {
                endDate = new Date();
            }
            Long endTime = endDate.getTime();
            //只传uid的时候设置时间范围
            if (StringUtils.isNotEmpty(uid) && StringUtils.isEmpty(start) && StringUtils.isEmpty(end)) {
                String tmpStartDate = DateUtil.addDate(DateUtil.getCurrentDate(), -30);
                times.setBeganTime(DateUtil.datetimeStrToLong(tmpStartDate + " 00:00:00"));
                times.setEndTime(new Date().getTime());
            } else {
                times.setEndTime(endTime);
                times.setBeganTime(startTime);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return times;
    }

    public static String pliceWithComma(Collection<String> args) {
        StringBuffer result = new StringBuffer();
        for (String str : args) {
            if (org.apache.commons.lang3.StringUtils.isEmpty(str)) {
                continue;
            }
            result.append(str + ",");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}
