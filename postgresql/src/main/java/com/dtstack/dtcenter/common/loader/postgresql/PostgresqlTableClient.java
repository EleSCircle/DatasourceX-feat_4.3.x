/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.dtcenter.common.loader.postgresql;

import com.dtstack.dtcenter.common.loader.rdbms.AbsTableClient;
import com.dtstack.dtcenter.common.loader.rdbms.ConnFactory;
import com.dtstack.dtcenter.loader.dto.UpsertColumnMetaDTO;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;
import com.dtstack.dtcenter.loader.dto.source.PostgresqlSourceDTO;
import com.dtstack.dtcenter.loader.dto.source.RdbmsSourceDTO;
import com.dtstack.dtcenter.loader.exception.DtLoaderException;
import com.dtstack.dtcenter.loader.source.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * postgresql表操作相关接口
 *
 * @author ：wangchuan
 * date：Created in 10:57 上午 2020/12/3
 * company: www.dtstack.com
 */
@Slf4j
public class PostgresqlTableClient extends AbsTableClient {

    // 获取表占用存储sql
    private static final String TABLE_SIZE_SQL = "SELECT pg_total_relation_size('\"' || table_schema || '\".\"' || table_name || '\"') AS table_size " +
            "FROM information_schema.tables where table_schema = '%s' and table_name = '%s'";

    // 判断表是不是视图表sql
    private static final String TABLE_IS_VIEW_SQL = "select viewname from pg_views where (schemaname ='public' or schemaname = '%s') and viewname = '%s'";

    private static final String ADD_COLUMN_SQL = "ALTER TABLE %s ADD COLUMN %s %s";

    private static final String ADD_COLUMN_COMMENT_SQL = "COMMENT ON COLUMN %s IS '%s'";

    @Override
    protected ConnFactory getConnFactory() {
        return new PostgresqlConnFactory();
    }

    @Override
    protected DataSourceType getSourceType() {
        return DataSourceType.PostgreSQL;
    }

    @Override
    public List<String> showPartitions(ISourceDTO source, String tableName) {
        throw new DtLoaderException("postgresql not supported fetch partition operation");
    }

    @Override
    protected String getDropTableSql(String tableName) {
        return String.format("drop table if exists %s", tableName);
    }

    @Override
    public Boolean alterTableParams(ISourceDTO source, String tableName, Map<String, String> params) {
        PostgresqlSourceDTO postgresqlSourceDTO = (PostgresqlSourceDTO) source;
        String comment = params.get("comment");
        log.info("update table comment，comment：{}！", comment);
        if (StringUtils.isEmpty(comment)) {
            return true;
        }
        String tbName = transferSchemaAndTableName(postgresqlSourceDTO.getSchema(), tableName);
        String alterTableParamsSql = String.format("comment on table %s is '%s'", tbName, comment);
        return executeSqlWithoutResultSet(source, alterTableParamsSql);
    }

    @Override
    protected String getTableSizeSql(String schema, String tableName) {
        if (StringUtils.isBlank(schema)) {
            throw new DtLoaderException("schema is not empty");
        }
        return String.format(TABLE_SIZE_SQL, schema, tableName);
    }

    @Override
    public Boolean isView(ISourceDTO source, String schema, String tableName) {
        checkParamAndSetSchema(source, schema, tableName);
        schema = StringUtils.isNotBlank(schema) ? schema : "public";
        String sql = String.format(TABLE_IS_VIEW_SQL, schema, tableName);
        return CollectionUtils.isNotEmpty(executeQuery(source, sql));
    }



    /**
     * 添加表列名
     *
     * @param source
     * @param columnMetaDTO
     * @return
     */
    protected Boolean addTableColumn(ISourceDTO source, UpsertColumnMetaDTO columnMetaDTO) {
        RdbmsSourceDTO rdbmsSourceDTO = (RdbmsSourceDTO) source;
        String schema = StringUtils.isNotBlank(columnMetaDTO.getSchema()) ? columnMetaDTO.getSchema() : rdbmsSourceDTO.getSchema();
        String tableName = transferSchemaAndTableName(schema, columnMetaDTO.getTableName());
        String sql = String.format(ADD_COLUMN_SQL, tableName, columnMetaDTO.getColumnName(), columnMetaDTO.getColumnType());
        executeSqlWithoutResultSet(source, sql);
        if (StringUtils.isNotEmpty(columnMetaDTO.getColumnComment())) {
            String commentSql = String.format(ADD_COLUMN_COMMENT_SQL, transformTableColumn(tableName, columnMetaDTO.getColumnName()), columnMetaDTO.getColumnComment());
            executeSqlWithoutResultSet(source, commentSql);
        }
        return true;
    }
}
