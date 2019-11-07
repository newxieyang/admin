package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.RolePermission;
import com.cullen.admin.server.system.mapper.RolePermissionMapper;
import com.cullen.admin.server.system.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 角色权限接口实现
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionMapper mapper;

    @Override
    public BaseMapper<RolePermission> getMapper() {
        return mapper;
    }

    @Override
    public List<RolePermission> findByPermissionId(String permissionId) {

        Example example = new Example(RolePermission.class);
        example.createCriteria().andEqualTo("permissionId", permissionId);
        return mapper.selectByExample(example);

    }

    @Override
    public List<RolePermission> findByRoleId(String roleId) {

        Example example = new Example(RolePermission.class);
        example.createCriteria().andEqualTo("roleId", roleId);
        return mapper.selectByExample(example);
    }

    @Override
    public void deleteByRoleId(String roleId) {
        Example example = new Example(RolePermission.class);
        example.createCriteria().andEqualTo("roleId", roleId);
        mapper.deleteByExample(example);
    }
}