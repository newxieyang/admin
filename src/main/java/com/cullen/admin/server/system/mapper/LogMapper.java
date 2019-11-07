package com.cullen.admin.server.system.mapper;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.Log;
import org.apache.ibatis.annotations.Delete;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@org.apache.ibatis.annotations.Mapper
public interface LogMapper extends BaseMapper<Log> {


    @Delete("delete from sys_log")
    void deleteAll();

}
