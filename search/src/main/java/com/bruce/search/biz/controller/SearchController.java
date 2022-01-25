package com.bruce.search.biz.controller;

import com.bruce.search.biz.service.ContentService;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * SearchController
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2022/1/21
 */
@Controller
public class SearchController {

    @Autowired
    private ContentService contentService;

    @RequestMapping(value = {"/index", "/", ""})
    public ModelAndView index(HttpServletRequest request) throws Exception {
        ModelAndView model = new ModelAndView("index");

        String keyword = request.getParameter("keyword");
        if(StringUtils.isNotBlank(keyword)){
            model.addObject("list", contentService.search(keyword));
            model.addObject("keyword",keyword);
        }
        return model;
    }

    @RequestMapping(value = {"/tongji"})
    public ModelAndView tongji(HttpServletRequest request) throws Exception {
        ModelAndView model = new ModelAndView("tongji");

        model.addObject("list", contentService.statistics());
        return model;
    }

}
