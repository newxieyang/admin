package com.cullen.admin.config.jwt;

import com.cullen.admin.config.TokenProperties;
import com.cullen.admin.exception.LoginFailLimitException;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.utils.ServletUtils;
import com.cullen.admin.utils.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author cullen
 * @date 2019-07-13  11:34
 * @email newxieyang@msn.cn
 */
@Slf4j
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            String username = request.getParameter("username");
            recordLoginTime(username);
            String key = "loginTimeLimit:" + username;
            String value = redisTemplate.opsForValue().get(key);
            if (StrUtils.isBlank(value)) {
                value = "0";
            }
            //获取已登录错误次数
            int loginFailTime = Integer.parseInt(value);
            int restLoginTime = tokenProperties.getLoginTimeLimit() - loginFailTime;
            log.info("用户" + username + "登录失败，还有" + restLoginTime + "次机会");
            if (restLoginTime <= 3 && restLoginTime > 0) {
                ServletUtils.out(response, ResultUtil.error("用户名或密码错误，还有" + restLoginTime + "次尝试机会"));
            } else if (restLoginTime <= 0) {
                ServletUtils.out(response, ResultUtil.error("登录错误次数超过限制，请" + tokenProperties.getLoginAfterTime() + "分钟后再试"));
            } else {
                ServletUtils.out(response, ResultUtil.error("用户名或密码错误"));
            }
        } else if (e instanceof DisabledException) {
            ServletUtils.out(response, ResultUtil.error("账户被禁用，请联系管理员"));
        } else if (e instanceof LoginFailLimitException) {
            ServletUtils.out(response, ResultUtil.error(((LoginFailLimitException) e).getMsg()));
        } else {
            ServletUtils.out(response, ResultUtil.error("登录失败，其他内部错误"));
        }
    }

    /**
     * 判断用户登陆错误次数
     */
    private boolean recordLoginTime(String username) {

        String key = "loginTimeLimit:" + username;
        String flagKey = "loginFailFlag:" + username;
        String value = redisTemplate.opsForValue().get(key);
        if (StrUtils.isBlank(value)) {
            value = "0";
        }
        //获取已登录错误次数
        int loginFailTime = Integer.parseInt(value) + 1;
        redisTemplate.opsForValue().set(key, String.valueOf(loginFailTime), tokenProperties.getLoginAfterTime(), TimeUnit.MINUTES);
        if (loginFailTime >= tokenProperties.getLoginTimeLimit()) {
            redisTemplate.opsForValue().set(flagKey, "fail", tokenProperties.getLoginAfterTime(), TimeUnit.MINUTES);
            return false;
        }
        return true;
    }
}
