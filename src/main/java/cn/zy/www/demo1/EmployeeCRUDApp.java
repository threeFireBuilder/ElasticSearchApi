package cn.zy.www.demo1;

import cn.zy.www.ClientFactory;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 * 员工增删改查的应用程序
 * Created by zy on 2017/12/19.
 *
 */
public class EmployeeCRUDApp {

	public static void main(String[] args) throws  Exception{
		Client client = ClientFactory.buildClient();
		addEmploy((TransportClient) client);

	//	undateEmployee(client);
	//	delEmployee(client);
		getEmployee((TransportClient) client);
		client.close();
	}

	public static void addEmploy(TransportClient client) throws Exception{
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
				.field("name","zhangsan")
				.field("age",27)
				.field("position","technique english")
				.field("country","China")
				.field("join_date","2017-01-01")
				.endObject();
		IndexResponse response = client.prepareIndex("company","employee","7")
				.setSource(builder).get();
		System.out.println(response);
		System.out.println(response.getResult());
	}

	public static void delEmployee(TransportClient client){
		DeleteResponse response  = client.prepareDelete("company","employee","6").get();
		System.out.println(response);
		System.out.println(response.getResult());
	}

	public static  void undateEmployee(TransportClient client) throws  Exception{
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
				.field("name","lisi").endObject();
		UpdateResponse response  =  client.prepareUpdate("company","employee","6")
				.setDoc(builder).get();
		System.out.println(response.getResult());
	}

	public static  void getEmployee(TransportClient client){
/*
		GetResponse response = client.prepareGet("company","employee","6").get();
*/

		GetResponse response = client.prepareGet("company","employee","7").get();
		System.out.println(response.getSourceAsString());


		/*QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchQuery("brand","宝马"))
				.mustNot(QueryBuilders.termQuery("name.raw","宝马318"))
				.should(QueryBuilders.rangeQuery("produce_date").gte("2017-01-01").lte("2017-01-31"))
				.filter(QueryBuilders.rangeQuery("price").gte(280000).lte(350000));*/


/*
		QueryBuilder queryBuilder = QueryBuilders.;
*/


	}
}
