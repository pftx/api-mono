package com.x.api.common.validation.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.x.api.common.validation.validator.TimezoneValidator;

public class TimezoneValidatorTest {

    @Test
    public void testIsValid() throws Exception {
        TimezoneValidator v = new TimezoneValidator();
        assertFalse(v.isValid("Asia/Chengdu", null));
        assertFalse(v.isValid("Asia/Beijing", null));
        assertTrue(v.isValid("Asia/Shanghai", null));
        assertTrue(v.isValid("CST", null));
        assertTrue(v.isValid("EST", null));
        assertTrue(v.isValid("gmt", null));
        assertTrue(v.isValid("GMT", null));
        assertTrue(v.isValid("GMT+8", null));
        assertTrue(v.isValid("GMT+8:00", null));
        assertTrue(v.isValid("GMT+08:00", null));
    }

}
