/**
 * AuditBean.java
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
package com.x.api.gateway.bean;

import lombok.Data;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 6, 2017
 */
@Data
public class AuditBean {

    private String time;
    private String requestUrl;
    private String method;
    private String xUid;
    private String ip;
    private int status;
    private String errorMsg;
    private int duration;
    private String queryStr;
    private String xForwardedFor;
    private String remoteAddr;

}
