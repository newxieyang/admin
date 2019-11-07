package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.QuartzJob;
import com.cullen.admin.server.system.mapper.QuartzJobMapper;
import com.cullen.admin.server.system.service.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 定时任务接口实现
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@Service
@Transactional
public class QuartzJobServiceImpl implements QuartzJobService {

    @Autowired
    private QuartzJobMapper mapper;

    @Override
    public BaseMapper<QuartzJob> getMapper() {
        return mapper;
    }

    @Override
    public List<QuartzJob> findByJobClassName(String jobClassName) {

        Example example = new Example(QuartzJob.class);
        example.createCriteria().andEqualTo("jobClassName", jobClassName);

        return mapper.selectByExample(example);
    }


    @Override
    public List<QuartzJob> findByCondition() {
        return mapper.selectAll();
    }
}