package com.cullen.admin.ctrl.common;

import cn.hutool.http.HttpUtil;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@RestController
@Api("Security相关接口")
@RequestMapping("/api/common")
public class SecurityCtrl {

    @GetMapping(value = "/needLogin")
    @ApiOperation(value = "没有登录")
    public Result<Object> needLogin() {

        return new ResultUtil<>().setErrorMsg(401, "请先登陆");
    }

    @GetMapping(value = "/swagger/login")
    @ApiOperation(value = "Swagger接口文档专用登录接口 方便测试")
    public Result<Object> swaggerLogin(@RequestParam String username, @RequestParam String password,
                                       @ApiParam("验证码") @RequestParam(required = false) String code,
                                       @ApiParam("图片验证码ID") @RequestParam(required = false) String captchaId,
                                       @ApiParam("可自定义登录接口地址")
                                       @RequestParam(required = false, defaultValue = "http://127.0.0.1:8080/api/login")
                                               String loginUrl) {

        Map<String, Object> params = new HashMap<>(16);
        params.put("username", username);
        params.put("password", password);
        params.put("code", code);
        params.put("captchaId", captchaId);
        String result = HttpUtil.post(loginUrl, params);
        return new ResultUtil<Object>().setData(result);
    }
}
