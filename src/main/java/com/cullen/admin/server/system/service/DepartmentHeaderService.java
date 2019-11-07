package com.cullen.admin.server.system.service;

import com.cullen.admin.base.BaseService;
import com.cullen.admin.server.system.entity.DepartmentHeader;

import java.util.List;

/**
 * 部门负责人接口
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public interface DepartmentHeaderService extends BaseService<DepartmentHeader> {

    /**
     * 通过部门和负责人类型获取
     *
     * @param departmentId
     * @param type
     * @return
     */
    List<String> findHeaderByDepartmentId(String departmentId, Integer type);

    /**
     * 通过部门id删除
     *
     * @param departmentId
     */
    void deleteByDepartmentId(String departmentId);

    /**
     * 通过userId删除
     *
     * @param userId
     */
    void deleteByUserId(String userId);
}