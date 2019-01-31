package cn.zy.www.demo1;

import cn.zy.www.ClientFactory;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 * Created by zy on 2017/12/29.
 * Bulk进行批量操作
 */
public class BulkUploadSalesDataApp {
    public static void main(String[] args) throws  Exception{
        Client client = ClientFactory.buildClient();

        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

        //add
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("car_shop","cars","4")
                .setSource(XContentFactory.jsonBuilder().startObject()
                        .field("brand", "奔驰")
                        .field("name", "奔驰C200")
                        .field("price", 350000)
                        .field("produce_date", "2017-01-05")
                        .field("sale_price", 340000)
                        .field("sale_date", "2017-02-03")
                        .endObject());
        bulkRequestBuilder.add(indexRequestBuilder);

        //update
        UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate("car_shop","cars","5")
                .setDoc(XContentFactory.jsonBuilder()
                .startObject()
                .field("sales_price","290000")
                        .endObject());
        bulkRequestBuilder.add(updateRequestBuilder);

        //del
        DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete("car_shop","cars","2");
        bulkRequestBuilder.add(deleteRequestBuilder);


        BulkResponse responses  = bulkRequestBuilder.get();
        for(BulkItemResponse response : responses.getItems()){
            System.out.println(response.getVersion());
        }
        client.close();
    }
}
