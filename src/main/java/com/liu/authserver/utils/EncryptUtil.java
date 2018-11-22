package com.liu.authserver.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
/**
 * Spring Security 3.1 中功能强大的加密工具 PasswordEncoder
 * 2017/5/12.
 */
public class EncryptUtil {
    //从配置文件中获得
    private static final String SITE_WIDE_SECRET = "my-secret-key";
    private static final PasswordEncoder encoder = new StandardPasswordEncoder(
            SITE_WIDE_SECRET);

    public static String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public static boolean match(String rawPassword, String password) {
        return encoder.matches(rawPassword, password);
    }

    public static void main(String[] args) {
        System.out.println(EncryptUtil.encrypt("每次结果都不一样伐?"));
        System.out.println(EncryptUtil.encrypt("每次结果都不一样伐?"));
        System.out.println(EncryptUtil.encrypt("每次结果都不一样伐?"));
        System.out.println(EncryptUtil.encrypt("每次结果都不一样伐?"));
        System.out.println(EncryptUtil.encrypt("每次结果都不一样伐?"));
        //但是把每次结果拿出来进行match，你会发现可以得到true。

        System.out.println(EncryptUtil.match("每次结果都不一样伐?","2aefa88cce2cb12543edd53527598cc9cac97e142755a6ac713967abf0dfe96b4706b5822b210684"));
    }
}
