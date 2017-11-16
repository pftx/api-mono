/**
 * AccountDetails.java
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
package com.x.api.account.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.x.api.common.dto.BaseDto;
import com.x.api.common.dto.Timezone;
import com.x.api.common.enums.AccountType;
import com.x.api.common.validator.annotation.ValidEnum;
import com.x.api.common.validator.annotation.ValidTimezone;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"accountId", "name", "type", "accountBalance", "currencyCode", "timezone"})
public class AccountDto extends BaseDto implements Timezone {

    private String accountId;

    @NotNull(message = "The 'name' can not be null.")
    @Size(min = 4, max = 126, message = "The length of 'name' must in range [4, 126].")
    @Pattern(regexp = "[\\w _\\-\\.'@]{4,126}", message = "The 'name' must be only character: [a-zA-Z_0-9 _-.'@].")
    private String name;

    @Size(min = 4, max = 255, message = "The length of 'description' must in range [4, 255].")
    private String description;

    @ValidEnum(type = AccountType.class)
    private String type;

    @Pattern(regexp = "\\d+\\.?\\d*", message = "The 'accountBalance' must be decimal.")
    private String accountBalance;

    @Size(min = 3, max = 3, message = "The length of 'currencyCode' must be 3.")
    @Pattern(regexp = "[A-Z]{3}", message = "The 'currencyCode' must be all Upper Case.")
    private String currencyCode;

    @Size(min = 3, max = 32, message = "The length of 'timezone' must in range [3, 32].")
    @ValidTimezone
    private String timezone;

}
