package com.mig.data.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: fengwen.dong@going-link.com
 * @createDate: 2023-09-11 11:45
 */
@Data
@ApiModel("分页查询: P")
public class P<T> {

    @ApiModelProperty("当前页数据")
    private List<T> records;

    @ApiModelProperty("总条数")
    private long total;

    @ApiModelProperty("每页条数")
    private long size;

    @ApiModelProperty("第几页")
    private long current;

    @ApiModelProperty("sortCursor 游标")
    private Object[] sortCursor;

    public P(List<T> records, long total, long size, long current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
    }
}