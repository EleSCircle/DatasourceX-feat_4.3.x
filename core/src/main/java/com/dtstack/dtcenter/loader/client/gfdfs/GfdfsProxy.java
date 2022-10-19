package com.dtstack.dtcenter.loader.client.gfdfs;

import com.dtstack.dtcenter.loader.ClassLoaderCallBackMethod;
import com.dtstack.dtcenter.loader.client.IGfdfs;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;

import java.io.File;

/**
 * @author wlq
 * @create 2022-10-17 14:27
 */
public class GfdfsProxy implements IGfdfs {

    IGfdfs targetClient = null;

    public GfdfsProxy(IGfdfs iGfdfs) {
        this.targetClient = iGfdfs;
    }

    @Override
    public Boolean testCon(ISourceDTO sourceDTO) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.testCon(sourceDTO),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public String uploadFile(ISourceDTO sourceDTO, File file) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.uploadFile(sourceDTO, file),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public String deleteFile(ISourceDTO sourceDTO, String path) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.deleteFile(sourceDTO, path),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public String deleteFileByMd5(ISourceDTO sourceDTO, String md5) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.deleteFileByMd5(sourceDTO, md5),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public String getFileInfoByPath(ISourceDTO sourceDTO, String path) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.getFileInfoByPath(sourceDTO, path),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public String getFileInfoByMd5(ISourceDTO sourceDTO, String md5) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.getFileInfoByMd5(sourceDTO, md5),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public String getFileListDir(ISourceDTO sourceDTO, String dir) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.getFileListDir(sourceDTO, dir),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public String getFileStat(ISourceDTO sourceDTO) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.getFileStat(sourceDTO),
                targetClient.getClass().getClassLoader());
    }
}
