package com.cullen.admin.server.system.service;


import com.cullen.admin.vo.SearchVo;
import com.cullen.admin.base.BaseService;
import com.cullen.admin.server.system.entity.Log;

import java.util.List;

/**
 * 日志接口
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public interface LogService extends BaseService<Log> {


    /**
     * 分页搜索获取日志
     *
     * @param type
     * @param key
     * @param searchVo
     * @return
     */
    List<Log> findByCondition(Integer type, String key, SearchVo searchVo);

    /**
     * 删除所有
     */
    void deleteAll();
}
