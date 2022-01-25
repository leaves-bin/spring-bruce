package com.bruce.search.biz.domain;

import lombok.Data;

/**
 * ContentStatistics
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2022/1/25
 */
@Data
public class ContentStatistics {

    private String author;

    private String docType;

    private String status;

    private long docTypeTotal = 0;

    private long statusTotal = 0;

    private long total = 0;

}
