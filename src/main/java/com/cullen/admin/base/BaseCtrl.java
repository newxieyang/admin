package com.cullen.admin.base;


import com.cullen.admin.constant.WebConst;
import com.cullen.admin.utils.ServletUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Objects;

/**
 * web层通用数据处理
 *
 * @Author: 谢洋   newxieyang@msn.cn
 */
public class BaseCtrl {


    /**
     * 设置请求分页数据
     */
    protected void startPage() {

        Integer pageNum = ServletUtils.getParameterToInt(WebConst.PAGE_NUM);
        Integer pageSize = ServletUtils.getParameterToInt(WebConst.PAGE_SIZE);

        String sort = ServletUtils.getParameter("sort");
        String order = ServletUtils.getParameter("order");
        order = Objects.isNull(order) ? "desc" : order;

        if (Objects.nonNull(pageNum) && Objects.nonNull(pageSize)) {
            if (Objects.nonNull(sort)) {
                sort = "createTime".equals(sort) ? "create_time" : sort;
                sort = "sortOrder".equals(sort) ? "sort_order" : sort;
                String orderBy = sort + " " + order;
                PageHelper.startPage(pageNum, pageSize, orderBy);
            } else {
                PageHelper.startPage(pageNum, pageSize);
            }
        }
    }


    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected PageInfo getDataTable(List<?> list) {
        return new PageInfo(list);
    }


}
