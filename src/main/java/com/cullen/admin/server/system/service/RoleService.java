package com.cullen.admin.server.system.service;


import com.cullen.admin.base.BaseService;
import com.cullen.admin.server.system.entity.Role;

import java.util.List;

/**
 * 角色接口
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 获取默认角色
     *
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);


    /**
     * 分页搜索获取角色
     *
     * @return
     */
    List<Role> findByCondition();


}
