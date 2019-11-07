package com.cullen.admin.ctrl.manage;

import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.base.BaseCtrl;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.vo.Result;
import com.cullen.admin.vo.SearchVo;
import com.cullen.admin.server.system.entity.Dict;
import com.cullen.admin.server.system.entity.DictData;
import com.cullen.admin.server.system.service.DictDataService;
import com.cullen.admin.server.system.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@RestController
@Api("字典数据管理接口")
@RequestMapping("/api/dictData")
@CacheConfig(cacheNames = "dictData")
@Transactional
public class DictDataCtrl extends BaseCtrl {

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDataService iDictDataService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping(value = "/getByCondition")
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<Object> getByCondition(@ModelAttribute DictData dictData) {

        startPage();
        List<DictData> list = iDictDataService.findByCondition(dictData);
        return new ResultUtil<>().setData(getDataTable(list));
    }

    @GetMapping(value = "/getByType/{type}")
    @ApiOperation(value = "通过类型获取")
    @Cacheable(key = "#type")
    public Result<Object> getByType(@PathVariable String type) {

        Dict dict = dictService.findByType(type);
        if (dict == null) {
            return new ResultUtil<>().setErrorMsg("字典类型Type不存在");
        }
        List<DictData> list = iDictDataService.findByDictId(dict.getId());
        return new ResultUtil<>().setData(list);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "添加")
    public Result<Object> add(@ModelAttribute DictData dictData) {

        Dict dict = dictService.get(dictData.getDictId());
        if (dict == null) {
            return new ResultUtil<>().setErrorMsg("字典类型id不存在");
        }
        iDictDataService.save(dictData);
        // 删除缓存
        redisTemplate.delete("dictData::" + dict.getType());
        return new ResultUtil<>().setSuccessMsg("添加成功");
    }

    @PostMapping(value = "/edit")
    @ApiOperation(value = "编辑")
    public Result<Object> edit(@ModelAttribute DictData dictData) {

        iDictDataService.update(dictData);
        // 删除缓存
        Dict dict = dictService.get(dictData.getDictId());
        redisTemplate.delete("dictData::" + dict.getType());
        return new ResultUtil<>().setSuccessMsg("编辑成功");
    }

    @DeleteMapping(value = "/delByIds/{ids}")
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            DictData dictData = iDictDataService.get(id);
            Dict dict = dictService.get(dictData.getDictId());
            iDictDataService.delete(id);
            // 删除缓存
            redisTemplate.delete("dictData::" + dict.getType());
        }
        return new ResultUtil<>().setSuccessMsg("批量通过id删除数据成功");
    }
}
