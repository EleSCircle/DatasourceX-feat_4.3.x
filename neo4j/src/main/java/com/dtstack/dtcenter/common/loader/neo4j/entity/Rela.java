package com.dtstack.dtcenter.common.loader.neo4j.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author Yi Dong
 * @create 2022-10-10-16:05
 */
@Data
public class Rela {

    private String id;

    private String relaName;

    private String labelName;

    private String startNodeId;

    private String endNodeId;

    private Map<String, Object> propertyMap;
}
