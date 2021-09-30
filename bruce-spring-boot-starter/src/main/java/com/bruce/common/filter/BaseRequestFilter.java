package com.bruce.common.filter;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * BaseRequestFilter
 * 类描述：公共过滤器配置
 *
 * @author:zhangyongbin5
 * @date:2021/9/23
 */
@Slf4j
public class BaseRequestFilter extends OncePerRequestFilter implements Ordered, InitializingBean {

    private static final String IGNORE_CONTENT_TYPE = "multipart/form-data";

    public BaseRequestFilter() {
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
    }

    /**
     * 忽略处理的url
     *
     * @return
     */
    protected List<String> ignore() {
        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!this.isRequestValid(request)) {
            filterChain.doFilter(request, response);
        } else {
            if (!(request instanceof ContentCachingRequestWrapper)) {
                request = new ContentCachingRequestWrapper(request);
            }

            if (!(response instanceof ContentCachingResponseWrapper)) {
                response = new ContentCachingResponseWrapper(response);
            }
            int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
            long startTime = System.currentTimeMillis();

            try {
                filterChain.doFilter(request, response);
                status = (response).getStatus();
            } finally {
                String path = request.getRequestURI();
                HttpLog traceLog = new HttpLog();
                traceLog.setPath(path);
                traceLog.setMethod(request.getMethod());
                long cost = System.currentTimeMillis() - startTime;
                traceLog.setCost(cost);
                traceLog.setStatus(status);

                List<String> ignore = ignore();
                if (!Objects.equals(IGNORE_CONTENT_TYPE, getContentType(request))) {
                    if (ignore == null || !ignore.contains(path)) {
                        traceLog.setRequestBody(getRequestBody(request));
                        traceLog.setResponseBody(getResponseBody(response));
                        traceLog.setHeader(getHeaderInfo(request));
                    }
                }
                log.info("http trace log: {}", JSON.toJSONString(traceLog));
                this.updateResponse(response);
            }
        }
    }

    private String getContentType(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            contentType = "";
        }
        int semicolon = contentType.indexOf(';');
        if (semicolon >= 0) {
            contentType = contentType.substring(0, semicolon).trim();
        } else {
            contentType = contentType.trim();
        }
        return contentType;
    }

    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException var3) {
            return false;
        }
    }

    private Map<String, String> getHeaderInfo(HttpServletRequest request) {
        Map<String, String> header = new HashMap();
        Enumeration<String> requestHeaderNames = request.getHeaderNames();
        while (requestHeaderNames.hasMoreElements()) {
            String key = requestHeaderNames.nextElement();
            String value = request.getHeader(key);
            header.put(key, value);
        }
        return header;
    }

    private String getRequestBody(HttpServletRequest request) {
        String requestBody = "";
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            try {
                requestBody = new String(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
                if (requestBody != null) {
                    requestBody = JSON.toJSONString(request.getParameterMap());
                }
            } catch (IOException e) {
                log.debug("BaseRequestFilter-getRequestBody throw io error []", e.getMessage());
            }
        }
        return requestBody;
    }

    private String getResponseBody(HttpServletResponse response) {
        String responseBody = "";
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            try {
                responseBody = new String(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            } catch (IOException e) {
                log.debug("BaseRequestFilter-getResponseBody throw io error []", e.getMessage());
            }
        }
        return responseBody;
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }

    @Data
    @ToString
    private static class HttpLog {
        private String path;
        private String method;
        private long cost;
        private Integer status;
        private String requestBody;
        private String responseBody;
        private Map<String, String> header;
    }

}
