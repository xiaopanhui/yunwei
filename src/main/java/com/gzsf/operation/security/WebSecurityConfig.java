package com.gzsf.operation.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private ServerSecurityContextRepository securityContextRepository;
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            AccessDeniedHandler accessDeniedHandler,
            ServerHttpSecurity http)
    {
        return http.authorizeExchange()
                .pathMatchers("/login","/add","/public/**","/api/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .securityContextRepository(securityContextRepository)
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .requestCache().disable()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
               .and().build();
    }


    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService()
    {
        return new AuthReactiveUserDetailsService();
    }
}
