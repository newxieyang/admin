package com.cullen.admin.server.system.mapper;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/***
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */

@org.apache.ibatis.annotations.Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 通过用户id获取
     *
     * @param userId
     * @return
     */
    List<Permission> findByUserId(@Param("userId") String userId);
}
