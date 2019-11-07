package com.cullen.admin.ctrl.manage;

import com.cullen.admin.server.system.entity.Dict;
import com.cullen.admin.server.system.service.DictDataService;
import com.cullen.admin.server.system.service.DictService;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api("字典管理接口")
@RequestMapping("/api/dict")
@Transactional
public class DictCtrl {

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDataService dictDataService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping(value = "/getAll")
    @ApiOperation(value = "获取全部数据")
    public Result<List<Dict>> getAll() {

        List<Dict> list = dictService.findAllOrderBySortOrder();
        return new ResultUtil<List<Dict>>().setData(list);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "添加")
    public Result<Object> add(@ModelAttribute Dict dict) {

        if (dictService.findByType(dict.getType()) != null) {
            return new ResultUtil<>().setErrorMsg("字典类型Type已存在");
        }
        dictService.save(dict);
        return new ResultUtil<>().setSuccessMsg("添加成功");
    }

    @PostMapping(value = "/edit")
    @ApiOperation(value = "编辑")
    public Result<Object> edit(@ModelAttribute Dict dict) {

        Dict old = dictService.get(dict.getId());
        // 若type修改判断唯一
        if (!old.getType().equals(dict.getType()) && dictService.findByType(dict.getType()) != null) {
            return new ResultUtil<>().setErrorMsg("字典类型Type已存在");
        }
        dictService.update(dict);
        return new ResultUtil<>().setSuccessMsg("编辑成功");
    }

    @DeleteMapping(value = "/delByIds/{id}")
    @ApiOperation(value = "通过id删除")
    public Result<Object> delAllByIds(@PathVariable String id) {


        Dict dict = dictService.get(id);
        dictService.delete(id);
        dictDataService.deleteByDictId(id);
        // 删除缓存
        redisTemplate.delete("dictData::" + dict.getType());
        return new ResultUtil<>().setSuccessMsg("删除成功");
    }

    @GetMapping(value = "/search")
    @ApiOperation(value = "搜索字典")
    public Result<List<Dict>> searchPermissionList(@RequestParam String key) {

        List<Dict> list = dictService.findByTitleOrTypeLike(key);
        return new ResultUtil<List<Dict>>().setData(list);
    }
}
