package com.dtstack.dtcenter.common.loader.neo4j.utils;

import com.dtstack.dtcenter.common.loader.common.exception.IErrorPattern;
import com.dtstack.dtcenter.common.loader.common.service.ErrorAdapterImpl;
import com.dtstack.dtcenter.common.loader.common.service.IErrorAdapter;
import com.dtstack.dtcenter.common.loader.neo4j.Neo4jErrorPattern;
import com.dtstack.dtcenter.common.loader.neo4j.entity.Node;
import com.dtstack.dtcenter.common.loader.neo4j.entity.Rela;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import com.dtstack.dtcenter.loader.dto.source.Neo4jSourceDTO;
import com.dtstack.dtcenter.loader.exception.DtLoaderException;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Relationship;
import org.neo4j.driver.v1.util.Pair;

import java.util.*;

/**
 * @author Yi Dong
 * @create 2022-09-21-17:32
 */
public class Neo4jUtils {

    static final ThreadLocal<Session> threadSession = new ThreadLocal<>();

    static Driver driver = null;

    private static final IErrorPattern ERROR_PATTERN = new Neo4jErrorPattern();
    // 异常适配器
    private static final IErrorAdapter ERROR_ADAPTER = new ErrorAdapterImpl();

    public static boolean checkConnection(ISourceDTO iSource) {
        boolean check = false;
        try {
            Session session = session(iSource);
            if (session.isOpen())
            check = true;
            //关闭会话
            close();
        }catch (Exception e){
            throw new DtLoaderException(ERROR_ADAPTER.connAdapter(e.getMessage(), ERROR_PATTERN), e);
        }
        return check;
    }

    public static Session session(ISourceDTO iSource){
        Neo4jSourceDTO neo4jSourceDTO = (Neo4jSourceDTO) iSource;
        Session session = threadSession.get();
        if(session == null || !session.isOpen()) {
            if(driver == null) {
                driver = GraphDatabase.driver(neo4jSourceDTO.getUri(), AuthTokens.basic(neo4jSourceDTO.getUsername(), neo4jSourceDTO.getPassword()),
                        Config.build().withEncryptionLevel(Config.EncryptionLevel.REQUIRED).toConfig());
            }
            session = driver.session();
            threadSession.set(session);
        }
        return session;
    }

    public static void close(){
        Session session = threadSession.get();
        if(session != null) {
            session.close();
            threadSession.remove();
        }
    }

    public static StatementResult execute(ISourceDTO iSource, String str){
        StatementResult run = null;
        try {
            run = session(iSource).run(str);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return run;
    }

    public static StatementResult execute(ISourceDTO iSource, String str, Object... objs){
        StatementResult run = null;
        try {
            run = session(iSource).run(format(str, objs));
//            System.out.println(format(str, objs));
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return run;
    }

    public static String format(String str, Object... objs){
        for(Object obj : objs){
            if(obj == null){
                obj = "";
            }
            str = str.replaceFirst("[?]", obj.toString());
        }
        return str;
    }

    /**
     * 根据查询结果集返回节点集合
     * @param result
     * @return
     */
    public static List<Node> getNodes(StatementResult result){
        List<Node> list = new ArrayList<Node>();
        Node node;
        while(result.hasNext()){
            node = new Node();
            Record record = result.next();
            List<Pair<String, Value>> fields = record.fields();

            Pair<String, Value> pair = fields.get(0);
            org.neo4j.driver.v1.types.Node asNode = pair.value().asNode();
            Iterator<String> it = asNode.labels().iterator();
            if(it.hasNext()){
                String labelName = it.next();
                if (StringUtils.equals("exchange",labelName)){
                    if (it.hasNext()){
                        labelName = it.next();
                    }
                }
                node.setLabelName(labelName);
            }
            node.setId(asNode.id() + "");
            node.setPropertyMap(asNode.asMap());
            list.add(node);
        }
        return list;
    }

    /**
     * 返回第一个节点
     * @param result
     * @return
     */
    public static Node getNode(StatementResult result){
        List<Node> nodes = getNodes(result);
        return nodes.size() == 0 ? null : nodes.get(0);
    }

    public static String getSuffix(Map<String, Object> map, boolean flag){
        StringBuilder sb = new StringBuilder();
        String result = "";
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (StringUtils.isNotBlank(key)
                    && StringUtils.isNotBlank(map.get(key).toString())) {
                if (flag) {
                    sb.append("n." + key + "='" + map.get(key).toString() + "',");
                } else {
                    sb.append("n." + key + ",");
                }
            }
        }
        result = sb.substring(0, sb.length() - 1);
        return result;
    }


    /**
     * 根据查询结果集返回关系集合
     * @param result
     * @return
     */
    public static List<Rela> getRelas(StatementResult result){
        List<Rela> list = new ArrayList<>();
        Rela rela;
        while(result.hasNext()){
            rela = new Rela();
            Record record = result.next();
            List<Pair<String, Value>> fields = record.fields();
            Pair<String, Value> pair = fields.get(0);
            Relationship asRela = pair.value().asRelationship();
            rela.setId(asRela.id() + "");
            rela.setStartNodeId(asRela.startNodeId() + "");
            rela.setEndNodeId(asRela.endNodeId() + "");
            rela.setPropertyMap(asRela.asMap());
            rela.setLabelName(asRela.type());
            list.add(rela);
        }
        return list;
    }
}
