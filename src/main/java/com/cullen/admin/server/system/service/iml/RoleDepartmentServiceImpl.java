package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.RoleDepartment;
import com.cullen.admin.server.system.mapper.RoleDepartmentMapper;
import com.cullen.admin.server.system.service.RoleDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 角色部门接口实现
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@Service
@Transactional
public class RoleDepartmentServiceImpl implements RoleDepartmentService {

    @Autowired
    private RoleDepartmentMapper mapper;

    @Override
    public BaseMapper<RoleDepartment> getMapper() {
        return mapper;
    }

    @Override
    public List<RoleDepartment> findByRoleId(String roleId) {

        Example example = new Example(RoleDepartment.class);
        example.createCriteria().andEqualTo("roleId", roleId);
        return mapper.selectByExample(example);
    }

    @Override
    public void deleteByRoleId(String roleId) {

        Example example = new Example(RoleDepartment.class);
        example.createCriteria().andEqualTo("roleId", roleId);
        mapper.deleteByExample(example);

    }

    @Override
    public void deleteByDepartmentId(String departmentId) {

        Example example = new Example(RoleDepartment.class);
        example.createCriteria().andEqualTo("departmentId", departmentId);
        mapper.deleteByExample(example);
    }
}