package com.dtstack.dtcenter.loader.client.sql;

import com.dtstack.dtcenter.common.loader.neo4j.Neo4jClient;
import com.dtstack.dtcenter.common.loader.neo4j.entity.Node;
import com.dtstack.dtcenter.common.loader.neo4j.entity.Rela;
import com.dtstack.dtcenter.common.loader.neo4j.utils.FormatNode;
import com.dtstack.dtcenter.loader.client.BaseTest;
import com.dtstack.dtcenter.loader.client.ClientCache;
import com.dtstack.dtcenter.loader.client.INeo4j;
import com.dtstack.dtcenter.loader.dto.source.Neo4jSourceDTO;
import com.dtstack.dtcenter.loader.exception.DtLoaderException;
import com.dtstack.dtcenter.loader.source.DataSourceType;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

/**
 * @author Yi Dong
 * @create 2022-09-22-16:18
 */
public class Neo4jTest extends BaseTest {

    // 构建client
    private static final INeo4j client = ClientCache.getNeo4j(DataSourceType.NEO4J.getVal());

    // 构建Neo4j数据源信息
    private static final Neo4jSourceDTO source = Neo4jSourceDTO.builder()
            .uri("bolt://10.2.18.17:7687")
            .username("neo4j")
            .password("wonders")
            .build();

    /**
     * 连通性测试
     */
    @Test
    public void testConNeo4j() {
        Boolean isConnected = client.testCon(source);
        if (Boolean.FALSE.equals(isConnected)) {
            throw new DtLoaderException("connection exception");
        }else {
            System.out.println("connection ok");
        }
    }

    /**
     * 测试直接输入语句执行命令
     */
    @Test
    public void testAllOrder(){
        String str= "create (n:person{name:'cpo'}) return n";
        client.executeWhatever(source,str);
        System.out.println("execute ok");
    }

    /**
     * 测试新增节点
     */
    @Test
    public void testAdd(){
        Neo4jClient client = new Neo4jClient();
        HashMap<String,Object> test = new HashMap<>();
        test.put("tt","7");
        client.add(source,"t","test", FormatNode.mapToString(test));
        System.out.println("add ok");
    }

    /**
     * 测试删除查询到的节点
     */
    @Test
    public void testDelete(){
        Neo4jClient client = new Neo4jClient();
        HashMap<String,Object> test = new HashMap<>();
        test.put("tt","1");
        client.delete(source,"t","test", FormatNode.mapToString(test));
        System.out.println("delete ok");
    }


    /**
     * 测试删除节点
     */
    @Test
    public void testDeleteByID(){
        Neo4jClient client = new Neo4jClient();
        client.deleteByID(source,"4432");
        System.out.println("delete ok");
    }

    /**
     * 测试更新节点属性
     */
    @Test
    public void testUpdate(){
        Neo4jClient client = new Neo4jClient();
        HashMap<String,Object> test = new HashMap<>();
        test.put("tt","2");
        client.updateProp(source, "4434", test);
        System.out.println("update ok");
    }

    /**
     * 测试根据属性查询节点
     */
    @Test
    public void testFindNodeByParams(){
        Neo4jClient client = new Neo4jClient();
        HashMap<String,Object> test = new HashMap<>();
        test.put("tt","2");
        List<Node> result = client.findByParams(source, test);
        System.out.println(result);
        System.out.println("query ok");
    }


    /**
     * 测试 新增关系
     */
    @Test
    public void testAddRela(){
        Neo4jClient client = new Neo4jClient();
        HashMap<String,Object> test = new HashMap<>();
        test.put("source", 1);
        client.addRela(source,"4437","4434","R","add",FormatNode.mapToString(test));
        System.out.println("add ok");
    }

    /**
     * 测试 删除关系
     */
    @Test
    public void testDeleteRela(){
        Neo4jClient client = new Neo4jClient();
        HashMap<String,Object> start = new HashMap<>();
        start.put("tt", "3");
        HashMap<String,Object> end = new HashMap<>();
        end.put("tt", "2");
        client.deleteRela(source,"n","test",FormatNode.mapToString(start),"m","test",FormatNode.mapToString(end));
        System.out.println("delete ok");
    }

    /**
     * 测试 更新关系的属性
     */
    @Test
    public void testUpdateRelaById(){
        Neo4jClient client = new Neo4jClient();
        HashMap<String,Object> test = new HashMap<>();
        test.put("source", 2);
        client.updateRelaById(source,"12856",test);
        System.out.println("update ok");
    }

    /**
     * 测试 根据结束节点id查询其对应的关系
     */
    @Test
    public void testFindRelasByEndNodeId(){
        Neo4jClient client = new Neo4jClient();
        List<Rela> result = client.findRelasByEndNodeId(source, "4434");
        System.out.println(result);
        System.out.println("query ok");
    }
}
