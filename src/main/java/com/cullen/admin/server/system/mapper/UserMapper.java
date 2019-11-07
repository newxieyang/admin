package com.cullen.admin.server.system.mapper;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends BaseMapper<User> {


    @Select("select * from sys_user where username = #{username}")
    User findByUsername(String username);

    @Select("select * from sys_user where mobile = #{mobile}")
    User findByMobile(String mobile);

    @Select("select * from sys_user where email = #{email}")
    User findByEmail(String email);


    @Select("select * from sys_user where department_id = #{departmentId}")
    List<User> findByDepartmentId(String departmentId);

}
