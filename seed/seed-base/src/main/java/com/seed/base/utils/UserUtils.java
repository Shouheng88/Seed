package com.seed.base.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.lang.NonNull;

import static java.lang.Math.*;

/**
 * User utils.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 17:54
 */
public final class UserUtils {

    /**
     * Generate random token for given account
     *
     * @param uid     the uid
     * @param account the account
     * @return        the token generated
     */
    public static String generateToken(String uid, String account) {
        long salt0 = System.currentTimeMillis();
        long salt1 = RandomUtil.randomInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        long salt2 = RandomUtil.randomInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        String tokenStr = salt0 + ";" + uid + ';' + salt1 + account + ';' + salt2;
        return DigestUtil.sha256Hex(tokenStr);
    }

    /**
     * Generate a verify code of given length.
     *
     * @return the verify code
     */
    public static String generateVerifyCode(int length) {
        return String.valueOf(RandomUtil.randomInt((int) pow(10, length-1), (int) pow(10, length)));
    }

    /**
     * Hide user account information.
     *
     * @param phoneOrEmail phone number or email
     * @return             the account
     */
    public static String hideAccount(@NonNull String phoneOrEmail) {
        if (StrUtil.isEmpty(phoneOrEmail)) return phoneOrEmail;
        if (phoneOrEmail.contains("@")) {
            // email: w_****ng@163.com
            String[] split = phoneOrEmail.split("@");
            int len = split[0].length();
            int showCount = len >= 6 ? 2 : 1;
            return split[0].substring(0, showCount) + "****" + split[0].substring(len-showCount, len) + "@" + split[1];
        } else  {
            // phone number: 157****1814
            int len = phoneOrEmail.length();
            return phoneOrEmail.substring(0, 3) + "****" + phoneOrEmail.substring(len-4, len);
        }
    }

    private UserUtils() {
        throw new UnsupportedOperationException("u can't initialize me!");
    }
}
