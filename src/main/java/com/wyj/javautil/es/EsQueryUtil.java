package com.wyj.javautil.es;

import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wyj.javautil.es.EsUtil.getIndexDate;


public class EsQueryUtil {

    @Autowired
    protected Client client;


    public static void main(String[] args) {
        TimeRangeQ timeRangeQ = new TimeRangeQ();
        timeRangeQ.setBeganTime(Long.valueOf("1572577230000"));
        timeRangeQ.setEndTime(Long.valueOf("1574737230000"));
        //String indexs = EsUtil.getIndexDate("bro_", timeRangeQ);
        String[] result = getIndexList("bro_", timeRangeQ);
        EsQueryUtil esQueryUtil = new EsQueryUtil();
        System.out.println(esQueryUtil.getFlowSum(timeRangeQ, null));
    }

    //select ip1+ip ,sum(flow1),sum(flow2),sum(flow3) from es group by ip1+ip2 order by flow3
    public SearchResponse getTopNInfo(Map<String, Object> object, List<String> orgFilterIpList) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (query == null) return null;
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty((String) object.get("proto"))) {  //协议
            MatchQueryBuilder queryMtch = QueryBuilders.matchQuery("proto", object.get("proto").toString());
            query.must(queryMtch);
        }
        if (StringUtils.isNotEmpty((String) object.get("service"))) { //服务
            MatchQueryBuilder queryMtch = QueryBuilders.matchQuery("service", object.get("service").toString());
            query.must(queryMtch);
        }

        TermsAggregationBuilder tb = AggregationBuilders.terms("all_ip_stat")
                .script(new Script("doc['ip_dst_addr'].value+'='+doc['ip_src_addr'].value"));

        String sortStr = (String) object.get("sort");
        if (StringUtils.isEmpty(sortStr)) {
            sortStr = "desc";
        }
        Boolean sort = false;
        if (sortStr.equals("desc")) {
            sort = false;
        } else if (sortStr.equals("asc")) {
            sort = true;
        }
        String sortFiled = (String) object.get("sortFiled");
        if (StringUtils.isEmpty(sortFiled)) {
            sortFiled = "total_bytes";
        }
        tb.order(BucketOrder.aggregation(sortFiled, sort));

        tb.subAggregation(AggregationBuilders.sum("total_bytes")
                .script(new Script("doc['orig_ip_bytes'].value+doc['resp_ip_bytes'].value")));
        tb.subAggregation(AggregationBuilders.sum("up_total_bytes").field("orig_ip_bytes"));
        tb.subAggregation(AggregationBuilders.sum("down_total_bytes").field("resp_ip_bytes"));
        tb.size(100);
        qb.filter(query);
        String index = "bro_index_";
        SearchRequestBuilder searchRequestBuilder = initBuilder(index).setQuery(qb)
                .addAggregation(tb);

        SearchResponse response;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }


    //select sum(flow1),sun(flow2),sum(flow1+flow2) from es
    public SearchResponse getFlowSum(TimeRangeQ time, List<String> orgFilterIpList) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        BoolQueryBuilder qb = QueryBuilders.boolQuery();

        RangeQueryBuilder rangQuery = QueryBuilders.rangeQuery("timestamp").from(time.getBeganTime()).to(time.getEndTime())
                .format("epoch_millis");
        query.must(rangQuery);

        /**
         * 根据访问权限过滤数据
         */
        if (orgFilterIpList != null && orgFilterIpList.size() > 1) {
            // must中的或者条件
            BoolQueryBuilder orgQuery = QueryBuilders.boolQuery();
            for (String ip : orgFilterIpList) {
                MatchQueryBuilder srcOrgMatch = QueryBuilders.matchQuery("ip", ip.trim());
                orgQuery.should(srcOrgMatch);
            }
            /**
             * must中嵌套should其格式为{"must":{"bool":{"should":{"range":{"ip_dst_addr" : {
             * "from" : "192.168.0.0", "to" : "192.168.255.255"}}}}}}
             */
            query.must(orgQuery);
        } else if (orgFilterIpList != null && orgFilterIpList.size() == 1) {
            MatchQueryBuilder srcOrgMatch = QueryBuilders.matchQuery("ip", orgFilterIpList.get(0).trim());
            query.must(srcOrgMatch);
        }

        SumAggregationBuilder sb = AggregationBuilders.sum("total_bytes")
                .script(new Script("doc['up_bytes'].value+doc['down_bytes'].value"));
        SumAggregationBuilder upSb = AggregationBuilders.sum("up_bytes").field("up_bytes");
        SumAggregationBuilder downSb = AggregationBuilders.sum("down_bytes").field("down_bytes");

        qb.filter(query);
        String index = getIndexDate("flow_index_", time);
        SearchRequestBuilder searchRequestBuilder = initBuilder(index).setQuery(qb)
                .addAggregation(sb).addAggregation(upSb).addAggregation(downSb).setSize(0);

        //searchRequestBuilder.setSize((Integer) object.get("numPerPage")).setFrom(from).setExplain(true);
        SearchResponse response;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (SearchPhaseExecutionException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    public static String[] getIndexList(String indexName, TimeRangeQ time) {
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
            return result.toArray(new String[result.size()]);
        }

        if (!beganMonth.equals(endMonth)) {
            String monthIndexName = indexName + beganYear + ".";
            List<String> years = DateUtil.getDiffList(beganMonth, endMonth, "MM", "m");
            result = years.stream().map(month -> monthIndexName + month + "*").collect(Collectors.toList());
            return result.toArray(new String[result.size()]);
        }

        if (!beganDay.equals(endDay)) {
            String dayIndexName = indexName + beganYear + "." + beganMonth + ".";
            List<String> years = DateUtil.getDiffList(beganDay, endDay, "dd", "d");
            result = years.stream().map(day -> dayIndexName + day + "*").collect(Collectors.toList());
            return result.toArray(new String[result.size()]);
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
            return result.toArray(new String[result.size()]);
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
        return result.toArray(new String[result.size()]);
    }

    public SearchRequestBuilder initBuilder(String dataSource) {
        SearchRequestBuilder builder = client.prepareSearch(dataSource.split(",")).setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        return builder;
    }
}
