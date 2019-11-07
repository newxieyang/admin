package com.cullen.admin.base;

import com.cullen.admin.constant.Constant;
import com.cullen.admin.server.system.entity.Permission;
import com.cullen.admin.server.system.entity.Role;
import com.cullen.admin.server.system.entity.User;
import com.cullen.admin.server.system.service.UserRoleService;
import com.cullen.admin.server.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Component
@Slf4j
public class SecurityUtil {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService iUserRoleService;

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public User getCurrUser() {

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByUsername(user.getUsername());
    }

    /**
     * 获取当前用户数据权限 null代表具有所有权限
     */
    public List<String> getDepartmentIds() {

        User u = getCurrUser();
        // 用户角色
        List<Role> userRoleList = iUserRoleService.findByUserId(u.getId());
        // 判断有无全部数据的角色
        boolean flagAll = false;
        for (Role r : userRoleList) {
            if (r.getDataType() == null || r.getDataType().equals(Constant.DATA_TYPE_ALL)) {
                flagAll = true;
                break;
            }
        }
        if (flagAll) {
            return null;
        }
        // 查找自定义
        return iUserRoleService.findDepIdsByUserId(u.getId());
    }

    /**
     * 通过用户名获取用户拥有权限
     *
     * @param username
     */
    public List<GrantedAuthority> getCurrUserPerms(String username) {

        List<Permission> list = userService.findByUsername(username).getPermissions();
        return list.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getTitle()))
                .collect(Collectors.toList());

    }
}
