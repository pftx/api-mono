package com.x.api.common.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testIsValidDate() throws Exception {
        assertTrue(DateTimeHelper.isValidDate("1960-01-01"));
        assertTrue(DateTimeHelper.isValidDate("1960-01-01 11"));
        assertTrue(DateTimeHelper.isValidDate("1960-01-01 11:12"));
        assertTrue(DateTimeHelper.isValidDate("1960-01-01 11:12:33"));
        assertTrue(DateTimeHelper.isValidDate("1960-01-01 11:12:33CST"));
        assertTrue(DateTimeHelper.isValidDate("1960-01-01 11:12:33EST"));
        assertTrue(DateTimeHelper.isValidDate("1960-01-01 11:12:33Asia/Shanghai"));
        Date d1 = DateTimeHelper.parseDate("1960-13-01", "GMT+02:00");
        assertEquals("1961-01-01 06:00:00.000", DateTimeUtil.formatDate2LongStr(d1));
        assertFalse(DateTimeHelper.isValidDate("1960-130-01"));
        assertFalse(DateTimeHelper.isValidDate("20100101"));
        assertFalse(DateTimeHelper.isValidDate("1960-01-01 11:12:33GDT"));
    }

}
