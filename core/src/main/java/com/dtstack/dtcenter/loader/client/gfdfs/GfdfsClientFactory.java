package com.dtstack.dtcenter.loader.client.gfdfs;

import com.dtstack.dtcenter.loader.ClassLoaderCallBack;
import com.dtstack.dtcenter.loader.ClassLoaderCallBackMethod;
import com.dtstack.dtcenter.loader.client.ClientFactory;
import com.dtstack.dtcenter.loader.client.IGfdfs;
import com.dtstack.dtcenter.loader.exception.DtLoaderException;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author wlq
 * @create 2022-10-17 14:07
 * go-fdfs 客户端工厂
 */
public class GfdfsClientFactory {
    public static IGfdfs createPluginClass(String pluginName) throws Exception {
        ClassLoader classLoader = ClientFactory.getClassLoader(pluginName);
        return ClassLoaderCallBackMethod.callbackAndReset((ClassLoaderCallBack<IGfdfs>) () -> {
            ServiceLoader<IGfdfs> gfdfs = ServiceLoader.load(IGfdfs.class);
            Iterator<IGfdfs> iClientIterator = gfdfs.iterator();
            if (!iClientIterator.hasNext()) {
                throw new DtLoaderException("This plugin type is not supported: " + pluginName);
            }
            IGfdfs iGfdfs = iClientIterator.next();
            return new GfdfsProxy(iGfdfs);
        }, classLoader);
    }

}
