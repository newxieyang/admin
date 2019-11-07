package com.cullen.admin.utils;


/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public class CommonUtil {


    /**
     * 批量递归删除时 判断target是否在ids中 避免重复删除
     *
     * @param target
     * @param ids
     * @return
     */
    public static Boolean judgeIds(String target, String[] ids) {

        Boolean flag = false;
        for (String id : ids) {
            if (id.equals(target)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
