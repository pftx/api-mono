/**
 * Demo.java
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
package com.x.api.common.spring;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.x.api.common.dto.BaseDto;
import com.x.api.common.dto.annotation.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 21, 2017
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Demo extends BaseDto {

    @Id
    @Pattern(regexp = "\\d+", message = "The 'accountId' must be integer.")
    private String accountId;

    @NotNull(message = "The 'name' can not be null.")
    @Size(min = 4, max = 126, message = "The length of 'name' must in range [4, 126].")
    @Pattern(regexp = "[\\w _\\-\\.'@]{4,126}", message = "The 'name' must be only character: [a-zA-Z_0-9 _-.'@].")
    private String name;

}
