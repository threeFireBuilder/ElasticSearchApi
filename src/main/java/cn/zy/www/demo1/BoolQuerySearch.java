package cn.zy.www.demo1;

import cn.zy.www.ClientFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

/**
 * Created by zy on 2017/12/29.
 * 使用bool的组合条件进行检索
 */
public class BoolQuerySearch {
    public static void main(String[] args)throws  Exception{
        Client client = ClientFactory.buildClient();

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("brand","宝马"))
                .mustNot(QueryBuilders.termQuery("name.raw","宝马318"))
                .should(QueryBuilders.rangeQuery("produce_date").gte("2017-01-01").lte("2017-01-31"))
                .filter(QueryBuilders.rangeQuery("price").gte(280000).lte(350000));
        SearchResponse response  = client.prepareSearch("car_shop")
                .setTypes("cars")
                .setQuery(queryBuilder)
                .get();
        for(SearchHit hitFields :response.getHits()){
            System.out.println(hitFields.getSourceAsString());
        }


        client.close();


    }
}
