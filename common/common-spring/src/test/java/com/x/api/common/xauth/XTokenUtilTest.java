package com.x.api.common.xauth;

import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Lists;
import com.x.api.common.util.Constants;
import com.x.api.common.xauth.token.SecuredXToken;

public class XTokenUtilTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testDecodeToken() throws Exception {
        XTokenPrincipal p = new XTokenPrincipal();
        p.setOpAccountId(123L);
        p.setOpUserId(123456L);
        p.setUserId(1L);
        p.setUserName("root");
        p.setPermissionList(Lists.newArrayList("ADMIN", "READ"));
        SecuredXToken x = new SecuredXToken(p, "9d0500");
        String s = XTokenUtil.encodeToken(x, "Lippolis");
        System.out.println(s);
        System.out.println(XTokenUtil.plainStr(s));

        SecuredXToken dx = XTokenUtil.decodeToken(s, "Lippolis");
        Assert.assertEquals(p, dx.getPrincipal());
    }

    @Test
    public void testToStr() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Bad X-Token: ");
        XTokenPrincipal p = new XTokenPrincipal();
        p.setOpAccountId(123L);
        p.setOpUserId(123456L);
        p.setUserId(1L);
        p.setUserName("root");
        p.setPermissionList(Lists.newArrayList("ADMIN", "READ"));
        SecuredXToken x = new SecuredXToken(p, "9d0500");
        String s = XTokenUtil.encodeToken(x, "Lippolis1");
        System.out.println(s);
        System.out.println(XTokenUtil.plainStr(s));

        XTokenUtil.decodeToken(s, "Lippolis2");
    }

    @Test
    public void testCalculatePermission() throws Exception {
        XTokenPrincipal p = new XTokenPrincipal();
        p.setOpAccountId(123L);
        p.setOpUserId(123456L);
        p.setUserId(1L);
        p.setUserName("root");
        p.setPermissionList(Lists.newArrayList("super", Constants.PERM_WRITE, Constants.PERM_READ));
        List<AccountInfo> accountList = Lists.newArrayList();
        accountList.add(new AccountInfo(123L, "good", Lists.newArrayList(Constants.PERM_READ)));
        accountList.add(new AccountInfo(321L, "bad", Lists.newArrayList("other")));
        p.setAccountList(accountList);

        SecuredXToken x = new SecuredXToken(p, "9d0500");
        String s = XTokenUtil.encodeToken(x, "Lippolis1");
        SecuredXToken y = XTokenUtil.decodeToken(s, "Lippolis1");
        Assert.assertEquals(Lists.newArrayList("super", "account_read"), y.getPrincipal().getPermissionList());

        // ---
        p.setOpAccountId(321L);
        String t = XTokenUtil.encodeToken(x, "Lippolis1");
        SecuredXToken z = XTokenUtil.decodeToken(t, "Lippolis1");
        Assert.assertEquals(Lists.newArrayList("super"), z.getPrincipal().getPermissionList());
    }

}
