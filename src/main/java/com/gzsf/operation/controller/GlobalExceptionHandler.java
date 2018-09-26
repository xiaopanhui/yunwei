package com.gzsf.operation.controller;

import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.bean.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServerRequest;

import java.util.function.BiFunction;

/**
 * 统一异常处理
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    private DefaultDataBufferFactory defaultDataBufferFactory=new DefaultDataBufferFactory();
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Mono<Response> defaultErrorHandler(ServerHttpRequest req, Exception e)  {
        logger.error("SystemError",e);
        logger.error("System Error remote:{} request path:{}",req.getRemoteAddress().getHostString(),req.getPath(),req.getQueryParams());
        return Mono.just(ResponseUtils.systemError());
    }
}
