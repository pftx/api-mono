/**
 * ModelBuilder.java
 *
 * Copyright 2017 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.x.api.common;

import java.util.Set;

import org.apache.niolex.commons.codec.StringUtil;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableSet;

/**
 * The helper class to generate Model and Dao Java code.
 * 
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
public class ModelBuilder {

    public static final String COL_LIST =
            "user_id, username, status, permission, last_login";
    public static final String TYPE_LIST =
            "long,     String, Status, String, Date";
    public static final String CLS_NAME = "UserAccount";
    
    public static final Set<String> BASE_M = ImmutableSet.of("status", "created", "modified");

    public static void main(String[] args) {
        String[] columns = COL_LIST.split(" *, *");
        String[] types = TYPE_LIST.split(" *, *");
        final String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, CLS_NAME);
        final String varName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, CLS_NAME);
        final String keyColumn = columns[0];
        final String keyProperty = columnToCamel(keyColumn);

        System.out.println("@Data");
        System.out.println("@EqualsAndHashCode(callSuper=true)");
        System.out.println("public class " + CLS_NAME + " extends BaseModel {");
        System.out.println();

        for (int i = 0; i < columns.length; ++i) {
            String col = columns[i];
            String typ = types[i];
            if (BASE_M.contains(col)) {
                continue;
            }
            System.out.println("private " + typ + " " + columnToCamel(col) + ";");
        }
        System.out.println();
        System.out.println("}");
        System.out.println();
        System.out.println();

        System.out.println("@Mapper");
        System.out.println("public interface " + CLS_NAME + "Dao extends QueryConstants {");
        System.out.println();
        System.out.println("String SELECT_COL =\"SELECT " + COL_LIST + " FROM " + tableName + "\";");
        System.out.println();

        System.out.println("@Results(id = \"resultMap$" + CLS_NAME + "\", value = {");
        for (int i = 0; i < columns.length; ++i) {
            String col = columns[i];
            System.out.print("\t@Result(column = \"" + col + "\", property = \"" + columnToCamel(col) + "\")");
            if (i < columns.length - 1) {
                System.out.println(",");
            }
        }
        System.out.println("})");
        System.out.println("@Select(SELECT_COL + \" WHERE " + keyColumn + " = #{" + keyProperty + "} AND \" + VISIBLE)");
        System.out.println(CLS_NAME + " findById(@Param(\"" + keyProperty + "\") " + types[0] + " " + keyProperty + ");");
        System.out.println();

        String param2 = columnToCamel(columns[1]);
        System.out.println("@ResultMap(\"resultMap$" + CLS_NAME + "\")");
        System.out.println("@Select(SELECT_COL + \" WHERE " + columns[1] + " like #{" + param2 + "} AND \" + VISIBLE + PAGING)");
        System.out.println(
                "List<" + CLS_NAME + "> findByName(@Param(\"" + param2 + "\") " + types[1] + " " + param2 + ", @Param(\"offset\") int offset, @Param(\"limit\") int limit,\n" + 
                        "            @Param(\"orderBy\") String sortBy, @Param(\"orderDirection\") String sortOrder);");
        System.out.println();

        System.out.print("@Insert(\"INSERT INTO " + tableName + "(");
        for (int i = 1; i < columns.length; ++i) {
            String col = columns[i];
            if (BASE_M.contains(col)) {
                continue;
            }
            System.out.print(col + ", ");
        }
        System.out.print("status)\"\n + \"VALUES (");
        for (int i = 1; i < columns.length; ++i) {
            String col = columns[i];
            if (BASE_M.contains(col)) {
                continue;
            }
            System.out.print("#{" + columnToCamel(col) + "}, ");
        }
        System.out.println("#{status, javaType=status, jdbcType=TINYINT})\")");
        System.out.println("@Options(useGeneratedKeys = true, keyProperty = \""+keyProperty+"\", keyColumn = \""+keyColumn+"\")");
        System.out.println("public long create" + CLS_NAME + "(" + CLS_NAME + " " + varName + ");");
        System.out.println();

        System.out.println("@Update(\"UPDATE " + tableName + " SET status = 3 WHERE " + keyColumn + " = #{" + keyProperty
                + "} AND \" + NOT_DELETED)");
        System.out.println("int delete" + CLS_NAME + "(@Param(\"" + keyProperty + "\") " + types[0] + " " + keyProperty + ");");
        System.out.println();

        System.out.println("@Update(\"UPDATE " + tableName + " SET status = 0 WHERE " + keyColumn + " = #{" + keyProperty
                + "}\")");
        System.out
                .println("int deactivate" + CLS_NAME + "(@Param(\"" + keyProperty + "\") " + types[0] + " " + keyProperty + ");");
        System.out.println();
        
        System.out.println("@Update(\"UPDATE " + tableName + " SET status = 1 WHERE " + keyColumn + " = #{" + keyProperty
                + "}\")");
        System.out
        .println("int activate" + CLS_NAME + "(@Param(\"" + keyProperty + "\") " + types[0] + " " + keyProperty + ");");
        System.out.println();

        System.out.print("@Update(\"UPDATE " + tableName + " SET ");
        int real = 0;
        for (int i = 1; i < columns.length; ++i) {
            String col = columns[i];
            if (StringUtil.isIn(col, "created", "modified")) {
                continue;
            }
            if (real++ != 0) {
                System.out.print(", ");
            }
            System.out.print(col + " = #{" + columnToCamel(col) + "}");
        }
        System.out.println(" WHERE " + keyColumn + " = #{" + keyProperty
                + "} AND \" + VISIBLE)");
        System.out
                .println("int update" + CLS_NAME + "(" + CLS_NAME + " " + varName + ");");
        System.out.println();
        System.out.println("}");
    }

    public static final String columnToCamel(String col) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, col);
    }

}
