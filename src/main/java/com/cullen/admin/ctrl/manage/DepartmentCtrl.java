package com.cullen.admin.ctrl.manage;

import com.cullen.admin.base.SecurityUtil;
import com.cullen.admin.constant.Constant;
import com.cullen.admin.exception.TatuException;
import com.cullen.admin.server.system.entity.Department;
import com.cullen.admin.server.system.entity.DepartmentHeader;
import com.cullen.admin.server.system.entity.User;
import com.cullen.admin.server.system.service.DepartmentHeaderService;
import com.cullen.admin.server.system.service.DepartmentService;
import com.cullen.admin.server.system.service.RoleDepartmentService;
import com.cullen.admin.server.system.service.UserService;
import com.cullen.admin.utils.CommonUtil;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.utils.StrUtils;
import com.cullen.admin.vo.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
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
@Api("部门管理接口")
@RequestMapping("/api/department")
@CacheConfig(cacheNames = "department")
@Transactional
public class DepartmentCtrl {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private DepartmentHeaderService departmentHeaderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping(value = "/getByParentId/{parentId}")
    @ApiOperation(value = "通过id获取")
    public Result<List<Department>> getByParentId(@PathVariable String parentId,
                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter) {

        List<Department> list;
        User u = securityUtil.getCurrUser();
        String key = "department::" + parentId + ":" + u.getId() + "_" + openDataFilter;
        String v = redisTemplate.opsForValue().get(key);
        if (StrUtils.isNotBlank(v)) {
            list = new Gson().fromJson(v, new TypeToken<List<Department>>() {
            }.getType());
            return new ResultUtil<List<Department>>().setData(list);
        }
        list = departmentService.findByParentIdOrderBySortOrder(parentId, openDataFilter);
        list = setInfo(list);

        String val2 = new Gson().toJson(list);

        redisTemplate.opsForValue().set(key,
                val2);
        return new ResultUtil<List<Department>>().setData(list);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "添加")
    public Result<Object> add(@ModelAttribute Department department) {

        int d = departmentService.save(department);
        // 同步该节点缓存
        User u = securityUtil.getCurrUser();
        Set<String> keys = redisTemplate.keys("department::" + department.getParentId() + ":*");
        redisTemplate.delete(keys);
        // 如果不是添加的一级 判断设置上级为父节点标识
        if (!Constant.PARENT_ID.equals(department.getParentId())) {
            Department parent = departmentService.get(department.getParentId());
            if (parent.getIsParent() == null || !parent.getIsParent()) {
                parent.setIsParent(true);
                departmentService.update(parent);
                // 更新上级节点的缓存
                Set<String> keysParent = redisTemplate.keys("department::" + parent.getParentId() + ":*");
                redisTemplate.delete(keysParent);
            }
        }
        return new ResultUtil<Object>().setSuccessMsg("添加成功");
    }

    @PostMapping(value = "/edit")
    @ApiOperation(value = "编辑")
    public Result<Object> edit(@ModelAttribute Department department,
                               @RequestParam(required = false) String[] mainHeader,
                               @RequestParam(required = false) String[] viceHeader) {

        int d = departmentService.update(department);
        // 先删除原数据
        departmentHeaderService.deleteByDepartmentId(department.getId());
        for (String id : mainHeader) {
            DepartmentHeader dh = new DepartmentHeader();
            dh.setUserId(id);
            dh.setDepartmentId(department.getId());
            dh.setType(Constant.HEADER_TYPE_MAIN);
            departmentHeaderService.save(dh);
        }
        for (String id : viceHeader) {
            DepartmentHeader dh = new DepartmentHeader();
            dh.setUserId(id);
            dh.setDepartmentId(department.getId());
            dh.setType(Constant.HEADER_TYPE_VICE);
            departmentHeaderService.save(dh);
        }
        // 手动删除所有部门缓存
        Set<String> keys = redisTemplate.keys("department:" + "*");
        redisTemplate.delete(keys);
        // 删除所有用户缓存
        Set<String> keysUser = redisTemplate.keys("user:" + "*");
        redisTemplate.delete(keysUser);
        return new ResultUtil<Object>().setSuccessMsg("编辑成功");
    }

    @DeleteMapping(value = "/delByIds/{ids}")
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            deleteRecursion(id, ids);
        }
        // 手动删除所有部门缓存
        Set<String> keys = redisTemplate.keys("department:" + "*");
        redisTemplate.delete(keys);
        // 删除数据权限缓存
        Set<String> keysUserRoleData = redisTemplate.keys("userRole::depIds:" + "*");
        redisTemplate.delete(keysUserRoleData);
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    public void deleteRecursion(String id, String[] ids) {

        List<User> list = userService.findByDepartmentId(id);
        if (list != null && list.size() > 0) {
            throw new TatuException("删除失败，包含正被用户使用关联的部门");
        }
        // 获得其父节点
        Department dep = departmentService.get(id);
        Department parent = null;
        if (dep != null && StrUtils.isNotBlank(dep.getParentId())) {
            parent = departmentService.get(dep.getParentId());
        }
        departmentService.delete(id);
        // 删除关联数据权限
        roleDepartmentService.deleteByDepartmentId(id);
        // 删除关联部门负责人
        departmentHeaderService.deleteByDepartmentId(id);
        // 判断父节点是否还有子节点
        if (parent != null) {
            List<Department> childrenDeps = departmentService.findByParentIdOrderBySortOrder(parent.getId(), false);
            if (childrenDeps == null || childrenDeps.size() == 0) {
                parent.setIsParent(false);
                departmentService.update(parent);
            }
        }
        // 递归删除
        List<Department> departments = departmentService.findByParentIdOrderBySortOrder(id, false);
        for (Department d : departments) {
            if (!CommonUtil.judgeIds(d.getId(), ids)) {
                deleteRecursion(d.getId(), ids);
            }
        }
    }

    @GetMapping(value = "/search")
    @ApiOperation(value = "部门名模糊搜索")
    public Result<List<Department>> searchByTitle(@RequestParam String title,
                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter) {

        List<Department> list = departmentService.findByTitleLikeOrderBySortOrder(title, openDataFilter);
        list = setInfo(list);
        return new ResultUtil<List<Department>>().setData(list);
    }

    public List<Department> setInfo(List<Department> list) {

        // lambda表达式
        list.forEach(item -> {
            if (!Constant.PARENT_ID.equals(item.getParentId())) {
                Department parent = departmentService.get(item.getParentId());
                item.setParentTitle(parent.getTitle());
            } else {
                item.setParentTitle("一级部门");
            }
            // 设置负责人
            item.setMainHeader(departmentHeaderService.findHeaderByDepartmentId(item.getId(), Constant.HEADER_TYPE_MAIN));
            item.setViceHeader(departmentHeaderService.findHeaderByDepartmentId(item.getId(), Constant.HEADER_TYPE_VICE));
        });
        return list;
    }
}
