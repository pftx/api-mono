package com.x.api.common.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.x.api.common.dto.annotation.Id;
import com.x.api.common.exception.ValidationException;
import com.x.api.common.spring.Demo;

public class ValidationUtilTest {

    @Id
    public int getNiceName() {
        return 3322;
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testValidateId() throws Exception {
        Demo d = new Demo();
        ValidationUtil.validateId(d, 1234);
        d.setAccountId("1234");
        ValidationUtil.validateId(d, 1234);
    }

    @Test
    public void testValidateIdErr() throws Exception {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("The value of accountId from request body and path must match.");
        Demo d = new Demo();
        d.setAccountId("1234");
        ValidationUtil.validateId(d, 1233);
    }

    @Test
    public void testValidateMethod() throws Exception {
        ValidationUtil.validateId(this, 3322);
    }

    @Test
    public void testValidateMethodErr() throws Exception {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("The value of niceName from request body and path must match.");
        ValidationUtil.validateId(this, 3344);
    }

}
