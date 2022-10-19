package com.dtstack.dtcenter.common.loader.neo4j.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 按照标准格式化节点
 * @author Yi Dong
 * @create 2022-10-12-10:30
 */
public class FormatNode {

    /**
     * map 转  String  {"key":"value"}类型
     * @param params
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String mapToString(Map params) {
        if(params != null) {
            if(params.size()==0) {
                return "";
            }
        }else {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        if (!params.isEmpty()) {
            params.forEach((key, value) -> {
                sb.append(key).append(":");
                if(value instanceof Map) {
                    sb.append(mapToString((Map)value)).append(",");
                }else if(value instanceof List){
                    sb.append("[");
                    sb.append(((List) value).stream().map(i->"'"+i+"'").collect(Collectors.joining(",")));
                    sb.append("],");
                }else if (value instanceof Object[]) {
                    sb.append("[");
                    sb.append(Arrays.stream(((Object[]) value)).map(i -> "'" + i + "'").collect(Collectors.joining(",")));
                    sb.append("],");
                } else {
                    sb.append("'").append(formartString(value, key) + "").append("'").append(",");
                }
            });
        }
        sb.setLength(sb.length()-1);
        sb.append("}");
        return sb.toString();
    }

    public static String formartString(Object words, Object k) {
        String val = words + "";
        val = val.replaceAll("\\\\", "\\\\\\\\");// 替换 \

        //对于一些富文本不进行后续处理
        if (StringUtils.contains(k + "", "content")) {
            return val;
        }
        val = val.replaceAll("\r|\n", "");//去换行
        val = StringUtils.deleteWhitespace(val); //去空格

        if(val.contains("'")) {
            val = val.replaceAll("'", "’");
        }
        return val;
    }
}
