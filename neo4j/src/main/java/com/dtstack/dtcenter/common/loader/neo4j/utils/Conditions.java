package com.dtstack.dtcenter.common.loader.neo4j.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yi Dong
 * @create 2022-10-12-15:15
 */
public class Conditions {
    private boolean isAnd;

    private List<Object> conditionList;

    private List<Object> filterValueList = new ArrayList<Object>();

    private Conditions(boolean isAnd) {
        this.isAnd = isAnd;
        conditionList = new ArrayList<>();
    }

    /**
     * 将查询条件中的取值按顺序生成一个对象数组
     * @return 有序条件取值对象数组
     */
    public Object[] getObjectArray() {
        List<Object> list = getObjectList();
        Object[] obj = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            obj[i] = list.get(i);
        }
        return obj;
    }

    /**
     * 将查询条件中的取值按顺序生成一个对象列表
     * @return 有序条件取值对象列表
     */
    public List<Object> getObjectList() {
        ArrayList<Object> al = new ArrayList<Object>();
        for (int i = 0; i < conditionList.size(); i++) {
            Object obj = conditionList.get(i);
            if (obj instanceof Conditions) {
                Conditions conds = (Conditions) obj;
                al.addAll(conds.getObjectList());
            } else {
                Condition cond = (Condition) obj;
                al.addAll(cond.getObjectList());
            }
        }
        return al;
    }

    /**
     * 得到拼好的 SQL 语句条件部分
     * @return 拼好的 SQL 语句条件部分, 如果没有条件返回的是空字符串
     */
    public String toString() {
        String conj = isAnd ? " and " : " or ";
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < conditionList.size(); i++) {
            Object obj = conditionList.get(i);
            if (obj instanceof Conditions) {
                Conditions conds = (Conditions) obj;
                String tmp = conds.toString();
                if (!"".equals(tmp)) {
                    sb.append("(" + tmp + ")" + conj);
                }
            } else {
                Condition cond = (Condition) obj;
                sb.append(cond.toString() + conj);
            }
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - conj.length());
        } else {
            return "";
        }
    }
}
