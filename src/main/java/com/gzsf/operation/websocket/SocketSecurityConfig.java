package com.gzsf.operation.websocket;

import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;

public class SocketSecurityConfig {
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/secured/**").authenticated()
                .anyMessage().authenticated();
    }
}