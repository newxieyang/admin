package com.cullen.admin.server.system.service;

import com.cullen.admin.base.BaseService;
import com.cullen.admin.server.system.entity.QuartzJob;

import java.util.List;

/**
 * 定时任务接口
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public interface QuartzJobService extends BaseService<QuartzJob> {

    /**
     * 通过类名获取
     *
     * @param jobClassName
     * @return
     */
    List<QuartzJob> findByJobClassName(String jobClassName);


    /**
     * 多条件分页获取用户
     *
     * @return
     */
    List<QuartzJob> findByCondition();

}