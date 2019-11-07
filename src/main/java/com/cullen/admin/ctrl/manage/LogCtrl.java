package com.cullen.admin.ctrl.manage;


import com.cullen.admin.base.BaseCtrl;
import com.cullen.admin.server.system.entity.Log;
import com.cullen.admin.server.system.service.LogService;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.vo.Result;
import com.cullen.admin.vo.SearchVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api("日志管理接口")
@RequestMapping("/api/log")
@Transactional
public class LogCtrl extends BaseCtrl {


    @Autowired
    private LogService iLogService;

    @GetMapping(value = "/getAllByPage")
    @ApiOperation(value = "分页获取全部")
    public Result<Object> getAllByPage(@RequestParam(required = false) Integer type,
                                       @RequestParam String key,
                                       @ModelAttribute SearchVo searchVo) {

        startPage();
        List<Log> list = iLogService.findByCondition(type, key, searchVo);
        return new ResultUtil<>().setData(getDataTable(list));
    }

    @DeleteMapping(value = "/delByIds/{ids}")
    @ApiOperation(value = "批量删除")
    public Result<Object> delByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iLogService.delete(id);
        }
        return new ResultUtil<>().setSuccessMsg("删除成功");
    }

    @DeleteMapping(value = "/delAll")
    @ApiOperation(value = "全部删除")
    public Result<Object> delAll() {

        iLogService.deleteAll();
        return new ResultUtil<>().setSuccessMsg("删除成功");
    }
}
