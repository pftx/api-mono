package com.x.api.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.x.api.common.exception.BadRequestException;

public class ExceptionUtilTest {

    private static class B extends BadRequestException {
        private static final long serialVersionUID = 1L;
    }

    @Test
    public void testFindHttpStatus() throws Exception {
        assertEquals(HttpStatus.BAD_REQUEST,
                ExceptionUtil.findHttpStatus(new B()));
    }

}
