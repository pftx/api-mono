package com.x.api.common.xauth;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Lists;
import com.x.api.common.exception.AuthorizationException;
import com.x.api.common.util.Constants;
import com.x.api.common.xauth.token.SecuredXToken;

public class XAuthUtilTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SecuredXToken root;
    private SecuredXToken user;

    @Before
    public void setup() {
        XTokenPrincipal p1 = new XTokenPrincipal();
        p1.setOpAccountId(123L);
        p1.setOpUserId(123456L);
        p1.setUserId(1L);
        p1.setUserName("root");
        p1.setPermissionList(Lists.newArrayList(Constants.PERM_SUPER_LOGIN, Constants.PERM_READ));
        root = new SecuredXToken(p1, "9d0500");

        XTokenPrincipal p2 = new XTokenPrincipal();
        p2.setOpAccountId(321L);
        p2.setOpUserId(666L);
        p2.setUserId(666L);
        p2.setUserName("user");
        p2.setPermissionList(Lists.newArrayList(Constants.PERM_WRITE, Constants.PERM_READ));
        user = new SecuredXToken(p2, "9d0500");
    }

    @Test
    public void testCheckCanOperateNo1() throws Exception {
        expectedException.expect(AuthorizationException.class);
        expectedException.expectMessage("You are not authorized to operate the");
        XAuthUtil.checkCanOperate(root, 123L);
    }

    @Test
    public void testCheckCanOperateNo2() throws Exception {
        expectedException.expect(AuthorizationException.class);
        expectedException.expectMessage("You are not authorized to operate the");
        XAuthUtil.checkCanOperate(root, 111L);
    }

    @Test
    public void testCheckCanOperateYes() throws Exception {
        XAuthUtil.checkCanOperate(user, 321L);
    }

    @Test
    public void testCanOperate() throws Exception {
        assertTrue(XAuthUtil.canOperate(user, 321));
        assertFalse(XAuthUtil.canOperate(user, 123));
        assertFalse(XAuthUtil.canOperate(root, 321));
        assertFalse(XAuthUtil.canOperate(root, 123));
    }

    @Test
    public void testCheckCanRead() throws Exception {
        XAuthUtil.checkCanRead(root, 1);
    }

    @Test
    public void testCanRead() throws Exception {
        assertTrue(XAuthUtil.canRead(user, 321));
        assertFalse(XAuthUtil.canRead(user, 123));
        assertTrue(XAuthUtil.canRead(root, 321));
        assertTrue(XAuthUtil.canRead(root, 123));
    }

    @Test
    public void testCanSuper() throws Exception {
        assertTrue(XAuthUtil.canSuper(root));
        assertFalse(XAuthUtil.canSuper(user));
    }

    @Test
    public void testHasPerm() throws Exception {
        assertTrue(XAuthUtil.hasPerm(root, Constants.PERM_SUPER_LOGIN));
        assertFalse(XAuthUtil.hasPerm(user, Constants.PERM_SUPER_LOGIN));
        assertFalse(XAuthUtil.hasPerm(root, Constants.PERM_WRITE));
    }

    @Test
    public void testHasAccountPerm() throws Exception {
        assertTrue(XAuthUtil.hasAccountPerm(root, 123, Constants.PERM_SUPER_LOGIN));
        assertFalse(XAuthUtil.hasAccountPerm(root, 124, Constants.PERM_SUPER_LOGIN));
        assertFalse(XAuthUtil.hasAccountPerm(root, 123, Constants.PERM_WRITE));
        assertFalse(XAuthUtil.hasAccountPerm(user, 123, Constants.PERM_WRITE));
        assertTrue(XAuthUtil.hasAccountPerm(user, 321, Constants.PERM_WRITE));
    }

}
