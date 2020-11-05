package com.seed.base;

import com.seed.base.utils.UserUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 18:00
 */
public class UserUtilsTest {

    @Test
    public void testUserUtils() {
        Assert.assertNotNull(UserUtils.generateToken("1111", "shouheng"));
    }

    public static void main(String...args) {
        long start = System.currentTimeMillis();
        int total = 1_000_000;
        String token;
        Set<String> set = new HashSet<>();
        for (int i=0; i<total; i++) {
            token = UserUtils.generateToken("125", "shouheng2015@gmail.com");
            set.add(token);
            if (i<10) {
                System.out.println(token);
            }
        }
        System.out.println(set.size() + "/" + total + " " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        for (int i=0; i<10; i++) {
            System.out.print(UserUtils.generateVerifyCode(4) + " ");
            System.out.println(UserUtils.generateVerifyCode(6) + " ");
        }
    }
}
