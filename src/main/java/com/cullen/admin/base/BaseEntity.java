package com.cullen.admin.base;

import com.cullen.admin.constant.Constant;
import com.cullen.admin.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @author cullen
 * @date 2019-07-13  11:34
 * @email newxieyang@msn.cn
 */
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {


    @ApiModelProperty(value = "唯一标识")
    @Column(name = "id")
    @Id
    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    @ApiModelProperty(value = "创建者")
    @Column(name = "create_by")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time")
    private Long createTime = System.currentTimeMillis();

    @ApiModelProperty(value = "更新者")
    @Column(name = "update_by")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_time")
    private Long updateTime = System.currentTimeMillis();

    @ApiModelProperty(value = "删除标志 默认0")
    @Column(name = "state")
    private Integer state = Constant.STATUS_NORMAL;
}
