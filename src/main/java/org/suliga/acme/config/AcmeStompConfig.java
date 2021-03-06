package org.suliga.acme.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class AcmeStompConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic", "/queue"); // memory broker - forward to subscribed clients - send to cient: /topic/backgammon/...
		// registry.enableStompBrokerRelay("/queue", "/topic"); // real STOMP broker such as RabbitMQ or ActiveMQ

		registry.setApplicationDestinationPrefixes("/stomp"); // application destinations - forward to controller - javascript sends: /stomp/backgammon/...

	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/minesweeper").withSockJS(); // stomp/minesweeper
		registry.addEndpoint("/dailydiet").withSockJS();
		registry.addEndpoint("/mazegen").withSockJS();
		registry.addEndpoint("/primegen").withSockJS();
		registry.addEndpoint("/backgammon").withSockJS();
	}
}

