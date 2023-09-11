package com.mig.data.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: BasePageForm
 * @author: fengwen.dong@going-link.com
 * @createDate: 2023-09-11 11:44
 */
@Data
@ApiModel("基础分页信息 BasePageForm")
public class BasePageForm {

    @ApiModelProperty(value = "页条数", example = "20")
    private int pageSize = 20;

    @ApiModelProperty(value = "第几页", example = "1")
    private int pageNum = 1;

    @ApiModelProperty(value = "排序字段: 可选: 不同列表排序不同需再协商", example = "")
    private String orderBy;

    @ApiModelProperty(value = "排序规则 true升序 false降序")
    private Boolean orderType = false;

    @ApiModelProperty("排序游标")
    private Object[] sortCursor;

    @ApiModelProperty("查询所有: 默认查询今日及所有未审核单子")
    private boolean isAll = false;
}