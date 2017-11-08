/**
 * AccountServer.java
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
package com.x.api.account;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.file.FileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Oct 27, 2017
 */
@EnableTransactionManagement
@EnableDiscoveryClient
@SpringBootApplication
public class AccountServer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AccountServer.class, args);
        System.out.println("AccountServer started!");
        System.out.println(
                FileUtil.getCharacterFileContentFromClassPath("/logo.txt", AccountServer.class, StringUtil.UTF_8));
    }

}
