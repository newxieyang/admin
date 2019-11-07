package com.cullen.admin.server.system.service;

import com.cullen.admin.base.BaseService;
import com.cullen.admin.server.system.entity.RoleDepartment;

import java.util.List;

/**
 * 角色部门接口
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public interface RoleDepartmentService extends BaseService<RoleDepartment> {

    /**
     * 通过roleId获取
     *
     * @param roleId
     * @return
     */
    List<RoleDepartment> findByRoleId(String roleId);

    /**
     * 通过角色id删除
     *
     * @param roleId
     */
    void deleteByRoleId(String roleId);

    /**
     * 通过角色id删除
     *
     * @param departmentId
     */
    void deleteByDepartmentId(String departmentId);
}