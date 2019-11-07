package com.cullen.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Data
@AllArgsConstructor
public class RedisVo {

    private String key;

    private String value;
}
