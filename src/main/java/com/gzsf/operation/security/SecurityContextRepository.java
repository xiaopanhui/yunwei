package com.gzsf.operation.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        ServerHttpRequest request=serverWebExchange.getRequest();
        List<String> authorization= request.getHeaders().get("Authorization");
        String token;
        if (authorization==null ||authorization.size()==0){
            token = request.getQueryParams().getFirst("token");
        }else {
            token = authorization.get(0);
        }
        if (token==null){
            serverWebExchange.getAttributes().put("isLogin",false);
            return Mono.just(new SecurityContextImpl(new UserAuthentication()));
        }
        Authentication userAuth= userAuthRepository.load(token);
        if (userAuth!=null&&userAuth.isAuthenticated()){
            userAuthRepository.expire(token);
            serverWebExchange.getAttributes().put("isLogin",true);
        }else {
            serverWebExchange.getAttributes().put("isLogin",false);

        }
        return Mono.just(new SecurityContextImpl(userAuth));
    }
}
