package com.cullen.admin.server.system.service.iml;

import com.cullen.admin.exception.TatuException;
import com.cullen.admin.utils.StrUtils;
import com.cullen.admin.base.BaseMapper;
import com.cullen.admin.server.system.entity.Dict;
import com.cullen.admin.server.system.mapper.DictMapper;
import com.cullen.admin.server.system.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 字典接口实现
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@Service
@Transactional
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;


    @Override
    public List<Dict> findAllOrderBySortOrder() {

        Example example = new Example(Dict.class);
        example.orderBy("sortOrder").asc();

        return dictMapper.selectByExample(example);

    }

    @Override
    public Dict findByType(String type) {
        Example example = new Example(Dict.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", type);
        List<Dict> list = dictMapper.selectByExample(example);

        if (list.isEmpty()) {
            return null;
        }

        if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new TatuException("数据错误");
        }

    }

    @Override
    public List<Dict> findByTitleOrTypeLike(String key) {
        Example example = new Example(Dict.class);
        example.orderBy("sortOrder").asc();
        Example.Criteria criteria = example.createCriteria();
        criteria.orLike("title", StrUtils.jointLike(key));
        criteria.orLike("type", StrUtils.jointLike(key));

        return dictMapper.selectByExample(example);
    }


    @Override
    public BaseMapper<Dict> getMapper() {
        return dictMapper;
    }
}