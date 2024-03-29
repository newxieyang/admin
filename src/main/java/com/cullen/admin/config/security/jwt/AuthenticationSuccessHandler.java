package com.cullen.admin.config.security.jwt;

import com.cullen.admin.annotation.SystemLog;
import com.cullen.admin.config.TokenProperties;
import com.cullen.admin.constant.SecurityConstant;
import com.cullen.admin.enums.LogType;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.utils.ServletUtils;
import com.cullen.admin.utils.StrUtils;
import com.cullen.admin.vo.TokenUser;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登录成功处理类
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private TokenProperties tokenProperties;


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    @SystemLog(description = "登录系统", type = LogType.LOGIN)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //用户选择保存登录状态几天
        String saveLogin = request.getParameter(SecurityConstant.SAVE_LOGIN);
        boolean saved = false;
        if (StrUtils.isNotBlank(saveLogin) && Boolean.valueOf(saveLogin)) {
            saved = true;
            if (!tokenProperties.getRedis()) {
                tokenProperties.setTokenExpireTime(tokenProperties.getSaveLoginTime() * 60 * 24);
            }
        }


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        String username = userDetails.getUsername();

        List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();

        List<String> list = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());


        // 登陆成功生成token
        String token;
        if (tokenProperties.getRedis()) {
            // redis
            token = UUID.randomUUID().toString().replace("-", "");
            TokenUser user = new TokenUser(username, list, saved);
            // 不缓存权限
            if (!tokenProperties.getStorePerms()) {
                user.setPermissions(null);
            }
            // 单设备登录 之前的token失效
            if (tokenProperties.getSdl()) {
                String oldToken = redisTemplate.opsForValue().get(SecurityConstant.USER_TOKEN + username);
                if (StrUtils.isNotBlank(oldToken)) {
                    redisTemplate.delete(SecurityConstant.TOKEN_PRE + oldToken);
                }
            }
            if (saved) {
                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + username, token, tokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), tokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
            } else {
                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + username, token, tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
            }
        } else {
            // 不缓存权限
            if (!tokenProperties.getStorePerms()) {
                list = null;
            }
            // jwt
            token = SecurityConstant.TOKEN_SPLIT + Jwts.builder()
                    //主题 放入用户名
                    .setSubject(username)
                    //自定义属性 放入用户拥有请求权限
                    .claim(SecurityConstant.AUTHORITIES, new Gson().toJson(list))
                    //失效时间
                    .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getTokenExpireTime() * 60 * 1000))
                    //签名算法和密钥
                    .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                    .compact();
        }

        ServletUtils.out(response, ResultUtil.data(token, "登录成功"));
    }
}
