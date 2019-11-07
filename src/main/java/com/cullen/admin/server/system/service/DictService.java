package com.cullen.admin.server.system.service;

import com.cullen.admin.base.BaseService;
import com.cullen.admin.server.system.entity.Dict;

import java.util.List;

/**
 * 字典接口
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public interface DictService extends BaseService<Dict> {

    /**
     * 排序获取全部
     *
     * @return
     */
    List<Dict> findAllOrderBySortOrder();

    /**
     * 通过type获取
     *
     * @param type
     * @return
     */
    Dict findByType(String type);

    /**
     * 模糊搜索
     *
     * @param key
     * @return
     */
    List<Dict> findByTitleOrTypeLike(String key);
}