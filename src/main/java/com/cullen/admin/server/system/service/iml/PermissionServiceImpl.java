package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.utils.StrUtils;
import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.Permission;
import com.cullen.admin.server.system.mapper.PermissionMapper;
import com.cullen.admin.server.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;


    @Override
    public BaseMapper<Permission> getMapper() {
        return permissionMapper;
    }

    @Override
    public List<Permission> findByUserId(String userId) {

        return permissionMapper.findByUserId(userId);
    }


    @Override
    public List<Permission> findByLevelOrderBySortOrder(Integer level) {
        Example example = new Example(Permission.class);
        example.orderBy("sortOrder").asc();
        example.createCriteria().andEqualTo("level", level);
        return permissionMapper.selectByExample(example);
    }

    @Override
    public List<Permission> findByParentIdOrderBySortOrder(String parentId) {
        Example example = new Example(Permission.class);
        example.orderBy("sortOrder").asc();
        example.createCriteria().andEqualTo("parentId", parentId);
        return permissionMapper.selectByExample(example);
    }

    @Override
    public List<Permission> findByTypeAndStatusOrderBySortOrder(Integer type, Integer status) {
        Example example = new Example(Permission.class);
        example.orderBy("sortOrder").asc();
        example.createCriteria().andEqualTo("type", type)
                .andEqualTo("status", status);
        return permissionMapper.selectByExample(example);
    }

    @Override
    public List<Permission> findByTitle(String title) {
        Example example = new Example(Permission.class);
        example.orderBy("sortOrder").asc();
        example.createCriteria().andEqualTo("title", title);
        return permissionMapper.selectByExample(example);
    }

    @Override
    public List<Permission> findByTitleLikeOrderBySortOrder(String title) {
        Example example = new Example(Permission.class);
        example.orderBy("sortOrder").asc();
        example.createCriteria().andLike("title", StrUtils.jointLike(title));
        return permissionMapper.selectByExample(example);
    }
}
