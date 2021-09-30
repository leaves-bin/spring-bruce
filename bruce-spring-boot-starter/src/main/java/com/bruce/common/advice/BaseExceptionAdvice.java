package com.bruce.common.advice;

import com.bruce.common.exception.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import com.bruce.common.vo.RespData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * BaseExceptionAdvice
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/22
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionAdvice {

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("throw a error on request[{}] , error message is [{}]", request.getRequestURI(), e.getMessage(), e);
        return RespData.error(ErrorCodeEnum.BIZ_ERROR);
    }

}
