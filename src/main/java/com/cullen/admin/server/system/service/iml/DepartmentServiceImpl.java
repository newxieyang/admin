package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.base.SecurityUtil;
import com.cullen.admin.server.system.entity.Department;
import com.cullen.admin.server.system.mapper.DepartmentMapper;
import com.cullen.admin.server.system.service.DepartmentService;
import com.cullen.admin.utils.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 部门接口实现
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private SecurityUtil securityUtil;


    @Override
    public BaseMapper<Department> getMapper() {
        return departmentMapper;
    }

    @Override
    public List<Department> findByParentIdOrderBySortOrder(String parentId, Boolean openDataFilter) {

        Example example = new Example(Department.class);
        example.orderBy("sortOrder").asc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);

        // 数据权限
        List<String> depIds = securityUtil.getDepartmentIds();
        if (depIds != null && depIds.size() > 0 && openDataFilter) {
            criteria.andIn("id", depIds);
        }
        return departmentMapper.selectByExample(example);
    }

    @Override
    public List<Department> findByParentIdAndStatusOrderBySortOrder(String parentId, Integer status) {

        Example example = new Example(Department.class);
        example.orderBy("sortOrder").asc();

        example.createCriteria().andEqualTo("parentId", parentId)
                .andEqualTo("status", status);

        return departmentMapper.selectByExample(example);
    }

    @Override
    public List<Department> findByTitleLikeOrderBySortOrder(String title, Boolean openDataFilter) {

        Example example = new Example(Department.class);
        example.orderBy("sortOrder").asc();

        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("title", StrUtils.jointLike(title));

        // 数据权限
        List<String> depIds = securityUtil.getDepartmentIds();
        if (depIds != null && depIds.size() > 0 && openDataFilter) {
            criteria.andIn("id", depIds);
        }
        return departmentMapper.selectByExample(example);

    }
}