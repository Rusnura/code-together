package ru.tumasoff.codetogether.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
  public static final String TOPIC_PREFIX = "/code-together/room";

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker(TOPIC_PREFIX);
    registry.setApplicationDestinationPrefixes("/code-together");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/code-together").setAllowedOriginPatterns("*");
    registry.addEndpoint("/code-together").setAllowedOriginPatterns("*").withSockJS();
  }
}
