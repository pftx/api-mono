package com.x.api.common.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.util.Date;

import org.apache.niolex.commons.util.DateTimeUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DateTimeHelperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testGetFormatterStringAbbr() throws Exception {
        Date d = new Date(123456789000L);
        DateFormat formatter = DateTimeHelper.getFormatter("HST");
        assertEquals("1973-11-29 11:33:09-10:00", formatter.format(d));
        assertEquals(d, DateTimeHelper.parseDate("1973-11-29 11:33:09-10:00", null));
    }

    @Test
    public void testGetFormatterStringLong() throws Exception {
        Date d = new Date(123456789000L);
        DateFormat formatter = DateTimeHelper.getFormatter("America/Los_Angeles");
        assertEquals("1973-11-29 13:33:09-08:00", formatter.format(d));
        assertEquals(d, DateTimeHelper.parseDate("1973-11-29 13:33:09America/Los_Angeles", null));
    }

    @Test
    public void testGetFormatterStringSt() throws Exception {
        Date d = new Date(123456789000L);
        DateFormat formatter = DateTimeHelper.getFormatter("GMT+08:00");
        assertEquals("1973-11-30 05:33:09+08:00", formatter.format(d));
        assertEquals(d, DateTimeHelper.parseDate("1973-11-30 05:33:09+08:00", null));
        assertEquals(d, DateTimeHelper.parseDate("1973-11-30 05:33:09+0800", null));
        assertEquals(d, DateTimeHelper.parseDate("1973-11-30 05:33:09+08", null));
        assertEquals(d, DateTimeHelper.parseDate("1973-11-30 05:33:09+8", null));
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
        assertEquals("2017-11-11 06:00:00+08:00", formatter.format(d));
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

    @Test
    public void testGetTimeZone() throws Exception {
        assertEquals("PST", DateTimeHelper.getTimeZone("PST").getID());
        assertEquals("CST", DateTimeHelper.getTimeZone("CST").getID());
        assertEquals("Asia/Shanghai", DateTimeHelper.getTimeZone("Asia/Shanghai").getID());
        assertEquals("Africa/Accra", DateTimeHelper.getTimeZone("Africa/Accra").getID());
        assertEquals("GMT-08:00", DateTimeHelper.getTimeZone("GMT-08:00").getID());
        assertEquals("GMT-08:00", DateTimeHelper.getTimeZone("-0800").getID());
        assertEquals("GMT-08:00", DateTimeHelper.getTimeZone("-08").getID());
        assertEquals("GMT-08:30", DateTimeHelper.getTimeZone("-08:30").getID());
    }


    @Test
    public void testGetTimeZoneErr() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid timezone: 'Pacific Standard Time'.");
        DateTimeHelper.getTimeZone("Pacific Standard Time");
    }

    @Test
    public void testIsValidTimezone() throws Exception {
        assertFalse(DateTimeHelper.isValidTimezone("Pacific Standard Time"));
        assertTrue(DateTimeHelper.isValidTimezone("PST"));
        assertTrue(DateTimeHelper.isValidTimezone("GMT"));
        assertTrue(DateTimeHelper.isValidTimezone("gmt"));
        assertTrue(DateTimeHelper.isValidTimezone("PST"));
    }

    @Test
    public void testTimezonePDT() throws Exception {
        DateFormat formatter = DateTimeHelper.getFormatter("America/Los_Angeles");
        Date d2 = DateTimeHelper.parseDate("2017-03-12 01:30:00", "America/Los_Angeles");
        assertEquals("2017-03-12 01:30:00-08:00", formatter.format(d2));
        Date d1 = DateTimeHelper.parseDate("2017-03-12 02:30:00", "America/Los_Angeles");
        assertEquals("2017-03-12 03:30:00-07:00", formatter.format(d1));
    }

    @Test
    public void testTimezonePST() throws Exception {
        DateFormat formatter = DateTimeHelper.getFormatter("America/Los_Angeles");
        Date d1 = DateTimeHelper.parseDate("2017-11-05 00:59:00", "America/Los_Angeles");
        assertEquals("2017-11-05 00:59:00-07:00", formatter.format(d1));
        Date d2 = DateTimeHelper.parseDate("2017-11-05 01:00:00", "America/Los_Angeles");
        assertEquals("2017-11-05 01:00:00-08:00", formatter.format(d2));
        Date d3 = DateTimeHelper.parseDate("2017-11-05 02:01:00", "America/Los_Angeles");
        assertEquals("2017-11-05 02:01:00-08:00", formatter.format(d3));
    }

}
