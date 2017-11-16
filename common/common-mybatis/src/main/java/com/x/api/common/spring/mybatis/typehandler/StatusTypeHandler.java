/**
 * StatusTypeHandler.java
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
package com.x.api.common.spring.mybatis.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.x.api.common.enums.Status;
import com.x.api.common.helper.EnumHelper;
import com.x.api.common.spring.mybatis.BaseEnumTypeHandler;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
@MappedTypes({Status.class})
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.TINYINT, JdbcType.INTEGER})
public class StatusTypeHandler extends BaseEnumTypeHandler<Status> {

    @Override
    protected Status getEnum(int orinal) {
        return EnumHelper.parseFromInt(Status.class, orinal);
    }

}
