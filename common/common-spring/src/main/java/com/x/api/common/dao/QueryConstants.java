/**
 * QueryConstants.java
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
package com.x.api.common.dao;

/**
 * Status:  Inactive, Active, Archive, Deleted;
 * Ordinal: 0         1       2        3
 * 
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 19, 2017
 */
public interface QueryConstants {

    // Inactive & Active
    String VISIBLE = "status < 2";

    String ACTIVE = "status = 1";

    String NOT_DELETED = "status != 3";

    String PAGING = " ORDER BY ${orderBy} ${orderDirection} LIMIT ${offset}, ${limit}";

}
