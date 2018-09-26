package com.gzsf.operation.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.bean.Response;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccessDeniedHandler implements ServerAccessDeniedHandler {
    private final List<String> contentType=new ArrayList<>();
    private final DefaultDataBufferFactory factory=new DefaultDataBufferFactory();
    private  DataBuffer dataBuffer;
    @Autowired
    public AccessDeniedHandler(ObjectMapper objectMapper) {
        contentType.add("application/json;charset=UTF-8");
        Response response= ResponseUtils.accessDenied();
        try {
            byte[] bytes= objectMapper.writeValueAsBytes(response);
            dataBuffer= factory.wrap(bytes);
        } catch (JsonProcessingException e1) {
        }
    }

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, AccessDeniedException e) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        serverWebExchange.getResponse().getHeaders().put("Content-Type",contentType);
        return  serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
