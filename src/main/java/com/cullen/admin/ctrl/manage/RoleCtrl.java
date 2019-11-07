package com.cullen.admin.ctrl.manage;


import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.vo.Result;
import com.cullen.admin.base.BaseCtrl;
import com.cullen.admin.server.system.entity.Role;
import com.cullen.admin.server.system.entity.RoleDepartment;
import com.cullen.admin.server.system.entity.RolePermission;
import com.cullen.admin.server.system.entity.UserRole;
import com.cullen.admin.server.system.service.RoleDepartmentService;
import com.cullen.admin.server.system.service.RolePermissionService;
import com.cullen.admin.server.system.service.RoleService;
import com.cullen.admin.server.system.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@RestController
@Api("角色管理接口")
@RequestMapping("/api/role")
@Transactional
public class RoleCtrl extends BaseCtrl {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping(value = "/getAllList")
    @ApiOperation(value = "获取全部角色")
    public Result<Object> roleGetAll() {

        List<Role> list = roleService.findByCondition();
        return new ResultUtil<Object>().setData(list);
    }

    @GetMapping(value = "/getAllByPage")
    @ApiOperation(value = "分页获取角色")
    public Result<Object> getRoleByPage() {

        startPage();
        List<Role> list = roleService.findByCondition();
        for (Role role : list) {
            // 角色拥有权限
            List<RolePermission> permissions = rolePermissionService.findByRoleId(role.getId());
            role.setPermissions(permissions);
            // 角色拥有数据权限
            List<RoleDepartment> departments = roleDepartmentService.findByRoleId(role.getId());
            role.setDepartments(departments);
        }
        return new ResultUtil<>().setData(getDataTable(list));
    }

    @PostMapping(value = "/setDefault")
    @ApiOperation(value = "设置或取消默认角色")
    public Result<Object> setDefault(@RequestParam String id,
                                     @RequestParam Boolean isDefault) {

        Role role = roleService.get(id);
        if (role == null) {
            return new ResultUtil<Object>().setErrorMsg("角色不存在");
        }
        role.setDefaultRole(isDefault);
        roleService.update(role);
        return new ResultUtil<Object>().setSuccessMsg("设置成功");
    }

    @PostMapping(value = "/editRolePerm")
    @ApiOperation(value = "编辑角色分配菜单权限")
    public Result<Object> editRolePerm(@RequestParam String roleId,
                                       @RequestParam(required = false) String[] permIds) {

        //删除其关联权限
        rolePermissionService.deleteByRoleId(roleId);
        //分配新权限
        for (String permId : permIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permId);
            rolePermissionService.save(rolePermission);
        }
        //手动批量删除缓存
        Set<String> keysUser = redisTemplate.keys("user:" + "*");
        redisTemplate.delete(keysUser);
        Set<String> keysUserRole = redisTemplate.keys("userRole:" + "*");
        redisTemplate.delete(keysUserRole);
        Set<String> keysUserPerm = redisTemplate.keys("userPermission:" + "*");
        redisTemplate.delete(keysUserPerm);
        Set<String> keysUserMenu = redisTemplate.keys("permission::userMenuList:*");
        redisTemplate.delete(keysUserMenu);
        return new ResultUtil<Object>().setData(null);
    }

    @PostMapping(value = "/editRoleDep")
    @ApiOperation(value = "编辑角色分配数据权限")
    public Result<Object> editRoleDep(@RequestParam String roleId,
                                      @RequestParam Integer dataType,
                                      @RequestParam(required = false) String[] depIds) {

        Role r = roleService.get(roleId);
        r.setDataType(dataType);
        roleService.update(r);
        // 删除其关联数据权限
        roleDepartmentService.deleteByRoleId(roleId);
        // 分配新数据权限
        for (String depId : depIds) {
            RoleDepartment roleDepartment = new RoleDepartment();
            roleDepartment.setRoleId(roleId);
            roleDepartment.setDepartmentId(depId);
            roleDepartmentService.save(roleDepartment);
        }
        // 手动删除相关缓存
        Set<String> keys = redisTemplate.keys("department:" + "*");
        redisTemplate.delete(keys);
        Set<String> keysUserRole = redisTemplate.keys("userRole:" + "*");
        redisTemplate.delete(keysUserRole);

        return new ResultUtil<Object>().setData(null);
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "保存数据")
    public Result<Role> save(@ModelAttribute Role role) {

        int r = roleService.save(role);
        return new ResultUtil<Role>().setSuccessMsg("保存成功");
    }

    @PostMapping(value = "/edit")
    @ApiOperation(value = "更新数据")
    public Result<Role> edit(@ModelAttribute Role entity) {

        int r = roleService.update(entity);
        //手动批量删除缓存
        Set<String> keysUser = redisTemplate.keys("user:" + "*");
        redisTemplate.delete(keysUser);
        Set<String> keysUserRole = redisTemplate.keys("userRole:" + "*");
        redisTemplate.delete(keysUserRole);
        return new ResultUtil<Role>().setSuccessMsg("更新成功");
    }

    @DeleteMapping(value = "/delAllByIds/{ids}")
    @ApiOperation(value = "批量通过ids删除")
    public Result<Object> delByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            List<UserRole> list = userRoleService.findByRoleId(id);
            if (list != null && list.size() > 0) {
                return new ResultUtil<Object>().setErrorMsg("删除失败，包含正被用户使用关联的角色");
            }
        }
        for (String id : ids) {
            roleService.delete(id);
            //删除关联菜单权限
            rolePermissionService.deleteByRoleId(id);
            //删除关联数据权限
            roleDepartmentService.deleteByRoleId(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

}
