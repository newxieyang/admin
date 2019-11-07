package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.Role;
import com.cullen.admin.server.system.mapper.RoleMapper;
import com.cullen.admin.server.system.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 角色接口实现
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public BaseMapper<Role> getMapper() {
        return roleMapper;
    }

    @Override
    public List<Role> findByDefaultRole(Boolean defaultRole) {
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("defaultRole", defaultRole);
        return roleMapper.selectByExample(example);
    }


    @Override
    public List<Role> findByCondition() {
        return roleMapper.selectAll();
    }
}
