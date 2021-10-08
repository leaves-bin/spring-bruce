package com.bruce.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * BaseQuery
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/10/6
 */
@Data
public class BaseQuery implements Serializable {
    /**
     * 每页数据
     */
    private long pageSize = 15;
    /**
     * 当前页
     */
    private long pageNum = 1;
}
