package com.dtstack.dtcenter.loader.client;

import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;

import java.io.File;

/**
 * GoFastDfs客户端接口
 * @author wlq
 * @create 2022-10-14 16:50
 */
public interface IGfdfs {
    /**
     * 测试 连接
     *
     * @param sourceDTO 数据源信息
     * @return
     * @throws Exception
     */
    Boolean testCon(ISourceDTO sourceDTO);
    /**
     * 上传文件
     * @Param sourceDTO
     * @Param file 文件地址
     * @return
     * @throws Exception
     */
    String uploadFile(ISourceDTO sourceDTO, File file);
    /**
     *删除文件
     * @param sourceDTO
     * @param path
     * @return
     * @throws Exception
     */
    String deleteFile(ISourceDTO sourceDTO, String path);
    /**
     *删除文件
     * @param sourceDTO
     * @param md5
     * @return
     * @throws Exception
     */
    String deleteFileByMd5(ISourceDTO sourceDTO, String md5);
    /**
     * 获取文件的详细信息
     * @param sourceDTO
     * @param path 文件存储路径
     * @return
     * @throws Exception
     */
    String getFileInfoByPath(ISourceDTO sourceDTO, String path);
    /**
     * 获取文件的详细信息
     * @param sourceDTO
     * @param md5
     * @return
     * @throws Exception
     */
    String getFileInfoByMd5(ISourceDTO sourceDTO, String md5);
    /**
     * 获取文件列表
     * @param sourceDTO
     * @Param dir 要获取的目录名称
     * @return
     * @throws Exception
     */
    String getFileListDir(ISourceDTO sourceDTO, String dir);
    /**
     * 文件服务器--->文件统计
     * @Param sourceDTO
     * @return
     * @throws Exception
     */
    String getFileStat(ISourceDTO sourceDTO);
}
