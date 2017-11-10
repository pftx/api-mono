/**
 * UniqueIdGenerator.java
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
package com.x.api.common.helper;

import java.security.SecureRandom;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 10, 2017
 */
public class UniqueIdGenerator {

    private static final long BASE = 1510298810994L;
    private static final SecureRandom sRand = new SecureRandom();

    public static long uniqueId() {
        long basePart = System.currentTimeMillis() - BASE;
        long minerPart = (sRand.nextLong() ^ (basePart % 1000)) % 10000;
        return basePart * 100 + minerPart;
    }

}
