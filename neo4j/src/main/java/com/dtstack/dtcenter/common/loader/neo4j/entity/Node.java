package com.dtstack.dtcenter.common.loader.neo4j.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author Yi Dong
 * @create 2022-10-10-16:03
 */
@Data
public class Node {

    private String id;

    private String nodeName;

    private String labelName;

    private Map<String, Object> propertyMap;
}
