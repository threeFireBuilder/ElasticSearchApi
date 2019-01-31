package cn.zy.www.demo2.controller;


import cn.zy.www.ClientFactory;
import cn.zy.www.demo2.bean.Book;
import cn.zy.www.demo2.common.Constants;
import cn.zy.www.demo2.common.Response;
import cn.zy.www.demo2.common.ResponsePage;
import cn.zy.www.demo2.form.BoolForm;
import cn.zy.www.demo2.form.MatchForm;
import cn.zy.www.demo2.service.BasicMatchQueryService;
import org.elasticsearch.client.Client;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: elastic
 * @description: Basic Match Query 全文搜索
 * @author: zy
 * @create: 2018-08-22 00:22
 **/
public class BasicMatchQueryController {

    private BasicMatchQueryService basicMatchQueryService  = new BasicMatchQueryService();

    public BasicMatchQueryController() throws UnknownHostException {
    }

    /**
     * 1.1 对 "guide" 执行全文检索
     * 测试：http://localhost:8080/basicmatch/multimatch?query=guide
     */
    public Response<List<Book>> multiMatch(Client client, String query) {
        return basicMatchQueryService.multiBatch(client,query);
    }

    /**
     * 1.2 指定特定字段检索
     * 测试：http://localhost:8080/basicmatch/match?title=in action&from=0&size=4
     */
    public ResponsePage<List<Book>> match(Client client,MatchForm form) {
        return basicMatchQueryService.match(client,form);
    }

    /**
     * 2 对 "guide" 执行多字段检索
     * 测试：http://localhost:8080/basicmatch/multifield?query=guide
     */
    public Response<List<Book>> multiField(Client client,String query) {
        return basicMatchQueryService.multiField(client,query);
    }

    /**
     * 3、 Boosting提升某字段得分的检索( Boosting): 将“摘要”字段的得分提高了3倍
     * 测试：http://localhost:8080/basicmatch/multifieldboost?query=elasticsearch guide
     */
    public Response<List<Book>> multiFieldboost(Client client, String query) {
        return basicMatchQueryService.multiFieldboost(client,query);
    }

    /**
     * 4、Bool检索( Bool Query)
     * 测试：http://localhost:8080/basicmatch/bool?shouldTitles=Elasticsearch&shouldTitles=Solr&mustAuthors=clinton gormely&mustNotAuthors=radu gheorge
     */
    public Response<List<Book>> bool(Client client, BoolForm form) {
        return basicMatchQueryService.bool(client,form);
    }

    /**
     * 5、 Fuzzy 模糊检索( Fuzzy Queries)
     */
    public Response<List<Book>> fuzzy(Client client,String query) {
        return basicMatchQueryService.fuzzy(client,query);
    }

    /**
     * 6、 Wildcard Query 通配符检索
     * 测试：http://localhost:8080/basicmatch/wildcard?pattern=t*
     */
    public Response<List<Book>> wildcard(Client client,String pattern) {
        return basicMatchQueryService.wildcard(client,Constants.AUTHORS, pattern);
    }


    /**
     * 7、正则表达式检索( Regexp Query)
     * 测试：http://localhost:8080/basicmatch/regexp
     */
    public Response<List<Book>> regexp(Client client,String regexp) {
        // 由于Tomcat的原因，直接接收有特殊字符的 正则表达式 会异常，所以这里写死，不过多探究
        // 若
        regexp = "t[a-z]*y";
        return basicMatchQueryService.regexp(client,Constants.AUTHORS, regexp);
    }

    /**
     * 8、匹配短语检索( Match Phrase Query)
     * 测试：http://localhost:8080/basicmatch/phrase?query=search engine
     */
    public Response<List<Book>> phrase(Client client,String query) {
        return basicMatchQueryService.phrase(client,query);
    }

    /**
     * 9、匹配词组前缀检索
     * 测试：http://localhost:8080/basicmatch/phraseprefix?query=search en
     */
    public Response<List<Book>> phrasePrefix(Client client,String query) {
        return basicMatchQueryService.phrasePrefix(client,query);
    }

    /**
     * 10、字符串检索（ Query String）
     * 测试：http://localhost:8080/basicmatch/querystring?query=(saerch~1 algorithm~1) AND (grant ingersoll)  OR (tom morton)
     */
    public Response<List<Book>> queryString(Client client,String query) {
        return basicMatchQueryService.queryString(client,query);
    }

    /**
     * 11、简化的字符串检索 （Simple Query String）
     * 测试：http://localhost:8080/basicmatch/simplequerystring?query=(saerch~1 algorithm~1) AND (grant ingersoll)  OR (tom morton)
     */
    public Response<List<Book>> simplequerystring(Client client,String query) {
        // 这里写死，仅为测试
        query = "(saerch~1 algorithm~1) + (grant ingersoll)  | (tom morton)";
        return basicMatchQueryService.simpleQueryString(client,query);
    }

    /**
     * 12、Term=检索（指定字段检索）
     * 测试：http://localhost:8080/basicmatch/term?query=manning
     */
    public Response<List<Book>> term(Client client,String query) {
        return basicMatchQueryService.term(client, query);
    }

    /**
     * 13、Term排序检索-（Term Query - Sorted）
     * 测试：http://localhost:8080/basicmatch/termsort?query=manning
     */
    public Response<List<Book>> termsort(Client client,String query) {
        return basicMatchQueryService.termsort(client,query);
    }

    /**
     * 14、范围检索（Range query）
     * 测试：http://localhost:8080/basicmatch/range?startDate=2015-01-01&endDate=2015-12-31
     */
    public Response<List<Book>> range(Client client,String startDate, String endDate) {
        return basicMatchQueryService.range(client,startDate, endDate);
    }

    /**
     * 15. 过滤检索
     * 测试：http://localhost:8080/basicmatch/filter?query=elasticsearch&gte=20
     */
    public Response<List<Book>> filter(Client client,String query, Integer gte, Integer lte) {
        return basicMatchQueryService.filter(client,query, gte, lte);
    }

    /**
     * 17、 Function 得分：Field值因子（ Function Score: Field Value Factor）
     * 测试：http://localhost:8080/basicmatch/fieldvaluefactor?query=search engine
     */
    public Response<List<Book>> fieldValueFactor(Client client,String query) {
        return basicMatchQueryService.fieldValueFactor(client,query);
    }

    /**
     * 18、 Function 得分：衰减函数( Function Score: Decay Functions )
     * 测试：http://localhost:8080/basicmatch/decay?query=search engines&origin=2014-06-15
     */
    public Response<List<Book>> decay(Client client,String query, String origin) {
        return basicMatchQueryService.decay(client,query, origin);
    }

    /**
     * 19、Function得分：脚本得分（ Function Score: Script Scoring ）
     * 测试：ES需要配置允许groovy脚本运行才可以
     */
    public Response<List<Book>> script(Client client,String query, String threshold) {
        return basicMatchQueryService.script(client,query, threshold);
    }



    public static void main(String args[]) throws UnknownHostException {

        Client client = ClientFactory.buildClient();

        Object object = null;

        BasicMatchQueryController controller = new BasicMatchQueryController();
        //     * 测试：http://localhost:8080/basicmatch/multimatch?query=guide

        //object = controller.multiMatch(client,"guide");
        //object = controller.multiField(client,"guide");

        MatchForm matchForm = new MatchForm("in action",10,0);
        // object = controller.match(client,matchForm);

        // object = controller.match(client,matchForm);


        //object = controller.multiFieldboost(client,"elasticsearch guide");
//

        //shouldTitles=Elasticsearch&shouldTitles=Solr&mustAuthors=clinton gormely&mustNotAuthors=radu gheorge
        List<String> list1 = new ArrayList<String>();
        list1.add("Elasticsearch");
        list1.add("Solr");
        List<String> list2 = new ArrayList<String>();
        list2.add("clinton gormely");
        List<String> list3 = new ArrayList<String>();
        list3.add("radu gheorge");
        BoolForm boolForm = new BoolForm(list1,list2,list3);
        //object = controller.bool(client,boolForm);


        //object = controller.fuzzy(client,"s");
        //object = controller.wildcard(client,"t*");

        //object = controller.phrase(client,"search engine");

        //object = controller.filter(client,"search engine",20,100);



        //object = controller.fieldValueFactor(client,"search engine");
        //object = controller.decay(client,"search engine");

        System.out.println(object.toString());
        client.close();


    }

}
