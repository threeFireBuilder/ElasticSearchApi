package cn.zy.www.demo1;

import cn.zy.www.ClientFactory;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 * Created by zy on 2017/12/29.
 * 使用upsert操作，如果没有该条数据，先进行添加，如果存在，则进行修改
 */
public class UpsertCarInfoApp {
    public static void main(String[] args) throws Exception{
        Client client = ClientFactory.buildClient();
        XContentBuilder indexBuilder = XContentFactory.jsonBuilder().startObject()
                .field("brand","宝马")
                .field("name","bmw320")
                .field("price",3100000)
                .field("produce_date","2017-01-01")
                .endObject();

        IndexRequest indexRequest = new IndexRequest("car_shop","cars","1")
                .source(indexBuilder);

        XContentBuilder updateBulider = XContentFactory.jsonBuilder().startObject()
                .field("price",310000)
                .endObject();

        UpdateRequest updateRequest = new UpdateRequest("car_shop","cars","1")
                .doc(updateBulider).upsert(indexRequest);

        UpdateResponse updateResponse = client.update(updateRequest).get();
        System.out.println(updateResponse.getResult().getOp());

        GetResponse getResponse = client.prepareGet("car_shop","cars","1").get();
        System.out.println(getResponse);

        XContentBuilder indexBuilder1 = XContentFactory.jsonBuilder().startObject()
                .field("brand","奔驰")
                .field("name","bz123")
                .field("price",8799000)
                .field("produce_date","2017-01-01")
                .endObject();

        IndexResponse indexResponse = client.
                prepareIndex("car_shop","cars","2").
                setSource(indexBuilder1).get();

        System.out.println(indexResponse);



        client.close();
    }
}
