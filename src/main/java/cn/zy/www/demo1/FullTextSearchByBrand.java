package cn.zy.www.demo1;

import cn.zy.www.ClientFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

/**
 * Created by zy on 2017/12/29.
 * 这快好乱啊
 * 全文检索
 */
public class FullTextSearchByBrand {
    public static void main(String[] args)throws Exception{

        Client client = ClientFactory.buildClient();

        //全文检索，品牌是宝马的数据,单个条件
        SearchResponse response = client.prepareSearch("car_shop")
                .setTypes("cars")
                .setQuery(QueryBuilders.matchQuery("brand","宝马"))
                .get();
        for(SearchHit searchHit:response.getHits()){
            System.out.println(searchHit.getSourceAsString());
        }
        System.out.println("===============================================");
        //多条件全文检索
        response = client.prepareSearch("car_shop")
                .setTypes("cars")
                .setQuery(QueryBuilders.multiMatchQuery("宝马","brand","name"))
                .get();
        for(SearchHit searchHit:response.getHits()){
            System.out.println(searchHit.getSourceAsString());
        }
        System.out.println("===============================================");
        //分组查询
        response = client.prepareSearch("car_shop")
                .setTypes("cars")
                .setQuery(QueryBuilders.termQuery("name.raw","宝马318"))
                .get();
        for(SearchHit searchHit:response.getHits()){
            System.out.println(searchHit.getSourceAsString());
        }
        System.out.println("===============================================");
        //前缀查询
        response = client.prepareSearch("car_shop")
                .setTypes("cars")
                .setQuery(QueryBuilders.prefixQuery("name","宝"))
                .get();
        for(SearchHit searchHit:response.getHits()){
            System.out.println(searchHit.getSourceAsString());
        }

        client.close();
    }
}
