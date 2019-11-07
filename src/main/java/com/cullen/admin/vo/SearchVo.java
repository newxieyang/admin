package com.cullen.admin.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cullen
 * @date 2019-07-13  11:34
 * @email newxieyang@msn.cn
 */
@Data
public class SearchVo implements Serializable {

    private Long startDate;

    private Long endDate;

    private String keyword;
}
