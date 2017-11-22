/**
 * Account.java
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
package com.x.api.account.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.x.api.common.dto.Timezone;
import com.x.api.common.enums.AccountType;
import com.x.api.common.json.EnumDeserializer;
import com.x.api.common.json.MoneyMicrosDeserializer;
import com.x.api.common.json.MoneyMicrosSerializer;
import com.x.api.common.model.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Account extends BaseModel implements Timezone {

    private long accountId;
    private String name;
    private String description;
    @JsonDeserialize(using = EnumDeserializer.class)
    private AccountType type;
    @JsonDeserialize(using = MoneyMicrosDeserializer.class)
    @JsonSerialize(using = MoneyMicrosSerializer.class)
    private Long accountBalance;
    private String currencyCode;
    private String timezone;

}
