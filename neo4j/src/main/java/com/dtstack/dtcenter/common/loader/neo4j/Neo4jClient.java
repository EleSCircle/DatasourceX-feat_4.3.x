package com.dtstack.dtcenter.common.loader.neo4j;

import com.dtstack.dtcenter.common.loader.neo4j.entity.Node;
import com.dtstack.dtcenter.common.loader.neo4j.entity.Rela;
import com.dtstack.dtcenter.common.loader.neo4j.utils.Conditions;
import com.dtstack.dtcenter.common.loader.neo4j.utils.Neo4jUtils;
import com.dtstack.dtcenter.loader.client.INeo4j;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.v1.StatementResult;

import java.util.List;
import java.util.Map;

/**
 * @author Yi Dong
 * @create 2022-09-21-15:30
 * @Description：Neo4j 客户端
 */
public class Neo4jClient implements INeo4j {

    /**
     * 测试连通
     * @param source
     * @return
     */
    @Override
    public Boolean testCon(ISourceDTO source) {
        return Neo4jUtils.checkConnection(source);
    }

    /**
     * 直接写语句执行命令
     * @param source
     * @param str
     */
    @Override
    public StatementResult executeWhatever(ISourceDTO source, String str){
        if (StringUtils.isNotEmpty(str)){
            StatementResult result = Neo4jUtils.execute(source,str);
            return result;
        }
        return null;
    }

    /**
     * 新增一个节点
     * @param source
     * @param nodeName
     * @param labelName
     * @param jsonStr
     * @return
     */
    public Node add(ISourceDTO source, String nodeName, String labelName, String jsonStr){
        if(StringUtils.isEmpty(nodeName)){
            nodeName = "n";
        }
        String cql = "CREATE (?:? ?) return ?";
        StatementResult result = Neo4jUtils.execute(source, cql, new Object[]{nodeName, labelName, jsonStr, nodeName});
        return Neo4jUtils.getNode(result);
    }

    /**
     * 删除节点
     * @param source
     * @param nodeName
     * @param labelName
     * @param jsonStr
     */
    public void delete(ISourceDTO source, String nodeName, String labelName, String jsonStr){
        if(StringUtils.isEmpty(nodeName)){
            nodeName = "n";
        }
        String cql = "Match (?:? ?) delete ?";
        Neo4jUtils.execute(source, cql, new Object[]{nodeName, labelName, jsonStr, nodeName});
    }

    /**
     * 根据节点id删除节点
     * @param source
     * @param nodeId
     */
    public void deleteByID(ISourceDTO source, String nodeId){
        String cql = "start n=node(?) delete n";
        Neo4jUtils.execute(source, cql, new Object[]{nodeId});
    }

    /**
     * 节点增加属性
     * @param source
     * @param id
     * @param map
     */
    public void update(ISourceDTO source, String id, Map<String, Object> map){
        String suffix = "";
        String cql = "";
        if (map != null && !map.isEmpty()) {
            suffix += " set " + Neo4jUtils.getSuffix(map, true);
            cql = "start n = node(?) " + suffix + " return n";
            Neo4jUtils.execute(source, cql, new Object[] { id });
        }
    }

    /**
     * 修改节点属性(先删后增)
     * @param source
     * @param id
     * @param map
     * @return
     */
    public Node updateProp(ISourceDTO source, String id, Map<String, Object> map){
        String suffix = "";
        String cql = "";
        Node node = getNodeById(source, id);
        if (map != null) {
            suffix = "remove " + Neo4jUtils.getSuffix(node.getPropertyMap(), false);
            suffix += " set " + Neo4jUtils.getSuffix(map, true);
            cql = "start n = node(?) " + suffix + " return n";
            StatementResult result = Neo4jUtils
                    .execute(source,cql, new Object[] { id });
            return Neo4jUtils.getNode(result);
        }
        return node;
    }

    /**
     * 根据id查询节点
     * @param source
     * @param id
     * @return
     */
    public Node getNodeById(ISourceDTO source, String id){
        String	cql = "start n = node(?) return n";
        StatementResult result = Neo4jUtils.execute(source, cql, new Object[]{id});
        return Neo4jUtils.getNode(result);
    }

    /**
     * 根据属性查询节点
     * @param source
     * @param params
     * @return
     */
    public List<Node> findByParams(ISourceDTO source, Map<String,Object> params){
        StringBuffer sb = new StringBuffer("match(n) where 1=1 ");
        params.entrySet().forEach(it->{
            sb.append(" and n.").append(it.getKey()).append(" = '").append(it.getValue()).append("' ");
        });
        sb.append("  return n ");
        StatementResult execute = Neo4jUtils.execute(source, sb.toString());
        return Neo4jUtils.getNodes(execute);
    }

    /**
     * 查询边的开始节点
     * @param source
     * @param id
     * @return
     */
    public Node findStartNode(ISourceDTO source,String id){
        String cql = "start r=relationship(?) return startnode(r)";
        StatementResult result = Neo4jUtils.execute(source, cql, new Object[]{id});
        return Neo4jUtils.getNode(result);
    }

    /**
     * 查询边的结束节点
     * @param source
     * @param id
     * @return
     */
    public Node findEndNode(ISourceDTO source, String id){
        String cql = "start r=relationship(?) return endnode(r)";
        StatementResult result = Neo4jUtils.execute(source, cql, new Object[]{id});
        return Neo4jUtils.getNode(result);
    }

    public List<Node> query(ISourceDTO source, Conditions conds, String where, String orderBy){
        if(where == null)
            where = "";

        String cql = "match (n) where ";
        if(conds == null || StringUtils.isEmpty(conds.toString())) {
            cql += " 1=1 ";
        }
        cql += conds+" "+where+" return n";
        if(StringUtils.isNotEmpty(orderBy)) {
            cql += " order by " + orderBy;
        }
        StatementResult result = Neo4jUtils.execute(source, cql, conds.getObjectArray());

        return Neo4jUtils.getNodes(result);
    }


    /**
     * 新增关系
     * @param source
     * @param startNodeId
     * @param endNodeId
     * @param relaName
     * @param labelName
     * @param jsonStr
     */
    public void addRela(ISourceDTO source, String startNodeId, String endNodeId, String relaName, String labelName, String jsonStr){
        String cql = "start n=node(?), m=node(?) create (n) - [?:? ?] -> (m)";
        Neo4jUtils.execute(source, cql, new Object[]{
                startNodeId,
                endNodeId,
                relaName,
                labelName,
                jsonStr});
    }

    /**
     * 删除关系
     * @param source
     * @param startNodeName
     * @param startLabelName
     * @param startJsonStr
     * @param endNodeName
     * @param endLabelName
     * @param endJsonStr
     */
    public void deleteRela(ISourceDTO source, String startNodeName, String startLabelName, String startJsonStr,
                           String endNodeName, String endLabelName, String endJsonStr){
        String cql = "Match (?:? ?)-[r]->(?:? ?) delete r";
        Neo4jUtils.execute(source, cql, new Object[]{
                startNodeName,
                startLabelName,
                startJsonStr,
                endNodeName,
                endLabelName,
                endJsonStr});
    }

    public void updateRelaById(ISourceDTO source, String relaId, Map<String, Object> map){
        String suffix = "";
        String cql = "";
        Rela rela = findRelaById(source, relaId);
        if (map != null) {
            suffix = "remove "
                    + Neo4jUtils.getSuffix(rela.getPropertyMap(), false);
            suffix += " set " + Neo4jUtils.getSuffix(map, true);
            cql = "start n=relationship(?) " + suffix + " return n";
            Neo4jUtils.execute(source, cql, new Object[] { relaId });
        }
    }

    public Rela findRelaById(ISourceDTO source, String relaId){
        String cql = "start r=relationship(?) return r";
        StatementResult result = Neo4jUtils
                .execute(source, cql, new Object[] { relaId });
        List<Rela> relas = Neo4jUtils.getRelas(result);
        return relas.size() == 0 ? null : relas.get(0);
    }

    public List<Rela> findRelasByGroup(ISourceDTO source, String stNodeId, String groupName,
                                       String groupValue, String orderBy) {
        String suffix = StringUtils.isBlank(orderBy) ? "" : "order by r."
                + orderBy + " desc";
        String cql = "start n=node(?) match (n) - [r] -> (m) where r."
                + groupName + "='" + groupValue + "'" + " return r " + suffix;
        StatementResult result = Neo4jUtils.execute(source, cql,
                new Object[] { stNodeId });
        return Neo4jUtils.getRelas(result);
    }

    public List<Rela> findRelas(ISourceDTO source, String stNodeId, String stLabelName, Conditions conds, String orderBy){
        if(StringUtils.isNotEmpty(stLabelName)) {
            stLabelName = ":" + stLabelName;
        }else {
            stLabelName = "";
        }
        String cql = "start n=node("+stNodeId+") match (n) - [r "+stLabelName+"] -> (m) where 1 = 1 ";
        if(conds != null && StringUtils.isNotEmpty(conds.toString())) {
            cql += conds.toString();
        }
        cql += " return r ";
        if(StringUtils.isNotEmpty(orderBy)) {
            cql += " order by " + orderBy;
        }
        StatementResult result;
        if(conds != null && StringUtils.isNotEmpty(conds.toString())) {
            result = Neo4jUtils.execute(source, cql, conds.getObjectArray());
        }else {
            result = Neo4jUtils.execute(source, cql);
        }

        return Neo4jUtils.getRelas(result);
    }

    public List<Rela> findRelasByEndNodeId(ISourceDTO source, String nodeId){
        String cql = "start m=node(?) match (n) - [r] -> (m) return r";
        StatementResult result = Neo4jUtils.execute(source, cql, new Object[]{nodeId});
        return Neo4jUtils.getRelas(result);
    }
}
