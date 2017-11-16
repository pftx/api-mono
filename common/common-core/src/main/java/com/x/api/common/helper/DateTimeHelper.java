/**
 * DateTimeHelper.java
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 15, 2017
 */
public class DateTimeHelper {

    /**
     * The long data time format, including time zone info.
     */
    public static final String LONG_FORMAT = "yyyy-MM-dd HH:mm:ssz";

    /**
     * The date time format, including details to seconds.
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * The date only format.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEFAULT_TIME = "2017-11-11 00:00:00";

    public static final String DEFAULT_TIME_ZONE = "GMT+08:00";

    private static final ConcurrentHashMap<String, DateFormat> formatterMap = new ConcurrentHashMap<>();

    public static DateFormat getFormatter(String timezone) {
        return getFormatter(LONG_FORMAT, timezone);
    }

    public static DateFormat getFormatter(String format, String timezone) {
        TimeZone zone = TimeZone.getTimeZone(timezone);
        String key = format + "&" + zone.getID();
        DateFormat dateFormat = formatterMap.get(key);
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(format);
            dateFormat.setTimeZone(zone);
            formatterMap.putIfAbsent(key, dateFormat);
        }
        return dateFormat;
    }

    public static Date parseDate(String date, String timezone) {
        if (date == null) {
            return null;
        }

        DateFormat formatter = null;
        if (date.length() > DEFAULT_TIME.length()) {
            formatter = getFormatter(timezone);
        } else {
            formatter = getFormatter(DATE_TIME_FORMAT, timezone);
            if (date.length() < DEFAULT_TIME.length()) {
                date = date + DEFAULT_TIME.substring(date.length());
            }
        }

        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date: " + date + " start at " + e.getErrorOffset());
        }
    }

}
