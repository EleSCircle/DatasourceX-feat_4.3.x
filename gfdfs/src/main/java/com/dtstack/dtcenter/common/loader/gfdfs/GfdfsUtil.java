package com.dtstack.dtcenter.common.loader.gfdfs;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtstack.dtcenter.common.loader.common.utils.AddressUtil;
import com.dtstack.dtcenter.loader.dto.source.GfdfsSourceDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wlq
 * @create 2022-10-11 14:48
 */
@Slf4j
public class GfdfsUtil {
    /**
     * 上传文件服务器
     */
    private static final String FILE_UPLOAD_URL = "/upload";

    /**
     * 删除文件服务器文件
     */
    private static final String FILE_DELETE_URL = "/delete";

    /**
     * 获取文件信息
     */
    private static final String GET_FILE_INFO_URL = "/get_file_info";

    /**
     *查看文件列表的目录名    例:default
     */
    private static final String GET_LIST_DIR_URL = "/list_dir";

    /**
     * 查询文件数量
     */
    private static final String FILE_STAT_URL = "/stat";


    /** 配置管理API */
    private static final String API_RELOAD = "/reload";

    /**
     * 测试连接
     */
    public static Boolean testConn(GfdfsSourceDTO gfdfsSourceDTO){
        Integer port = Integer.valueOf(gfdfsSourceDTO.getPort());
        if (!AddressUtil.telnet(gfdfsSourceDTO.getIp(), port)) {
            return Boolean.FALSE;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("action", "get");
        String result;
        try{
            result = HttpRequest.post("http://"+gfdfsSourceDTO.getIp()+":"+gfdfsSourceDTO.getPort()+"/"+gfdfsSourceDTO.getGroup() + API_RELOAD).form(paramMap).timeout(1000).execute().body();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            try{
                result = HttpRequest.post("http://"+gfdfsSourceDTO.getIp()+":"+gfdfsSourceDTO.getPort() + API_RELOAD).form(paramMap).timeout(1000).execute().body();
            } catch (Exception eq) {
                log.error(e.getMessage(), e);
                return Boolean.FALSE; //本机未安装goFastDfs
            }
        }
        if (StringUtils.isBlank(result)) {
            return Boolean.FALSE;
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getString("status").equals("ok")) {
            System.out.println(jsonObject.toJSONString());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     *上传文件
     */
    public static String upload(GfdfsSourceDTO gfdfsSourceDTO, File file, String scene) {

        //声明参数集合
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("file", file);
        //输出
        paramMap.put("output","json");
        //场景
        paramMap.put("scene",scene);
        //上传
        String uploadUrl = "http://"+gfdfsSourceDTO.getIp()+":"+gfdfsSourceDTO.getPort()+"/"+gfdfsSourceDTO.getGroup() + FILE_UPLOAD_URL;
        String result= HttpUtil.post(uploadUrl, paramMap);
        return result;
    }
    /**
     * 删除文件
     * @param path
     */
    public static String deleteByPath(GfdfsSourceDTO gfdfsSourceDTO, String path) {
        //声明参数集合
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("path", path);
        String deleteUrl = "http://"+gfdfsSourceDTO.getIp()+":"+gfdfsSourceDTO.getPort()+"/"+gfdfsSourceDTO.getGroup() + FILE_DELETE_URL;
        //删除
        String result= HttpUtil.post(deleteUrl, paramMap);
        return result;
    }
    /**
     * 删除文件
     * @param md5
     */
    public static String deleteByMd5(GfdfsSourceDTO gfdfsSourceDTO, String md5) {
        //声明参数集合
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("md5", md5);
        String deleteUrl = "http://"+gfdfsSourceDTO.getIp()+":"+gfdfsSourceDTO.getPort()+"/"+gfdfsSourceDTO.getGroup() + FILE_DELETE_URL;
        //删除
        String result= HttpUtil.post(deleteUrl, paramMap);
        return result;
    }
    /**
     * 获取文件详细信息
     * @param path
     */
    public static String FileInfoByPath(GfdfsSourceDTO gfdfsSourceDTO, String path) {
        //声明参数集合
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("path", path);

        String fileInfoUrl = "http://"+gfdfsSourceDTO.getIp()+":"+gfdfsSourceDTO.getPort()+"/"+gfdfsSourceDTO.getGroup() + GET_FILE_INFO_URL;
        //获取文件信息
        String result= HttpUtil.post(fileInfoUrl, paramMap);
        return result;
    }
    /**
     * 获取文件详细信息
     *  @param md5
     */
    public static String FileInfoByMd5(GfdfsSourceDTO gfdfsSourceDTO, String md5) {
        //声明参数集合
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("md5", md5);

        String fileInfoUrl = "http://"+gfdfsSourceDTO.getIp()+":"+gfdfsSourceDTO.getPort()+"/"+gfdfsSourceDTO.getGroup() + GET_FILE_INFO_URL;
        //获取文件信息
        String result= HttpUtil.post(fileInfoUrl, paramMap);
        return result;
    }
    /**
     * 获取文件列表
     */
    public static String fileListDir(GfdfsSourceDTO gfdfsSourceDTO, String dir){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("dir", dir);
        String listDirUrl = "http://"+gfdfsSourceDTO.getIp()+":"+gfdfsSourceDTO.getPort()+"/"+gfdfsSourceDTO.getGroup() + GET_LIST_DIR_URL;
        String result = HttpUtil.post(listDirUrl, paramMap);
        return result;

    }
    /**
     * 文件服务器--->文件统计
     */
    public static String fileStat(GfdfsSourceDTO gfdfsSourceDTO) {
        String statUrl = "http://"+gfdfsSourceDTO.getIp()+":"+gfdfsSourceDTO.getPort()+"/"+gfdfsSourceDTO.getGroup() + FILE_STAT_URL;
        String result = HttpUtil.post(statUrl, new HashMap<>());
        return result;
    }


}
