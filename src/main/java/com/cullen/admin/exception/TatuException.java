package com.cullen.admin.exception;

import lombok.Data;

/**
 * @author cullen
 * @date 2019-07-13  11:34
 * @email newxieyang@msn.cn
 */
@Data
public class TatuException extends RuntimeException {

    private String msg;

    public TatuException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
