package com.cullen.admin.ctrl.common;

import com.cullen.admin.utils.IpInfoUtil;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@RestController
@Api("IP接口")
@RequestMapping("/api/common/ip")
@Transactional
public class IpInfoCtrl {

    @Autowired
    private IpInfoUtil ipInfoUtil;

    @GetMapping(value = "/info")
    @ApiOperation(value = "IP及天气相关信息")
    public Result<Object> upload(HttpServletRequest request) {

//        String result= ipInfoUtil.getIpWeatherInfo(ipInfoUtil.getIpAddr(request));
//        return new ResultUtil<Object>().setData(result);
        return new ResultUtil<Object>().setErrorMsg("feel so sad");
    }
}