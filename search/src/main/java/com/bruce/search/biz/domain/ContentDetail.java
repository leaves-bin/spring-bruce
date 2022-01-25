package com.bruce.search.biz.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName(value = "cms_content")
public class ContentDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @TableField("title")
    private String title;

    @TableField("publishDate")
    private String publishDate;

    @TableField("submitDate")
    private String submitDate;

    @TableField("status")
    private Integer status;

    @TableField("docType")
    private Integer docType;

    @TableField("author")
    private String author;

    @TableField("url")
    private String url;

}
