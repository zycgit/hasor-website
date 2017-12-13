/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.website.datadao.convert;
import net.hasor.website.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @version : 2016年08月11日
 * @author 赵永春 (zyc@hasor.net)
 */
public abstract class AbstractJsonConvert<T> extends BaseTypeHandler<T> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    protected abstract Class<T> getConvertType();
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        String jsonData = "";
        if (parameter != null) {
            jsonData = JsonUtils.toJsonStringSingleLine(parameter);
        }
        ps.setString(i, jsonData);
    }
    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonData = rs.getString(columnName);
        if (StringUtils.isBlank(jsonData)) {
            return null;
        }
        return JsonUtils.toObject(jsonData, getConvertType());
    }
    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        throw new SQLException("not support");
    }
    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        throw new SQLException("not support");
    }
}