package com.cullen.admin.server.system.service;

import com.cullen.admin.base.BaseService;
import com.cullen.admin.server.system.entity.DictData;

import java.util.List;

/**
 * 字典数据接口
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public interface DictDataService extends BaseService<DictData> {

    /**
     * 多条件获取
     *
     * @param dictData
     * @return
     */
    List<DictData> findByCondition(DictData dictData);

    /**
     * 通过dictId获取启用字典 已排序
     *
     * @param dictId
     * @return
     */
    List<DictData> findByDictId(String dictId);

    /**
     * 通过dictId删除
     *
     * @param dictId
     */
    void deleteByDictId(String dictId);
}