package com.cullen.admin.utils.page;

import java.util.ArrayList;
import java.util.List;


/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
public class PageUtil {


    /**
     * 分页显示
     *
     * @param pageNum, pageSize, list
     * @return java.util.List
     * @author cullen
     * @date 2019-10-18 01:22
     */
    public static List listToPage(int pageNum, int pageSize, List list) {

        int pageNumber = pageNum - 1;

        int fromIndex = pageNumber * pageSize;
        int toIndex = pageNumber * pageSize + pageSize;

        if (fromIndex > list.size()) {
            return new ArrayList();
        } else if (toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }
}
