package com.example.socialnetwork.config.websocket;

import com.example.socialnetwork.config.authencation.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://127.0.0.1:5500")
                .withSockJS()
              ;
//          .withSockJS()
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(10 * 1024 * 1024);
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue/");

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                SimpMessageHeaderAccessor accessorHeader = SimpMessageHeaderAccessor.wrap(message);
                System.out.println(accessorHeader.getDestination());

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = getJwtFromRequest(accessor.getFirstNativeHeader("Authorization"));
                    if (token != null &&  jwtTokenProvider.validateToken(token)) {
                        log.info("Authorization Susscess: " + token);
                        accessor.getSessionAttributes().put("Authorization", "Bearer " + token);
                        accessor.getSessionAttributes().put("username", jwtTokenProvider.getUsernameFromJWT(token));
                        return message;
                    }
                }
                if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) ) {
                    String token= getJwtFromRequest((String) accessor.getSessionAttributes().get("Authorization"));
                    if (token != null &&  jwtTokenProvider.validateToken(token)) {
                        log.info("Authorization Subscribe Susscess");
                    }else{
                        return null;
                    }

                }
                if(accessorHeader.getDestination() != null && accessorHeader.getDestination().contains("/queue/specific-user/")) {
                    String username = (String) accessor.getSessionAttributes().get("username");
                    String userNameHeader =   accessorHeader.getDestination().replace("/queue/specific-user/", "");
                    log.info(userNameHeader + "---" + username +"-------"+ !username.equals(userNameHeader));
                    if(username == null || !username.equals(userNameHeader)){
                        log.info("Loi");
                        return null;
                    }
                }

                    if (StompCommand.SEND.equals(accessor.getCommand())) {
                    String token= getJwtFromRequest((String) accessor.getSessionAttributes().get("Authorization")) ;
                    if (token != null &&  jwtTokenProvider.validateToken(token)) {
                        String username = (String) accessor.getSessionAttributes().get("username");
                        if(username == null){
                            return null;
                        }
                        accessorHeader.setHeader("username", username);
                        log.info("Username " + username);
                        log.info("Authorization SEND Susscess");

                        return message;
                    }
                    return null;

                }

            return message;
            }
        });
    }
    private String getJwtFromRequest(String token ) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

}
