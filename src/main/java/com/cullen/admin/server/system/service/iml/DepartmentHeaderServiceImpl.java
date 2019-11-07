package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.DepartmentHeader;
import com.cullen.admin.server.system.mapper.DepartmentHeaderMapper;
import com.cullen.admin.server.system.service.DepartmentHeaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门负责人接口实现
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@Service
@Transactional
public class DepartmentHeaderServiceImpl implements DepartmentHeaderService {

    @Autowired
    private DepartmentHeaderMapper mapper;

    @Override
    public BaseMapper<DepartmentHeader> getMapper() {
        return mapper;
    }

    @Override
    public List<String> findHeaderByDepartmentId(String departmentId, Integer type) {


        Example example = new Example(DepartmentHeader.class);
        example.createCriteria().andEqualTo("departmentId", departmentId).andEqualTo("type", type);

        List<DepartmentHeader> headers = mapper.selectByExample(example);


        List<String> list = new ArrayList<>();
        headers.forEach(e -> {
            list.add(e.getUserId());
        });
        return list;
    }

    @Override
    public void deleteByDepartmentId(String departmentId) {

        mapper.deleteByDepartmentId(departmentId);
    }

    @Override
    public void deleteByUserId(String userId) {

        mapper.deleteByUserId(userId);
    }
}