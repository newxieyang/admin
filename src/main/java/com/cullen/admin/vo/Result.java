package com.cullen.admin.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * @author cullen
 * @date 2019-07-13  00:39
 * @email newxieyang@msn.cn
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 成功标志
     */
    private boolean success;
    /**
     * 失败消息
     */
    private String message;
    /**
     * 返回代码
     */
    private Integer code = 200;
    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();
    /**
     * 结果对象
     */
    private T result;

    public Result() {
    }

    public Result(boolean success) {
        this.success = success;
    }


}
