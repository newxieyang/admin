package com.cullen.admin.vo;

import lombok.Data;

/**
 * @author cullen
 * @date 2019-09-20  16:16
 * @email newxieyang@msn.cn
 *
 * <p>当前版本号 version</p>
 * <p>应用类型 applicationType</p>
 * <p>应用名称 appName</p>
 * <p>更新提示语 updateLog</p>
 * <p>应用文件下载地址或者是文件下载页面地址appUrl</p>
 * <p>强制更新 constraints</p>
 */

@Data
public class VersionVo {

    private boolean update;
    private String version;
    private String appUrl;
    private String updateLog;
    private Integer constraints;

}
