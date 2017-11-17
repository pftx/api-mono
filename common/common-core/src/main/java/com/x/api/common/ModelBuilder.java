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

import org.apache.niolex.commons.codec.StringUtil;

import com.google.common.base.CaseFormat;

/**
 * The helper class to generate Model and Dao Java code.
 * 
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
public class ModelBuilder {

    public static final String COL_LIST =
            "account_id, name, description, status, type, account_balance, currency_code, timezone, created, modified";
    public static final String TYPE_LIST =
            "long, String,String,               int,      int,       long, String,          String,  Date, Date";
    public static final String CLS_NAME = "Account";

    public static void main(String[] args) {
        String[] columns = COL_LIST.split(" *, *");
        String[] types = TYPE_LIST.split(" *, *");
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, CLS_NAME);
        String varName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, CLS_NAME);

        System.out.println("public class " + CLS_NAME + " {");
        System.out.println();

        for (int i = 0; i < columns.length; ++i) {
            String col = columns[i];
            String typ = types[i];
            System.out.println("private " + typ + " " + columnToCamel(col) + ";");
        }
        System.out.println();
        System.out.println("}");
        System.out.println();
        System.out.println();

        System.out.println("@Mapper");
        System.out.println("public interface " + CLS_NAME + "Dao {");
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
        String param1 = columnToCamel(columns[0]);
        System.out.println("})");
        System.out.println("@Select(SELECT_COL + \" WHERE " + columns[0] + " = #{" + param1 + "} AND status < 3\")");
        System.out.println(CLS_NAME + " findById(@Param(\"" + param1 + "\") " + types[0] + " " + param1 + ");");
        System.out.println();

        String param2 = columnToCamel(columns[1]);
        System.out.println("@ResultMap(\"resultMap$" + CLS_NAME + "\")");
        System.out.println("@Select(SELECT_COL + \" WHERE " + columns[1] + " like #{" + param2 + "} AND status < 3\")");
        System.out.println(
                "List<" + CLS_NAME + "> findByName(@Param(\"" + param2 + "\") " + types[1] + " " + param2 + ");");
        System.out.println();

        System.out.print("@Insert(\"INSERT INTO " + tableName + "(\" + SELECT_COL + \") VALUES (");
        for (int i = 0; i < columns.length; ++i) {
            String col = columns[i];
            System.out.print("#{" + columnToCamel(col) + "}");
            if (i < columns.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(")\")");
        System.out.println("public void create" + CLS_NAME + "(" + CLS_NAME + " " + varName + ");");
        System.out.println();

        System.out.println("@Update(\"UPDATE " + tableName + " SET status = 3 WHERE " + columns[0] + " = #{" + param1
                + "} AND status < 3\")");
        System.out.println("int delete" + CLS_NAME + "(@Param(\"" + param1 + "\") " + types[0] + " " + param1 + ");");
        System.out.println();

        System.out.println("@Update(\"UPDATE " + tableName + " SET status = 0 WHERE " + columns[0] + " = #{" + param1
                + "} AND status = 1\")");
        System.out
                .println("int deactivate" + CLS_NAME + "(@Param(\"" + param1 + "\") " + types[0] + " " + param1 + ");");
        System.out.println();

        System.out.print("@Update(\"UPDATE " + tableName + " SET ");
        int real = 0;
        for (int i = 1; i < columns.length; ++i) {
            String col = columns[i];
            if (StringUtil.isIn(col, "status", "created", "modified")) {
                continue;
            }
            if (real++ != 0) {
                System.out.print(", ");
            }
            System.out.print(col + " = #{" + columnToCamel(col) + "}");
        }
        System.out.println(" WHERE " + columns[0] + " = #{" + param1
                + "} AND status < 3\")");
        System.out
                .println("int update" + CLS_NAME + "(" + CLS_NAME + " " + varName + ");");
        System.out.println();
        System.out.println("}");
    }

    public static final String columnToCamel(String col) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, col);
    }

}
