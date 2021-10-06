package com.bruce.config;

import com.bruce.common.GatewayErrorCodeEnum;
import com.bruce.common.GatewayException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

import java.util.Map;

/**
 * 自定义异常处理
 *
 * <p>异常时用JSON代替HTML异常信息<p>
 *
 * @author yinjihuan
 */
@Slf4j
public class GatewayExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GatewayExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                   ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected ErrorAttributeOptions getErrorAttributeOptions(ServerRequest request, MediaType mediaType) {
        return super.getErrorAttributeOptions(request, mediaType);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = super.getError(request);
        if (error instanceof GatewayException) {
            GatewayException gatewayException = (GatewayException) error;
            return response(gatewayException.getErrCode(), gatewayException.getErrMsg());
        } else {
            error.printStackTrace();
            log.error("gateway throw a error ", error);
            return response("500", "服务器错误");
        }
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 根据code获取对应的HttpStatus
     *
     * @param errorAttributes
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        Object errorCode = errorAttributes.get("errorCode");
        if (null != errorCode && GatewayErrorCodeEnum.AUTH_ERROR.getCode().equals(errorCode.toString())) {
            return HttpStatus.UNAUTHORIZED.value();
        }
        return HttpStatus.OK.value();
    }

    /**
     * 构建异常信息
     *
     * @param request
     * @param ex
     * @return
     */
    private String buildMessage(ServerRequest request, Throwable ex) {

        StringBuilder message = new StringBuilder("Failed to handle request [");
        message.append(request.methodName());
        message.append(" ");
        message.append(request.uri());
        message.append("]");
        if (ex != null) {
            message.append(": ");
            message.append(ex.getMessage());
        }
        return message.toString();
    }

    public static Map<String, Object> response(String status, String errorMessage) {
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("errorCode", status);
        maps.put("errorMsg", errorMessage);
        maps.put("traceId", MDC.get("X-B3-TraceId"));
        maps.put("result", null);
        maps.put("success", false);
        maps.put("timestamp", System.currentTimeMillis());
        return maps;
    }

}