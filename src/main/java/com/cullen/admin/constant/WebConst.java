package com.cullen.admin.constant;

import org.springframework.stereotype.Component;

/**
 * Created by BlueT on 2017/3/3.
 */
@Component
public class WebConst {

    /**
     * aes加密加盐
     */
    public static String AES_SALT = "0123456789abcdef";

    /**
     * 当前记录起始索引
     */
    public static String PAGE_NUM = "pageNumber";

    /**
     * 每页显示记录数
     */
    public static String PAGE_SIZE = "pageSize";


}
