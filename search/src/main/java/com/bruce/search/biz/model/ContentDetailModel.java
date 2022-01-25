package com.bruce.search.biz.model;

import lombok.Data;

/**
 * ContentDetailModel
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2022/1/20
 */
@Data
public class ContentDetailModel {

    private String title;

    private String publishDate;

    private String submitDate;

    private Integer status;

    private Integer docType;

    private String author;

    private String url;

    private String content;

    private String html;

    private float score;

    private String summary;
}
