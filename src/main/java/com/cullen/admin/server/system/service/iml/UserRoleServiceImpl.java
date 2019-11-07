package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.Role;
import com.cullen.admin.server.system.entity.UserRole;
import com.cullen.admin.server.system.mapper.UserRoleMapper;
import com.cullen.admin.server.system.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 用户角色接口实现
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {


    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public BaseMapper<UserRole> getMapper() {
        return userRoleMapper;
    }

    @Override
    public List<UserRole> findByRoleId(String roleId) {
        Example example = new Example(UserRole.class);
        example.createCriteria().andEqualTo("roleId", roleId);
        return userRoleMapper.selectByExample(example);
    }

    @Override
    public void deleteByUserId(String userId) {
        Example example = new Example(UserRole.class);
        example.createCriteria().andEqualTo("userId", userId);
        userRoleMapper.deleteByExample(example);
    }


    @Override
    public List<Role> findByUserId(String userId) {
        return userRoleMapper.findByUserId(userId);
    }

    @Override
    public List<String> findDepIdsByUserId(String userId) {
        return userRoleMapper.findDepIdsByUserId(userId);
    }
}
