package cn.zy.www.util;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @Title:
 * @Project: elsstic 创建工具
 * @Author: zy
 * @Description:
 * @Date: Create in 11:37 2019/1/6
 */
public class ClientUtil {

    private Client client;

    public ClientUtil() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化客户端
     * */
    private void init() throws Exception {
        byte[] bs = new byte[] { (byte) 192, (byte) 168, (byte)52, (byte)130 };
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        client = new PreBuiltTransportClient(settings).
                addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("139.196.243.11"), 9300));
    }

    public Client getClient() {
        return client;
    }



}
