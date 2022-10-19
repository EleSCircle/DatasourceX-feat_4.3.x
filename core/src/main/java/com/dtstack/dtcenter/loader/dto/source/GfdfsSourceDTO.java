package com.dtstack.dtcenter.loader.dto.source;

import com.dtstack.dtcenter.loader.exception.DtLoaderException;
import com.dtstack.dtcenter.loader.source.DataSourceType;
import lombok.*;

import java.sql.Connection;

/**
 * @author wlq
 * @create 2022-09-22 9:33
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GfdfsSourceDTO implements ISourceDTO{
    /**
     * 文件服务器ip
     */
    private String ip;
    /**
     * 文件服务器端口号
     */
    private String port;
    /**
     * 文件服务器组名
     */
    private String group;
    /**
     * 文件服务器场景
     */
    private String scene;

    @Override
    public String getUsername() {
        throw new DtLoaderException("This method is not supported");
    }

    @Override
    public String getPassword() {
        throw new DtLoaderException("This method is not supported");
    }

    @Override
    public Integer getSourceType() {
        return DataSourceType.GFDFS.getVal();
    }

    @Override
    public Connection getConnection() {
        throw new DtLoaderException("This method is not supported");
    }

    @Override
    public void setConnection(Connection connection) {
        throw new DtLoaderException("This method is not supported");
    }
}