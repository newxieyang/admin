package com.cullen.admin.constant;

/**
 * @author cullen
 * @date 2019-07-13  11:34
 * @email newxieyang@msn.cn
 */
public interface SecurityConstant {

    /**
     * token分割
     */
    String TOKEN_SPLIT = "Bearer ";

    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = "tatu";

    /**
     * token参数头
     */
    String HEADER = "authorization";

    /**
     * 权限参数头
     */
    String AUTHORITIES = "authorities";

    /**
     * 用户选择JWT保存时间参数头
     */
    String SAVE_LOGIN = "saveLogin";

    /**
     * 交互token前缀key
     */
    String TOKEN_PRE = "SPIDER_TOKEN_PRE:";

    /**
     * 用户token前缀key 单点登录使用
     */
    String USER_TOKEN = "SPIDER_USER_TOKEN:";
}
