package com.gzsf.operation.controller;

import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.bean.Response;
import com.gzsf.operation.exception.BaseException;
import com.gzsf.operation.exception.NoCmdToRunException;
import com.gzsf.operation.exception.NoFileFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServerRequest;

import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 统一异常处理
 *
 */
//@ControllerAdvice
@Controller
public class GlobalExceptionHandler {
    //日志记录
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    private DefaultDataBufferFactory defaultDataBufferFactory=new DefaultDataBufferFactory();
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Mono<Response> defaultErrorHandler(ServerHttpRequest req, Exception e)  {
        if (e instanceof AccessDeniedException){
            return Mono.just(ResponseUtils.accessDenied());
        }
        if (e instanceof BaseException){
            return Mono.just(new Response(((BaseException) e).getError(),e.getMessage()));
        }
        if (e instanceof WebExchangeBindException){
            List<ObjectError> errors = ((WebExchangeBindException) e).getAllErrors();
            StringBuffer buffer=new StringBuffer();
            int count=0;
            for (ObjectError error: errors) {
                if (!(error instanceof FieldError)){
                    continue;
                }
                if (count>0){
                    buffer.append(",");
                }
                count++;
                buffer.append(((FieldError) error).getField());
                buffer.append(error.getDefaultMessage());
            }
            return Mono.just(ResponseUtils.paramError(buffer.toString()));
        }

        logger.error("SystemError",e);
        logger.error("System Error remote:{} request path:{}",req.getRemoteAddress().getHostString(),req.getPath(),req.getQueryParams());
        return Mono.just(ResponseUtils.systemError());
    }

    @ResponseBody
    @RequestMapping("**")
    public Mono<Response> statusError(ServerHttpRequest req){
        return Mono.just(ResponseUtils.notFound());
    }
}
