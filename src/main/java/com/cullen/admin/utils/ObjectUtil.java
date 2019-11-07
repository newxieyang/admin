package com.cullen.admin.utils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public class ObjectUtil {

    public static String mapToString(Map<String, String[]> paramMap) {

        if (paramMap == null) {
            return "";
        }
        Map<String, Object> params = new HashMap<>(16);
        paramMap.forEach((key, value) -> {
            String paramValue = (value != null && value.length > 0 ? value[0] : "");
            String obj = "password".equalsIgnoreCase(key) ? "你是看不见我的" : paramValue;
            params.put(key, obj);
        });

        return new Gson().toJson(params);
    }

    public static String mapToStringAll(Map<String, String[]> paramMap) {

        if (paramMap == null) {
            return "";
        }
        Map<String, Object> params = new HashMap<>(16);
        paramMap.forEach((key, value) -> {
            String paramValue = (value != null && value.length > 0 ? value[0] : "");
            params.put(key, paramValue);
        });

        return new Gson().toJson(params);
    }
}
