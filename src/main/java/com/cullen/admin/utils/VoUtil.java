package com.cullen.admin.utils;

import com.cullen.admin.server.system.entity.Permission;
import com.cullen.admin.vo.MenuVo;
import org.springframework.beans.BeanUtils;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public class VoUtil {

    public static MenuVo permissionToMenuVo(Permission p) {

        MenuVo menuVo = new MenuVo();
        BeanUtils.copyProperties(p, menuVo);
        return menuVo;
    }


}
