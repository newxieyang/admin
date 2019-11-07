package com.cullen.admin.constant;

/**
 * @author cullen
 * @date 2019-10-05  18:24
 * @email newxieyang@msn.cn
 */
public class Constant {

    /**
     * 正常状态
     */
    public static Integer STATUS_NORMAL = 0;

    /**
     * 禁用状态
     */
    public static Integer STATUS_DISABLE = -1;

    /**
     * 删除标志
     */
    public static Integer STATUS_DELETE = 1;


    /**
     * 用户默认头像
     */
    public static String USER_DEFAULT_AVATAR = "https://i.loli.net/2019/04/28/5cc5a71a6e3b6.png";

    /**
     * 用户正常状态
     */
    public static Integer USER_STATUS_NORMAL = 0;

    /**
     * 用户禁用状态
     */
    public static Integer USER_STATUS_LOCK = -1;

    /**
     * 普通用户
     */
    public static Integer USER_TYPE_NORMAL = 0;

    /**
     * 管理员
     */
    public static Integer USER_TYPE_ADMIN = 1;

    /**
     * 全部数据权限
     */
    public static Integer DATA_TYPE_ALL = 0;

    /**
     * 自定义数据权限
     */
    public static Integer DATA_TYPE_CUSTOM = 1;


    /**
     * 删除标志
     */
    public static Integer DEL_FLAG = 1;

    /**
     * 限流标识
     */
    public static String LIMIT_ALL = "SPIDER_LIMIT_ALL";

    /**
     * 顶部菜单类型权限
     */
    public static Integer PERMISSION_NAV = -1;

    /**
     * 页面类型权限
     */
    public static Integer PERMISSION_PAGE = 0;

    /**
     * 操作类型权限
     */
    public static Integer PERMISSION_OPERATION = 1;

    /**
     * 1级菜单父id
     */
    public static String PARENT_ID = "0";

    /**
     * 0级菜单
     */
    public static Integer LEVEL_ZERO = 0;

    /**
     * 1级菜单
     */
    public static Integer LEVEL_ONE = 1;

    /**
     * 2级菜单
     */
    public static Integer LEVEL_TWO = 2;

    /**
     * 3级菜单
     */
    public static Integer LEVEL_THREE = 3;

    /**
     * 部门负责人类型 主负责人
     */
    public static Integer HEADER_TYPE_MAIN = 0;

    /**
     * 部门负责人类型 副负责人
     */
    public static Integer HEADER_TYPE_VICE = 1;

}
