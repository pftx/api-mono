/**
 * BaseEnumTypeHandler.java
 *
 * Copyright 2017 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.x.api.common.spring.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
public abstract class BaseEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    protected abstract E getEnum(int orinal);

    /**
     * This is the override of super method.
     * 
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet, java.lang.String)
     */
    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getObject(columnName) == null ? null : getEnum(rs.getInt(columnName));
    }

    /**
     * This is the override of super method.
     * 
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet, int)
     */
    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) == null ? null : getEnum(rs.getInt(columnIndex));
    }

    /**
     * This is the override of super method.
     * 
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.CallableStatement, int)
     */
    @Override
    public E getNullableResult(CallableStatement rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) == null ? null : getEnum(rs.getInt(columnIndex));
    }

    /**
     * This is the override of super method.
     * 
     * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.PreparedStatement, int,
     *      java.lang.Object, org.apache.ibatis.type.JdbcType)
     */
    @Override
    public void setNonNullParameter(PreparedStatement rs, int columnIndex, E st, JdbcType arg3)
            throws SQLException {
        rs.setInt(columnIndex, st.ordinal());
    }

}
