package com.bruce.config.filter;

import com.bruce.common.filter.BaseRequestFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * BaseRequestFilter
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/23
 */
@Component
public class MyBaseRequestFilter extends BaseRequestFilter {

    @Override
    protected List<String> ignore() {
        return super.ignore();
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
