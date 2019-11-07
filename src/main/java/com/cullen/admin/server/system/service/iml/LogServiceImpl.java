package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.utils.StrUtils;
import com.cullen.admin.vo.SearchVo;
import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.Log;
import com.cullen.admin.server.system.mapper.LogMapper;
import com.cullen.admin.server.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author cullen
 * @date 2019-10-16  23:42
 * @email newxieyang@msn.cn
 */
@Service
public class LogServiceImpl implements LogService {


    @Autowired
    private LogMapper logMapper;

    @Override
    public List<Log> findByCondition(Integer type, String key, SearchVo searchVo) {

        Example example = new Example(Log.class);
        Example.Criteria criteria = example.createCriteria();
        if (StrUtils.isNotBlank(key)) {
            criteria.orLike("name", StrUtils.jointLike(key));
            criteria.orLike("requestUrl", StrUtils.jointLike(key));
            criteria.orLike("requestType", StrUtils.jointLike(key));
            criteria.orLike("requestParam", StrUtils.jointLike(key));
            criteria.orLike("username", StrUtils.jointLike(key));
            criteria.orLike("ip", StrUtils.jointLike(key));
            criteria.orLike("ipInfo", StrUtils.jointLike(key));
        }


        return logMapper.selectByExample(example);
    }

    @Override
    public void deleteAll() {
        logMapper.deleteAll();
    }

    @Override
    public BaseMapper<Log> getMapper() {
        return logMapper;
    }
}
