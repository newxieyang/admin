package com.cullen.admin.server.system.service;

import com.cullen.admin.base.BaseService;
import com.cullen.admin.server.system.entity.RolePermission;

import java.util.List;

/**
 * 角色权限接口
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public interface RolePermissionService extends BaseService<RolePermission> {

    /**
     * 通过permissionId获取
     *
     * @param permissionId
     * @return
     */
    List<RolePermission> findByPermissionId(String permissionId);

    /**
     * 通过roleId获取
     *
     * @param roleId
     */
    List<RolePermission> findByRoleId(String roleId);

    /**
     * 通过roleId删除
     *
     * @param roleId
     */
    void deleteByRoleId(String roleId);
}