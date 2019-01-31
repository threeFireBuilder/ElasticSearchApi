package cn.zy.www;


import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Title: 客户端建立
 * @Project:
 * @Author: zy
 * @Description:
 * @Date: Create in 21:54 2018/12/23
 */
public class ClientFactory {

    private static TransportClient client;

    public static TransportClient buildClient() throws UnknownHostException {

        if(client==null){
            Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
            client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("139.196.243.11"), 9300));
        }
        return client;
        //***.***.***.*** 表示 ip 地址，本地的话，可以使用 localhost，9300是默认的 api 访问接口

    }




    /**
     * 关闭client
     */
    public static void closeClient() {
        client.close();
    }

    /** * 创建索引 * * @throws Exception */
    public static void createIndex(String indexName)
            throws Exception {
        client = buildClient();
        CreateIndexResponse createIndexResponse = client.admin().
                indices().prepareCreate(indexName).execute() .actionGet();
        System.out.println(createIndexResponse.isAcknowledged());
    }

    public static void main(String args[]) throws Exception {


      /*  TransportClient client = buildClient();

        GetResponse response = client.prepareGet("my_index","my_type","2").execute().actionGet();

        JSONObject jsonObject = JSONObject.fromObject(response);


        System.out.print(jsonObject.toString());*/

        createIndex("javaapia");


    }
}
