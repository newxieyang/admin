package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.constant.Constant;
import com.cullen.admin.utils.StrUtils;
import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.DictData;
import com.cullen.admin.server.system.mapper.DictDataMapper;
import com.cullen.admin.server.system.service.DictDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 字典数据接口实现
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@Service
@Transactional
public class DictDataServiceImpl implements DictDataService {

    @Autowired
    private DictDataMapper dictDataMapper;


    @Override
    public BaseMapper<DictData> getMapper() {
        return dictDataMapper;
    }

    @Override
    public List<DictData> findByCondition(DictData dictData) {


        Example example = new Example(DictData.class);

        Example.Criteria criteria = example.createCriteria();


        //模糊搜素
        if (StrUtils.isNotBlank(dictData.getTitle())) {
            criteria.andLike("title", StrUtils.jointLike(dictData.getTitle()));
        }

        //状态
        if (dictData.getStatus() != null) {
            criteria.andEqualTo("status", dictData.getStatus());
        }

        //所属字典
        if (StrUtils.isNotBlank(dictData.getDictId())) {
            criteria.andEqualTo("dictId", dictData.getDictId());
        }

        return dictDataMapper.selectByExample(example);

    }

    @Override
    public List<DictData> findByDictId(String dictId) {

        Example example = new Example(DictData.class);

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("dictId", dictId);
        criteria.andEqualTo("status", Constant.STATUS_NORMAL);

        example.orderBy("sortOrder").asc();

        return dictDataMapper.selectByExample(example);
    }

    @Override
    public void deleteByDictId(String dictId) {

        dictDataMapper.deleteByDictId(dictId);
    }
}