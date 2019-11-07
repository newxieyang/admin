package com.cullen.admin.config.security;

import com.cullen.admin.constant.Constant;
import com.cullen.admin.utils.StrUtils;
import com.cullen.admin.server.system.entity.Permission;
import com.cullen.admin.server.system.entity.Role;
import com.cullen.admin.server.system.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
public class SecurityUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 1L;

    public SecurityUserDetails(User user) {

        if (user != null) {
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
            this.setStatus(user.getStatus());
            this.setRoles(user.getRoles());
            this.setPermissions(user.getPermissions());
        }
    }

    /**
     * 添加用户拥有的权限和角色
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<Permission> permissions = this.getPermissions();
        List<GrantedAuthority> authorityList = new ArrayList<>();
        // 添加请求权限
        if (permissions != null && permissions.size() > 0) {
            authorityList = permissions.stream().filter(permission ->
                    Constant.PERMISSION_OPERATION.equals(permission.getType())
                            && StrUtils.isNotBlank(permission.getTitle())
                            && StrUtils.isNotBlank(permission.getPath()))
                    .map(permission -> new SimpleGrantedAuthority(permission.getTitle()))
                    .collect(Collectors.toList());

        }


        // 添加角色
        List<Role> roles = this.getRoles();
        if (roles != null && roles.size() > 0) {

            authorityList.addAll(
                    roles.stream().filter(item -> StrUtils.isNotBlank(item.getName()))
                            .map(item -> new SimpleGrantedAuthority(item.getName()))
                            .collect(Collectors.toList()));
        }
        return authorityList;
    }

    /**
     * 账户是否过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否禁用
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {

        return !Constant.USER_STATUS_LOCK.equals(this.getStatus());
    }

    /**
     * 密码是否过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return Constant.USER_STATUS_NORMAL.equals(this.getStatus());
    }
}