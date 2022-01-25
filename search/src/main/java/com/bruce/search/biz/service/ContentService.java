package com.bruce.search.biz.service;

import com.bruce.search.biz.domain.ContentStatistics;
import com.bruce.search.biz.model.ContentDetailModel;
import com.bruce.search.biz.repository.ContentDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ArticleService
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2022/1/18
 */
@Service
public class ContentService {

    @Autowired
    private ContentDetailRepository repository;

    public List<ContentDetailModel> search(String keyword) throws Exception {
        List<ContentDetailModel> list = repository.search(keyword);
        return list;
    }

    public List<ContentStatistics> statistics() throws Exception {
        List<ContentStatistics> list = repository.statistics();
        return list;
    }


}
