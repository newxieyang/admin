package com.cullen.admin.config.security;

import com.cullen.admin.exception.LoginFailLimitException;
import com.cullen.admin.server.system.entity.User;
import com.cullen.admin.server.system.service.UserService;
import com.cullen.admin.utils.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String flagKey = "loginFailFlag:" + username;
        String value = redisTemplate.opsForValue().get(flagKey);
        Long timeRest = redisTemplate.getExpire(flagKey, TimeUnit.MINUTES);
        if (StrUtils.isNotBlank(value)) {
            //超过限制次数
            throw new LoginFailLimitException("登录错误次数超过限制，请" + timeRest + "分钟后再试");
        }
        User user = userService.findByUsername(username);
        return new SecurityUserDetails(user);
    }
}
