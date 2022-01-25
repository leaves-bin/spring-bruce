package com.bruce.search;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.search.biz.domain.ContentDetail;
import com.bruce.search.biz.domain.PageDetail;
import com.bruce.search.biz.mapper.ContentDetailMapper;
import com.bruce.search.biz.mapper.PageDetailMapper;
import com.bruce.search.biz.model.ContentDetailModel;
import com.bruce.search.biz.repository.ContentDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.htmlparser.Parser;
import org.htmlparser.visitors.TextExtractingVisitor;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SearchApplication.class)
@WebAppConfiguration
@Slf4j
public class ContentDetailTest {


    @Autowired
    private ContentDetailMapper contentDetailMapper;
    @Autowired
    private PageDetailMapper pageDetailMapper;
    @Autowired
    private ContentDetailRepository contentDetailRepository;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private int pageSize = 200;

    @Test
    public void createData() throws Exception {
        int pageNum = 1000;
        Page<ContentDetail> page = contentDetailMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<>());
        while (CollectionUtils.isNotEmpty(page.getRecords())) {
            log.info("获取地{}页数据", pageNum);
            ++pageNum;

            submitTask(page.getRecords());
            page = contentDetailMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<>());
        }
    }

    private void submitTask(List<ContentDetail> list) {
        threadPoolTaskExecutor.execute(() -> {

            try {
                index(list);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public void index(List<ContentDetail> contentDetails) throws Exception {
        Set<Long> ids = contentDetails.stream().map(ContentDetail::getId).collect(Collectors.toSet());
        List<PageDetail> pages = pageDetailMapper.selectPages(ids);

        Map<Long, List<PageDetail>> pageMap = pages.stream().collect(Collectors.groupingBy(PageDetail::getContentid));

        List<ContentDetailModel> models = contentDetails.stream().map(item -> {
            ContentDetailModel model = new ContentDetailModel();
            model.setTitle(item.getTitle());
            model.setPublishDate(item.getPublishDate());
            model.setSubmitDate(item.getSubmitDate());
            model.setStatus(item.getStatus());
            model.setDocType(item.getDocType());
            model.setAuthor(item.getAuthor());
            model.setUrl(item.getUrl());

            List<PageDetail> details = pageMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(details)) {
                String content = details.get(0).getContent();
                if (StringUtils.isNotBlank(content)) {
                    try {
                        Parser parser = new Parser(content);
                        TextExtractingVisitor visitor = new TextExtractingVisitor();
                        parser.visitAllNodesWith(visitor);
                        model.setContent(visitor.getExtractedText());
                    } catch (Exception e) {
                        model.setContent(content);
                    }
                    if (content.length() >= 10000) {
                        content = content.substring(0, 10000);
                    }
                    model.setHtml(content);
                }
            }
            return model;
        }).collect(Collectors.toList());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String indexName = "content_detail_" + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH);
        contentDetailRepository.save(models, indexName);
    }

}
