package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.utils.StrUtils;
import com.cullen.admin.vo.SearchVo;
import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.Department;
import com.cullen.admin.server.system.entity.Permission;
import com.cullen.admin.server.system.entity.Role;
import com.cullen.admin.server.system.entity.User;
import com.cullen.admin.server.system.mapper.DepartmentMapper;
import com.cullen.admin.server.system.mapper.PermissionMapper;
import com.cullen.admin.server.system.mapper.UserMapper;
import com.cullen.admin.server.system.mapper.UserRoleMapper;
import com.cullen.admin.server.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author cullen
 * @date 2019-10-16  23:28
 * @email newxieyang@msn.cn
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public User findByUsername(String username) {

        User user = userMapper.findByUsername(username);
        if (user == null) {
            return null;
        }
        // 关联部门
        if (StrUtils.isNotBlank(user.getDepartmentId())) {
            Department department = departmentMapper.selectByPrimaryKey(user.getDepartmentId());
            if (department != null) {
                user.setDepartmentTitle(department.getTitle());
            }
        }
        // 关联角色
        List<Role> roleList = userRoleMapper.findByUserId(user.getId());
        user.setRoles(roleList);
        // 关联权限菜单
        List<Permission> permissionList = permissionMapper.findByUserId(user.getId());
        user.setPermissions(permissionList);
        return user;
    }

    @Override
    public User findByMobile(String mobile) {
        return userMapper.findByMobile(mobile);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public List<User> findByCondition(User user, SearchVo searchVo) {

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();


        if (StrUtils.isNotBlank(searchVo.getKeyword())) {
            criteria.orLike("nick_name", StrUtils.jointLike(searchVo.getKeyword()))
                    .orLike("username", StrUtils.jointLike(searchVo.getKeyword()));
        }

        //模糊搜素
        if (StrUtils.isNotBlank(user.getUsername())) {
            criteria.orLike("username", StrUtils.jointLike(user.getUsername()));
        }
        if (StrUtils.isNotBlank(user.getMobile())) {
            criteria.orLike("mobile", StrUtils.jointLike(user.getMobile()));
        }
        if (StrUtils.isNotBlank(user.getEmail())) {
            criteria.orLike("email", StrUtils.jointLike(user.getEmail()));
        }

        //部门
        if (StrUtils.isNotBlank(user.getDepartmentId())) {
            criteria.andEqualTo("departmentId", user.getDepartmentId());
        }

        //性别
        if (StrUtils.isNotBlank(user.getSex())) {
            criteria.andEqualTo("sex", user.getSex());
        }
        //类型
        if (user.getType() != null) {
            criteria.andEqualTo("type", user.getType());
        }
        //状态
        if (user.getStatus() != null) {
            criteria.andEqualTo("status", user.getStatus());
        }

        return userMapper.selectByExample(example);
    }

    @Override
    public List<User> findByDepartmentId(String departmentId) {
        return userMapper.findByDepartmentId(departmentId);
    }

    @Override
    public BaseMapper<User> getMapper() {
        return userMapper;
    }


    @Override
    public List<User> getAll() {
        return userMapper.selectAll();
    }
}
