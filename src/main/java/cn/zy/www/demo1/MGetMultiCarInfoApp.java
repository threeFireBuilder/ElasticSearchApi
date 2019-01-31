package cn.zy.www.demo1;

import cn.zy.www.ClientFactory;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.Client;

/**
 * Created by zy on 2017/12/29.
 * MGet一次性拿出多条数据
 */
public class MGetMultiCarInfoApp {
    public static void main(String[] args) throws Exception{
        Client client = ClientFactory.buildClient();

        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("car_shop","cars","1")
                .add("car_shop","cars","2")
                .get();
        for(MultiGetItemResponse itemResponse : multiGetItemResponses){
            GetResponse response = itemResponse.getResponse();
            if(response.isExists()){
                System.out.println(response.getSourceAsString());
            }
        }
        client.close();
    }
}
