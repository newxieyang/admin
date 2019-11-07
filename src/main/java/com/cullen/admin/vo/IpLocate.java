package com.cullen.admin.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cullen
 * @date 2019-07-13  11:34
 * @email newxieyang@msn.cn
 */
@Data
public class IpLocate implements Serializable {

    private String retCode;

    private City result;
}

