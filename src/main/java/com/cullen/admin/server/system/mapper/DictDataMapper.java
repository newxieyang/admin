package com.cullen.admin.server.system.mapper;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.DictData;
import org.apache.ibatis.annotations.Delete;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@org.apache.ibatis.annotations.Mapper
public interface DictDataMapper extends BaseMapper<DictData> {


    @Delete("delete from sys_dict_data  where dict_id  = #{id}")
    void deleteByDictId(String id);

}
