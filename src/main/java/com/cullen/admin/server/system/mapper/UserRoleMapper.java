package com.cullen.admin.server.system.mapper;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.Role;
import com.cullen.admin.server.system.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@org.apache.ibatis.annotations.Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {


    /**
     * 通过用户id获取
     *
     * @param userId
     * @return
     */
    List<Role> findByUserId(@Param("userId") String userId);

    /**
     * 通过用户id获取用户角色关联的部门数据
     *
     * @param userId
     * @return
     */
    List<String> findDepIdsByUserId(@Param("userId") String userId);
}
