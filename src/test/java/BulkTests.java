import cn.zy.www.ClientFactory;
import cn.zy.www.demo2.bean.Book;
import cn.zy.www.demo2.utils.DateUtil;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.core.util.Assert;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.*;

/**
 * @program: elastic
 * @description: 通过Bulk将数据批量插入ES
 * @author: 赖键锋
 * @create: 2018-08-21 22:03
 **/
public class BulkTests {
/*
    // 在Test中 Autowired需要引入包 org.elasticsearch.plugin:transport-netty4-client:6.3.2，否则异常找不到Transport类
    private static Client client;

    static {
        try {
            client = ClientFactory.buildClient();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }*/

    private static String bookIndex = "bookdb_index";

    private static String bookType = "book";

    private static Gson gson = new GsonBuilder().setDateFormat("YYYY-MM-dd").create();

    /**
     * 创建索引，设置 settings，设置mappings
     */
    public static void createIndex(Client client) {
        int settingShards = 1;
        int settingReplicas = 0;

        // 判断索引是否存在，存在则删除
        IndicesExistsResponse indicesExistsResponse = client.admin().indices().prepareExists(bookIndex).get();

        if (indicesExistsResponse.isExists()) {
            System.out.println("索引 " + bookIndex + " 存在！");
            // 删除索引，防止报异常  ResourceAlreadyExistsException[index [bookdb_index/yL05ZfXFQ4GjgOEM5x8tFQ] already exists
            DeleteIndexResponse deleteResponse = client.admin().indices().prepareDelete(bookIndex).get();
            if (deleteResponse.isAcknowledged()){
                System.out.println("索引" + bookIndex + "已删除");
            }else {
                System.out.println("索引" + bookIndex + "删除失败");
            }


        } else {
            System.out.println("索引 " + bookIndex + " 不存在！");
        }

        // 设置Settings
        CreateIndexResponse response = client.admin().indices().prepareCreate(bookIndex)
                .setSettings(Settings.builder()
                        .put("index.number_of_shards", settingShards)
                        .put("index.number_of_replicas", settingReplicas))
                .get();

        // 查看结果
        GetSettingsResponse getSettingsResponse = client.admin().indices()
                .prepareGetSettings(bookIndex).get();
        System.out.println("索引设置结果");
        for (ObjectObjectCursor<String, Settings> cursor : getSettingsResponse.getIndexToSettings()) {
            String index = cursor.key;
            Settings settings = cursor.value;
            Integer shards = settings.getAsInt("index.number_of_shards", null);
            Integer replicas = settings.getAsInt("index.number_of_replicas", null);
            System.out.println("index:" + index + ", shards:" + shards + ", replicas:" + replicas);

         /*   Assert.(java.util.Optional.of(settingShards), java.util.Optional.of(shards));
            Assert.assertEquals(java.util.Optional.of(settingReplicas), java.util.Optional.of(replicas));*/
        }
    }

    /**
     * Bulk 批量插入数据
     */
    public static void bulk(Client client) throws  Exception{
        List<Book> list = DateUtil.batchData();

        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

        // 添加index操作到 bulk 中
        list.forEach(book -> {
            // 新版的API中使用setSource时，参数的个数必须是偶数，否则需要加上 setSource(json, XContentType.JSON)
            //上面这这句话有错 对于参数来说  insert 对象所有都必须是jaon 请求 所有东西都必须是json 对象，
            bulkRequestBuilder.add(client.prepareIndex(bookIndex, bookType, book.getId()).setSource(gson.toJson(book), XContentType.JSON));
            //bulkRequestBuilder.add(client.prepareDelete(bookIndex,bookType,book.getId()));


        });

       /* bulkRequestBuilder.add(client.prepareUpdate(bookIndex,bookType,"2").
                setDoc(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("publisher","zy")
                        .endObject()));
*/

        Map map = new HashMap();
        map.put("publisher","zy");// 这个很方便啊 可以知道很多啊
        bulkRequestBuilder.add(client.prepareUpdate(bookIndex,bookType,"2").
                setDoc(map));
        BulkResponse responses = bulkRequestBuilder.get();
        if (responses.hasFailures()) {
            // bulk有失败
            for (BulkItemResponse res : responses) {
                System.out.println(res.getFailure());
            }
            Assert.requireNonEmpty(false);
        }
    }


    public static void main(String args[]) throws Exception {


        Client client = ClientFactory.buildClient();
        //createIndex(client);

        bulk(client);
        client.close();
    }
}
