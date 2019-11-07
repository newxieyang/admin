package com.cullen.admin.server.system.mapper;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.DepartmentHeader;
import org.apache.ibatis.annotations.Delete;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@org.apache.ibatis.annotations.Mapper
public interface DepartmentHeaderMapper extends BaseMapper<DepartmentHeader> {


    @Delete("delete from sys_department_header where user_id =  #{userId}")
    void deleteByUserId(String userId);


    @Delete("delete from sys_department_header where department_id =  #{departmentId}")
    void deleteByDepartmentId(String departmentId);
}
