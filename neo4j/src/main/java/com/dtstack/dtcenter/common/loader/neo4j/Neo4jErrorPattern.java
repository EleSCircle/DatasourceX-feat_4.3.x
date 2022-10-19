package com.dtstack.dtcenter.common.loader.neo4j;

import com.dtstack.dtcenter.common.loader.common.exception.AbsErrorPattern;
import com.dtstack.dtcenter.common.loader.common.exception.ConnErrorCode;

import java.util.regex.Pattern;

/**
 * @author Yi Dong
 * @create 2022-09-21-17:48
 */
public class Neo4jErrorPattern extends AbsErrorPattern {
    private static final Pattern USERNAME_PASSWORD_ERROR = Pattern.compile("(?i)ERR\\s*invalid\\s*password");
    private static final Pattern DB_NOT_EXISTS = Pattern.compile("(?i)ERR\\s*invalid\\s*DB\\s*index");
    static {
        PATTERN_MAP.put(ConnErrorCode.USERNAME_PASSWORD_ERROR.getCode(), USERNAME_PASSWORD_ERROR);
        PATTERN_MAP.put(ConnErrorCode.DB_NOT_EXISTS.getCode(), DB_NOT_EXISTS);
    }
}
