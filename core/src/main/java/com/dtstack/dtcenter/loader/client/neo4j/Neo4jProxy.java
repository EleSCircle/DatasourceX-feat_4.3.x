package com.dtstack.dtcenter.loader.client.neo4j;

import com.dtstack.dtcenter.loader.ClassLoaderCallBackMethod;
import com.dtstack.dtcenter.loader.client.INeo4j;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;

/**
 * @author Yi Dong
 * @create 2022-10-13-15:12
 */
public class Neo4jProxy implements INeo4j {
    private INeo4j targetClient;

    public Neo4jProxy(INeo4j targetClient) {
        this.targetClient = targetClient;
    }

    @Override
    public Boolean testCon(ISourceDTO source) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.testCon(source),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public Object executeWhatever(ISourceDTO source, String str) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.executeWhatever(source, str),
                targetClient.getClass().getClassLoader());
    }
}
