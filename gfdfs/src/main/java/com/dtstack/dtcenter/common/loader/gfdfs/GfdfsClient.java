package com.dtstack.dtcenter.common.loader.gfdfs;

import com.dtstack.dtcenter.loader.client.IGfdfs;
import com.dtstack.dtcenter.loader.dto.source.GfdfsSourceDTO;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;

import java.io.File;

/**
 * @author wlq
 * @create 2022-09-26 10:39
 */
public class GfdfsClient implements IGfdfs {

    @Override
    public Boolean testCon(ISourceDTO sourceDTO) {
       GfdfsSourceDTO gfdfsSourceDTO = (GfdfsSourceDTO)sourceDTO;
       return GfdfsUtil.testConn(gfdfsSourceDTO);
    }

    @Override
    public String uploadFile(ISourceDTO sourceDTO, File file){
        GfdfsSourceDTO gfdfsSourceDTO = (GfdfsSourceDTO)sourceDTO;
        String result = GfdfsUtil.upload(gfdfsSourceDTO, file, gfdfsSourceDTO.getScene());
        return result;
    }

    @Override
    public String deleteFile(ISourceDTO sourceDTO, String path) {
        GfdfsSourceDTO gfdfsSourceDTO = (GfdfsSourceDTO)sourceDTO;
        String result = GfdfsUtil.deleteByPath(gfdfsSourceDTO, path);
        return result;
    }

    @Override
    public String deleteFileByMd5(ISourceDTO sourceDTO, String md5) {
        GfdfsSourceDTO gfdfsSourceDTO = (GfdfsSourceDTO)sourceDTO;
        String result = GfdfsUtil.deleteByMd5(gfdfsSourceDTO, md5);
        return result;
    }

    @Override
    public String getFileInfoByPath(ISourceDTO sourceDTO, String path) {
        GfdfsSourceDTO gfdfsSourceDTO = (GfdfsSourceDTO)sourceDTO;
        String result = GfdfsUtil.FileInfoByPath(gfdfsSourceDTO, path);
        return result;
    }

    @Override
    public String getFileInfoByMd5(ISourceDTO sourceDTO, String md5) {
        GfdfsSourceDTO gfdfsSourceDTO = (GfdfsSourceDTO)sourceDTO;
        String result = GfdfsUtil.FileInfoByMd5(gfdfsSourceDTO, md5);
        return result;
    }

    @Override
    public String getFileListDir(ISourceDTO sourceDTO, String dir){
        GfdfsSourceDTO gfdfsSourceDTO = (GfdfsSourceDTO)sourceDTO;
        String result = GfdfsUtil.fileListDir(gfdfsSourceDTO, dir);
        return result;
    }

    @Override
    public String getFileStat(ISourceDTO sourceDTO) {
        GfdfsSourceDTO gfdfsSourceDTO = (GfdfsSourceDTO)sourceDTO;
        String result = GfdfsUtil.fileStat(gfdfsSourceDTO);
        return result;
    }

}
