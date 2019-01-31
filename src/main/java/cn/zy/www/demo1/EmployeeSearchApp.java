package cn.zy.www.demo1;

import cn.zy.www.ClientFactory;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;

/**
 * Created by zy on 2017/12/19.
 * 员工搜索应用app
 */
public class EmployeeSearchApp {
    public static void main(String[] args) throws  Exception{
        Client client = ClientFactory.buildClient();

        //prepareData(client);
        executeSearch((TransportClient) client);

        //System.out.println(createIndex(client,"test2"));

        client.close();
    }

   /* *//**
     * 创建 empty index
     * @param client
     * @param index
     * @return
     * @throws Exception
     *//*
    public static boolean createIndex(Client client,String index)throws  Exception{

        IndicesAdminClient indicesAdminClient = client.admin().indices();
        CreateIndexResponse response = indicesAdminClient.prepareCreate(index).get();
        return response.isAcknowledged();
    }
*/
    /**
     创建索引 指定setting,创建mapper
     * @param client
     * @param index
     * @return
     */
    public static boolean createIndex(Client client, String index){

        // settings
        Settings settings = Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
                //.put("cluster.name", "poc")
                //.put("node.name", "node1")
                //.put("client.transport.ignore_cluster_name", true)
                //.put("node.client", true)
                //.put("client.transport.sniff", true)
                .build();
        XContentBuilder mappingBuilder;
        try {
            mappingBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(index)
                    //field("type", "string")  字段类型，必填
                    //field("store", "yes")   是否存储
                    //field("analyzer", "ik")所使用的分词器
                    .startObject("properties")
                    .startObject("ID").field("type", "text").field("store", true).endObject()
                    .startObject("IP").field("type", "text").field("store", true).endObject()
                    .startObject("VALUE_DAY").field("type", "text").field("store", true).endObject()
                    .startObject("TYPE_MACHINE").field("type", "text").field("store", true).endObject()
                    .startObject("time").field("type", "text").field("store", true).endObject()
                    .startObject("TYPE").field("type", "text").field("store", true).endObject()
                    .startObject("zs").field("type","text").endObject()
                    .endObject()
                    .endObject()
                    .endObject();
        } catch (Exception e) {
            System.out.println("--------- createIndex 创建 mapping 失败：" + e);
            return false;
        }


        IndicesAdminClient indicesAdminClient = client.admin().indices();
       /* CreateIndexResponse response = indicesAdminClient.prepareCreate(index)
                .setSettings(settings)
                .addMapping(index, mappingBuilder)
                .get();*/
        CreateIndexResponse response = indicesAdminClient.prepareCreate(index)
                .setSettings(settings)
                .addMapping(index,mappingBuilder)
                .get();
        return response.isAcknowledged();

    }


    /**
     * 添加数据
     * @param client
     * @throws Exception
     */
    public static void prepareData(Client client)throws  Exception{
        XContentBuilder builder1 = XContentFactory.jsonBuilder().startObject()
                .field("name","jack")
                .field("age",27)
                .field("position","technique software")
                .field("country","China")
                .field("join_date","2017-01-01")
                .field("salary","10000")
                .endObject();
        client.prepareIndex("company","employee","1").setSource(builder1).get();

        XContentBuilder builder2 = XContentFactory.jsonBuilder().startObject()
                .field("name", "marry")
                .field("age", 35)
                .field("position", "technique manager")
                .field("country", "china")
                .field("join_date", "2017-01-01")
                .field("salary", 12000)
                .endObject();
        client.prepareIndex("company","employee","2").setSource(builder2).get();

        XContentBuilder builder3 = XContentFactory.jsonBuilder().startObject()
                .field("name", "tom")
                .field("age", 32)
                .field("position", "senior technique software")
                .field("country", "china")
                .field("join_date", "2016-01-01")
                .field("salary", 11000)
                .endObject();
        client.prepareIndex("company","employee","3").setSource(builder3).get();

        XContentBuilder builder4 = XContentFactory.jsonBuilder().startObject()
                .field("name", "jen")
                .field("age", 25)
                .field("position", "junior finance")
                .field("country", "usa")
                .field("join_date", "2016-01-01")
                .field("salary", 7000)
                .endObject();
        client.prepareIndex("company","employee","4").setSource(builder4).get();

        XContentBuilder builder5 = XContentFactory.jsonBuilder().startObject()
                .field("name", "mike")
                .field("age", 37)
                .field("position", "finance manager")
                .field("country", "usa")
                .field("join_date", "2015-01-01")
                .field("salary", 15000)
                .endObject();
        client.prepareIndex("company","employee","5").setSource(builder5).get();

    }

    /**
     * 搜索
     * @param client
     */
    public static void executeSearch(TransportClient client){

        MatchQueryBuilder builder = QueryBuilders.matchQuery("position","technique");
/*
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").from(30).to(40);
*/
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").gt(30).lt(40);
        SearchResponse response =  client.prepareSearch("company").setTypes("employee").setQuery(builder)
                .setPostFilter(rangeQueryBuilder)
                .setFrom(0).setSize(1).get();

        SearchHit[] hits = response.getHits().getHits();
        System.out.println(response.toString());
        System.out.println(1234);
        for(int i = 0;i<hits.length;i++){
            System.out.println(hits[i].getSourceAsString());
        }

    }
}
