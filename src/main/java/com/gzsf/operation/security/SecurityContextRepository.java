package com.gzsf.operation.security;

import org.springframework.beans.factory.annotation.Autowired;
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
        List<String> authorization= serverWebExchange.getRequest().getHeaders().get("Authorization");
        if (authorization==null||authorization.size()==0){
            return Mono.just(new SecurityContextImpl(new UserAuthentication()));
        }
        Authentication userAuth= userAuthRepository.load(authorization.get(0));
        if (userAuth!=null&&userAuth.isAuthenticated()){
            userAuthRepository.expire(authorization.get(0));
        }
        return Mono.just(new SecurityContextImpl(userAuth));
    }
}
