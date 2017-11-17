package com.x.api.common.util;

import static com.x.api.common.util.MiscUtil.httpHeaderEncode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MiscUtilTest {

    @Test
    public void testHttpHeaderEncode() throws Exception {
        assertNull(httpHeaderEncode(null));
        assertEquals("not yet implemented", httpHeaderEncode("not yet implemented"));
        assertEquals("not^yet^implemented", httpHeaderEncode("not\nyet\r\nimplemented"));
        String veryLong = "LWS            = [CRLF] 1*( SP | HT )TLs or separators>nclusion in a HTTP header by\n"
                + "and consisting of either *TEXT or combinatio ns double check the spec again. I'm afraid\n"
                + "1*<any CHAR except C escaping the Unicode characters via the \\uXXXX escape sequence.";

        String res = "LWS            = [CRLF] 1*( SP | HT )TLs or separators>nclusion in a HTTP header by^and cons"
                        + "isting of either *TEXT or combina...";
        assertEquals(res, httpHeaderEncode(veryLong));
    }

}
