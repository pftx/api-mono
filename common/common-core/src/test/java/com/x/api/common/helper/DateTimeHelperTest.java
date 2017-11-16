package com.x.api.common.helper;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.util.Date;

import org.apache.niolex.commons.util.DateTimeUtil;
import org.junit.Test;

public class DateTimeHelperTest {

    @Test
    public void testGetFormatterStringAbbr() throws Exception {
        Date d = new Date(123456789000L);
        DateFormat formatter = DateTimeHelper.getFormatter("HST");
        assertEquals("1973-11-29 11:33:09HST", formatter.format(d));
    }

    @Test
    public void testGetFormatterStringLong() throws Exception {
        Date d = new Date(123456789000L);
        DateFormat formatter = DateTimeHelper.getFormatter("America/Los_Angeles");
        assertEquals("1973-11-29 13:33:09PST", formatter.format(d));
    }

    @Test
    public void testGetFormatterStringSt() throws Exception {
        Date d = new Date(123456789000L);
        DateFormat formatter = DateTimeHelper.getFormatter("GMT+08:00");
        assertEquals("1973-11-30 05:33:09GMT+08:00", formatter.format(d));
    }

    @Test
    public void testParseDateWithTZ() throws Exception {
        Date d1 = DateTimeHelper.parseDate("1973-11-30 05:33:09GMT+08:00", "GMT+02:00");
        Date d2 = new Date(123456789000L);
        assertEquals(d1, d2);
    }

    @Test
    public void testParseDateNoTZ() throws Exception {
        Date d1 = DateTimeHelper.parseDate("1973-11-30 05:33:09", "GMT+02:00");
        Date d2 = new Date(123456789000L + 6 * DateTimeUtil.HOUR);
        assertEquals(d1, d2);
    }

    @Test
    public void testParseDateThenBack() throws Exception {
        Date d = DateTimeHelper.parseDate("2017-11-11", "GMT+02:00");
        DateFormat formatter = DateTimeHelper.getFormatter("Asia/Shanghai");
        assertEquals("2017-11-11 06:00:00CST", formatter.format(d));
    }

}
