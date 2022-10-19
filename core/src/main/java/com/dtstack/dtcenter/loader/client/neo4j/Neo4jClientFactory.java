package com.dtstack.dtcenter.loader.client.neo4j;

import com.dtstack.dtcenter.loader.ClassLoaderCallBack;
import com.dtstack.dtcenter.loader.ClassLoaderCallBackMethod;
import com.dtstack.dtcenter.loader.client.ClientFactory;
import com.dtstack.dtcenter.loader.client.INeo4j;
import com.dtstack.dtcenter.loader.exception.DtLoaderException;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * neo4j 客户端工厂
 * @author Yi Dong
 * @create 2022-10-13-15:03
 */
public class Neo4jClientFactory {
    public static INeo4j createPluginClass(String pluginName) throws Exception {
        ClassLoader classLoader = ClientFactory.getClassLoader(pluginName);
        return ClassLoaderCallBackMethod.callbackAndReset((ClassLoaderCallBack<INeo4j>) () -> {
            ServiceLoader<INeo4j> neo4js = ServiceLoader.load(INeo4j.class);
            Iterator<INeo4j> iClientIterator = neo4js.iterator();
            if (!iClientIterator.hasNext()){
                throw new DtLoaderException("This plugin type is not supported: " + pluginName);
            }
            INeo4j iNeo4j = iClientIterator.next();
            return new Neo4jProxy(iNeo4j);
        }, classLoader);
    }
}
