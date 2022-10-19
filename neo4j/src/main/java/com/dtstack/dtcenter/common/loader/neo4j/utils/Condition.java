package com.dtstack.dtcenter.common.loader.neo4j.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yi Dong
 * @create 2022-10-12-15:23
 */
public class Condition {
    /** 等于 */
    public static final int OT_EQUAL = 0;

    /** 大于 */
    public static final int OT_GREATER = 1;

    /** 大于等于 */
    public static final int OT_GREATER_EQUAL = 2;

    /** 小于 */
    public static final int OT_LESS = 3;

    /** 小于等于 */
    public static final int OT_LESS_EQUAL = 4;

    /** 不等于 */
    public static final int OT_UNEQUAL = 5;

    /** 存在于集合中 */
    static final int OT_IN_TYPE = 9;
    public static final boolean OT_IN = true;

    private StringBuffer sb = new StringBuffer("");

    private ArrayList<Object> al = new ArrayList<Object>();

    private int type;

    /**
     * 将查询条件中的取值按顺序生成一个对象列表
     * @return 有序条件取值对象列表
     */
    public List<Object> getObjectList() {
        return al;
    }
}
