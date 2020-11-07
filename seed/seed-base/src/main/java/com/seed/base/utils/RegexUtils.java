package com.seed.base.utils;

import com.seed.base.constant.RegexConstants;

import java.util.regex.Pattern;

/**
 * Regex.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 20:13
 */
public final class RegexUtils {

    public static boolean isMobileSimple(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_MOBILE_SIMPLE, input);
    }

    public static boolean isMobileExact(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_MOBILE_EXACT, input);
    }

    public static boolean isEmail(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_EMAIL, input);
    }

    /**
     * Only test the format of id card.
     *
     * @param input the input
     * @return      is valid card by format
     */
    public static boolean isValidChineseIdCard(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_CHINESE_ID_CARD, input);
    }

    public static boolean isBankCard(final CharSequence input) {
        return isMatch(RegexConstants.BANK_CARD_NO, input);
    }

    public static boolean isNumber(final CharSequence input) {
        return isMatch(RegexConstants.NUMBER, input);
    }

    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    private RegexUtils() {
        throw new UnsupportedOperationException("u can't initialize me!");
    }

}
