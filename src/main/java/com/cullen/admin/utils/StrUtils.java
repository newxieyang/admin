package com.cullen.admin.utils;


/**
 * @author cullen
 * @date 2019-07-23  16:36
 * @email newxieyang@msn.cn
 */
public class StrUtils {


    /***
     * 是否为空
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    /***
     * 是否 不为空
     * @param cs
     * @return
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }


    /**
     * 是否为空 不判断空字符
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {

        return str == null || str.length() == 0;

    }


    /***
     * 拼接 like 字符串
     * @param key
     * @return %key%
     */
    public static String jointLike(String key) {
        return "%" + key + "%";
    }

}
