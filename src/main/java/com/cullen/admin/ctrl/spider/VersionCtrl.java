package com.cullen.admin.ctrl.spider;

import com.cullen.admin.constant.Constant;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.vo.Result;
import com.cullen.admin.vo.SearchVo;
import com.cullen.admin.base.BaseCtrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cullen
 * @date 2019-09-22  19:47
 * @email newxieyang@msn.cn
 */
@RestController
@Api("返乡车队api")
@RequestMapping("api/version")
public class VersionCtrl extends BaseCtrl {

//
//    @Autowired
//    VersionService service;
//
//
//    @GetMapping("list")
//    public Result<Object> list(SearchVo searchVo) {
//        startPage();
//        return new ResultUtil<>().setData(getDataTable(service.list(searchVo)));
//    }
//
//
//    @PostMapping("save")
//    public Result save(Version version) {
//
//        version.setUpdateBy(null);
//        version.setCreateTime(System.currentTimeMillis());
//        int num = service.save(version);
//
//        return new Result(num > 0);
//    }
//
//
//    @PostMapping("update")
//    public Result update(Version version) {
//        version.setCreateBy(null);
//        version.setUpdateTime(System.currentTimeMillis());
//        int num = service.update(version);
//        return new Result(num > 0);
//    }
//
//
//    @PostMapping(value = "/delByIds/{id}")
//    @ApiOperation(value = "删除版本")
//    public Result<Object> delVersion(@ApiParam("用户唯一id标识") @PathVariable String id) {
//        int num = service.delete(id);
//        return new Result(num > 0);
//    }
//
//    @PostMapping(value = "/disable/{id}")
//    @ApiOperation(value = "禁用版本")
//    public Result<Object> disable(@ApiParam("用户唯一id标识") @PathVariable String id) {
//        return updateState(id, Constant.USER_STATUS_LOCK);
//    }
//
//    @PostMapping(value = "/enable/{id}")
//    @ApiOperation(value = "启用版本")
//    public Result<Object> enable(@ApiParam("用户唯一id标识") @PathVariable String id) {
//        return updateState(id, Constant.USER_STATUS_NORMAL);
//    }
//
//    private Result<Object> updateState(String id, int state) {
//        Version version = service.get(id);
//        if (version == null) {
//            return new ResultUtil<>().setErrorMsg("通过Id获取版本失败");
//        }
//        version.setState(state);
//        service.update(version);
//        //手动更新缓存
//        return new ResultUtil<>().setData(null);
//    }


}
