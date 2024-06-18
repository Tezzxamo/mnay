package cn.mnay.common.util.des;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * 用于第一步加解密，与前端交互的加解密
 */
public class Base64Utils {
    private static final String CHARSET = "UTF-8";

    public static String encode(String txt) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(txt.getBytes(CHARSET));
    }

    public static String decode(String txt) throws IOException {
        return new String(Base64.getDecoder().decode(txt), CHARSET);
    }
}
