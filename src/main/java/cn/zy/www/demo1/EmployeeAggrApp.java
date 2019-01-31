package cn.zy.www.demo1;

import cn.zy.www.ClientFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by zy on 2017/12/19.
 */
public class EmployeeAggrApp {
    public static void main(String[] args) throws Exception{
        Client client = ClientFactory.buildClient();

        SearchResponse response = client.prepareSearch("company")
                .addAggregation(AggregationBuilders.terms("group_by_country").field("country")
                .subAggregation(AggregationBuilders.dateHistogram("group_by_join_date").field("join_date")
                .dateHistogramInterval(DateHistogramInterval.YEAR)
                        .subAggregation(AggregationBuilders.avg("avg_salary").field("salary"))))
                .execute().get();


        Map<String,Aggregation> map = response.getAggregations().asMap();
        StringTerms groupByCountry = (StringTerms) map.get("group_by_country");
        Iterator<StringTerms.Bucket> groupByCountryBucketIterator = groupByCountry.getBuckets().iterator();
        while (groupByCountryBucketIterator.hasNext()){
            Bucket groupByCountryBucket = groupByCountryBucketIterator.next();
            System.out.println(groupByCountryBucket.getKey()+":"+groupByCountryBucket.getDocCount());
            Histogram groupByJoinDate = (Histogram) groupByCountryBucket.getAggregations().asMap().get("group_by_join_date");
            Iterator<Histogram.Bucket> hisBucket = (Iterator<Histogram.Bucket>) groupByJoinDate.getBuckets().iterator();
            while (hisBucket.hasNext()){
                Histogram.Bucket groupByJoinDateBucket = hisBucket.next();
                System.out.println(groupByJoinDateBucket.getKey() + ":" +groupByJoinDateBucket.getDocCount());
                Avg avg = (Avg) groupByJoinDateBucket.getAggregations().asMap().get("avg_salary");
                System.out.println(avg.getValue());
            }
        }

        System.out.println();
        client.close();
    }
}
