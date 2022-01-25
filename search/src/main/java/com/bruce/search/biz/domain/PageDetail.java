package com.bruce.search.biz.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author yongbin
 * @date 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "cms_pages")
public class PageDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("contentid")
    private Long contentid;

    @TableField("content")
    private String content;

}
