package com.cullen.admin.server.system.service;


import com.cullen.admin.vo.SearchVo;
import com.cullen.admin.base.BaseService;
import com.cullen.admin.server.system.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 用户接口
 *
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@CacheConfig(cacheNames = "user")
public interface UserService extends BaseService<User> {

    /**
     * 通过用户名获取用户
     *
     * @param username
     * @return
     */
    @Cacheable(key = "#username")
    User findByUsername(String username);

    /**
     * 通过手机获取用户
     *
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 通过邮件和状态获取用户
     *
     * @param email
     * @return
     */
    User findByEmail(String email);


    /**
     * 多条件分页获取用户
     *
     * @param user
     * @param searchVo
     * @return
     */
    List<User> findByCondition(User user, SearchVo searchVo);

    /**
     * 通过部门id获取
     *
     * @param departmentId
     * @return
     */
    List<User> findByDepartmentId(String departmentId);


    /**
     * 获取所有用户
     *
     * @return
     */
    List<User> getAll();
}
