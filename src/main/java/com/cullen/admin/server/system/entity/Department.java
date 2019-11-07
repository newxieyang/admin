package com.cullen.admin.server.system.entity;

import com.cullen.admin.base.BaseEntity;
import com.cullen.admin.constant.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Data
@Entity
@Table(name = "sys_department")
@ApiModel(value = "部门")
public class Department extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门名称")
    private String title;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "是否为父节点(含子节点) 默认false")
    private Boolean isParent = false;

    @ApiModelProperty(value = "排序值")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    private Integer status = Constant.STATUS_NORMAL;

    @Transient
    @ApiModelProperty(value = "父节点名称")
    private String parentTitle;

    @Transient
    @ApiModelProperty(value = "主负责人")
    private List<String> mainHeader;

    @Transient
    @ApiModelProperty(value = "副负责人")
    private List<String> viceHeader;
}