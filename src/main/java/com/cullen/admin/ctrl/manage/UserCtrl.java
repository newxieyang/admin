package com.cullen.admin.ctrl.manage;


import com.cullen.admin.base.BaseCtrl;
import com.cullen.admin.base.SecurityUtil;
import com.cullen.admin.server.system.entity.Department;
import com.cullen.admin.server.system.entity.Role;
import com.cullen.admin.server.system.entity.User;
import com.cullen.admin.server.system.entity.UserRole;
import com.cullen.admin.server.system.service.DepartmentHeaderService;
import com.cullen.admin.server.system.service.DepartmentService;
import com.cullen.admin.server.system.service.UserRoleService;
import com.cullen.admin.server.system.service.UserService;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.utils.StrUtils;
import com.cullen.admin.vo.Result;
import com.cullen.admin.vo.UserVo;
import com.cullen.admin.constant.Constant;
import com.cullen.admin.vo.Result;
import com.cullen.admin.vo.SearchVo;
import com.cullen.admin.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@Api("用户接口")
@RequestMapping("/api/user")
@CacheConfig(cacheNames = "user")
@Transactional
public class UserCtrl extends BaseCtrl {


    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserRoleService iUserRoleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private DepartmentHeaderService departmentHeaderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;


    @Autowired
    private UserService iUserService;


    @GetMapping(value = "/info")
    @ApiOperation(value = "获取当前登录用户接口")
    public Result<User> getUserInfo() {

        User u = securityUtil.getCurrUser();
        // 清除持久上下文环境 避免后面语句导致持久化
        u.setPassword(null);
        return new ResultUtil<User>().setData(u);
    }

    @PostMapping(value = "/unlock")
    @ApiOperation(value = "解锁验证密码")
    public Result<Object> unLock(@RequestParam String password) {

        User u = securityUtil.getCurrUser();
        if (!new BCryptPasswordEncoder().matches(password, u.getPassword())) {
            return new ResultUtil<Object>().setErrorMsg("密码不正确");
        }
        return new ResultUtil<Object>().setData(null);
    }

    @PostMapping(value = "/resetPass")
    @ApiOperation(value = "重置密码")
    public Result<Object> resetPass(@RequestParam String[] ids) {

        for (String id : ids) {
            User u = iUserService.get(id);
            u.setPassword(new BCryptPasswordEncoder().encode("123456"));
            iUserService.update(u);
            redisTemplate.delete("user::" + u.getUsername());
        }
        return ResultUtil.success("操作成功");
    }

    @PostMapping(value = "/edit")
    @ApiOperation(value = "修改用户自己资料", notes = "用户名密码不会修改 需要username更新缓存")
    @CacheEvict(key = "#u.username")
    public Result<Object> editOwn(@ModelAttribute User u) {

        User old = securityUtil.getCurrUser();
        u.setUsername(old.getUsername());
        u.setPassword(old.getPassword());
        int res = iUserService.update(u);
        if (res != 1) {
            return new ResultUtil<>().setErrorMsg("修改失败");
        }
        return new ResultUtil<>().setSuccessMsg("修改成功");
    }

    /**
     * @param u
     * @return
     */
    @PostMapping(value = "/admin/edit")
    @ApiOperation(value = "管理员修改资料", notes = "需要通过id获取原用户信息 需要username更新缓存")
    @CacheEvict(key = "#u.username")
    public Result<Object> edit(@ModelAttribute UserVo u) {

        User old = iUserService.get(u.getId());
        //若修改了用户名
        if (!old.getUsername().equals(u.getUsername())) {
            //若修改用户名删除原用户名缓存
            redisTemplate.delete("user::" + old.getUsername());
            //判断新用户名是否存在
            if (iUserService.findByUsername(u.getUsername()) != null) {
                return new ResultUtil<>().setErrorMsg("该用户名已被存在");
            }
        }

        // 若修改了手机和邮箱判断是否唯一
        if (!old.getMobile().equals(u.getMobile()) && iUserService.findByMobile(u.getMobile()) != null) {
            return new ResultUtil<>().setErrorMsg("该手机号已绑定其他账户");
        }
        if (!old.getEmail().equals(u.getEmail()) && iUserService.findByMobile(u.getEmail()) != null) {
            return new ResultUtil<>().setErrorMsg("该邮箱已绑定其他账户");
        }

        u.setPassword(old.getPassword());

        BeanUtils.copyProperties(u, old);

        int res = iUserService.update(old);
        if (res != 1) {
            return new ResultUtil<>().setErrorMsg("修改失败");
        }
        //删除该用户角色
        userRoleService.deleteByUserId(u.getId());
        if (u.getRoles() != null && u.getRoles().length > 0) {
            //新角色
            for (String roleId : u.getRoles()) {
                UserRole ur = new UserRole();
                ur.setRoleId(roleId);
                ur.setUserId(u.getId());
                userRoleService.save(ur);
            }
        }
        //手动删除缓存
        redisTemplate.delete("userRole::" + u.getId());
        redisTemplate.delete("userRole::depIds:" + u.getId());
        redisTemplate.delete("userPermission::" + u.getId());
        redisTemplate.delete("permission::userMenuList:" + u.getId());
        return new ResultUtil<Object>().setSuccessMsg("修改成功");
    }

    /**
     * 线上demo不允许测试账号改密码
     *
     * @param password
     * @param newPass
     * @return
     */
    @PostMapping(value = "/modifyPass")
    @ApiOperation(value = "修改密码")
    public Result<Object> modifyPass(@ApiParam("旧密码") @RequestParam String password,
                                     @ApiParam("新密码") @RequestParam String newPass) {

        User user = securityUtil.getCurrUser();

        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return new ResultUtil<>().setErrorMsg("旧密码不正确");
        }

        String newEncryptPass = new BCryptPasswordEncoder().encode(newPass);
        user.setPassword(newEncryptPass);
        iUserService.update(user);

        //手动更新缓存
        redisTemplate.delete("user::" + user.getUsername());

        return new ResultUtil<>().setSuccessMsg("修改密码成功");
    }

    @GetMapping(value = "/getByCondition")
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<Object> getByCondition(@ModelAttribute User user,
                                         @ModelAttribute SearchVo searchVo) {

        startPage();

        List<User> page = iUserService.findByCondition(user, searchVo);
        for (User u : page) {
            // 关联部门
            if (StrUtils.isNotBlank(u.getDepartmentId())) {
                Department department = departmentService.get(u.getDepartmentId());
                u.setDepartmentTitle(department.getTitle());
            }
            // 关联角色
            List<Role> list = iUserRoleService.findByUserId(u.getId());
            u.setRoles(list);
            // 清除持久上下文环境 避免后面语句导致持久化
            u.setPassword(null);
        }
        return new ResultUtil<>().setData(getDataTable(page));
    }


    @GetMapping(value = "/getByDepartmentId/{departmentId}")
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<List<User>> getByCondition(@PathVariable String departmentId) {

        List<User> list = iUserService.findByDepartmentId(departmentId);
        list.forEach(u -> {
            u.setPassword(null);
        });
        return new ResultUtil<List<User>>().setData(list);
    }

    @GetMapping(value = "/getAll")
    @ApiOperation(value = "获取全部用户数据")
    public Result<List<User>> getByCondition() {

        List<User> list = iUserService.getAll();
        for (User u : list) {
            // 关联部门
            if (StrUtils.isNotBlank(u.getDepartmentId())) {
                Department department = departmentService.get(u.getDepartmentId());
                u.setDepartmentTitle(department.getTitle());
            }
            // 清除持久上下文环境 避免后面语句导致持久化
            u.setPassword(null);
        }
        return new ResultUtil<List<User>>().setData(list);
    }

    @PostMapping(value = "/admin/add")
    @ApiOperation(value = "添加用户")
    public Result<Object> regist(@ModelAttribute User u,
                                 @RequestParam(required = false) String[] roles) {

        if (StrUtils.isBlank(u.getUsername()) || StrUtils.isBlank(u.getPassword())) {
            return new ResultUtil<>().setErrorMsg("缺少必需表单字段");
        }

        if (iUserService.findByUsername(u.getUsername()) != null) {
            return new ResultUtil<>().setErrorMsg("该用户名已被注册");
        }

        String encryptPass = new BCryptPasswordEncoder().encode(u.getPassword());
        u.setPassword(encryptPass);
        int res = iUserService.save(u);
        if (res != 1) {
            return new ResultUtil<>().setErrorMsg("添加失败");
        }
        if (roles != null && roles.length > 0) {
            //添加角色
            for (String roleId : roles) {
                UserRole ur = new UserRole();
                ur.setUserId(u.getId());
                ur.setRoleId(roleId);
                userRoleService.save(ur);
            }
        }

        return new ResultUtil<>().setSuccessMsg("添加成功");
    }

    @PostMapping(value = "/admin/disable/{userId}")
    @ApiOperation(value = "后台禁用用户")
    public Result<Object> disable(@ApiParam("用户唯一id标识") @PathVariable String userId) {

        return changeState(userId, Constant.USER_STATUS_LOCK);
    }

    @PostMapping(value = "/admin/enable/{userId}")
    @ApiOperation(value = "后台启用用户")
    public Result<Object> enable(@ApiParam("用户唯一id标识") @PathVariable String userId) {

        return changeState(userId, Constant.USER_STATUS_NORMAL);
    }


    private Result<Object> changeState(String userId, int state) {
        User user = iUserService.get(userId);
        if (user == null) {
            return new ResultUtil<>().setErrorMsg("通过userId获取用户失败");
        }
        user.setStatus(state);
        iUserService.update(user);
        //手动更新缓存
        redisTemplate.delete("user::" + user.getUsername());
        return new ResultUtil<>().setData(null);
    }


    @DeleteMapping(value = "/delByIds/{ids}")
    @ApiOperation(value = "批量通过ids删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            User u = iUserService.get(id);
            //删除缓存
            redisTemplate.delete("user::" + u.getUsername());
            redisTemplate.delete("userRole::" + u.getId());
            redisTemplate.delete("userRole::depIds:" + u.getId());
            redisTemplate.delete("permission::userMenuList:" + u.getId());
            Set<String> keys = redisTemplate.keys("department::*");
            redisTemplate.delete(keys);
            iUserService.delete(id);
            //删除关联角色
            userRoleService.deleteByUserId(id);
            //删除关联部门负责人
            departmentHeaderService.deleteByUserId(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

}
