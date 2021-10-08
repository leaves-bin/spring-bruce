package com.bruce.common.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.common.utils.BeanUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * RespPage
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/27
 */
@EqualsAndHashCode
@ToString
@Data
public final class RespPage<T> implements Serializable {
    /**
     * 总数据
     */
    private long total;
    /**
     * 每页数据
     */
    private long pageSize;
    /**
     * 当前页
     */
    private long current;
    /**
     * 总页数
     */
    private long totalPages;
    /**
     * 返回数据
     */
    private T data;

    public RespPage() {
    }

    public RespPage(long total, long pageSize, long current, T data) {
        this.total = total;
        this.pageSize = pageSize;
        this.current = current;
        this.data = data;
    }

    public static <T> RespPage<T> of(long total, long pageSize, long current, T data) {
        return new RespPage(total, pageSize, current, data);
    }

}
