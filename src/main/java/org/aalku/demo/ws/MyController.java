package org.aalku.demo.ws;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@RestController
public class MyController {
	
	private Logger log = LoggerFactory.getLogger(MyController.class);
	
	public final WebSocketHandler wsHandler = new AbstractWebSocketHandler() {
		
		@Override
		public boolean supportsPartialMessages() {
			return false;
		}
		
		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			log.info("afterConnectionEstablished: {}", session);
		}
		
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
			log.error("handleTransportError: {}", exception, exception);
		};
		
		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			try {
				String s = message.getPayload();
				log.info("handleTextMessage: {}", s);
				if (true) {
					session.sendMessage(new TextMessage("(Response to " + s + ")"));
					return;
				}
				sendMessage(session, "{ \"error\": \"No handler for message\"}");
			} catch (Exception e) {
				log.error(e.toString(), e);
				sendMessage(session, "{ \"error\": \"Internal error\"}");
			}
		}
	};
	
	public WebSocketHandler getWsHandler() {
		return wsHandler;
	}

	public void sendMessage(WebSocketSession wss, CharSequence msg) throws IOException {
		synchronized (wss) {
			wss.sendMessage(new TextMessage(msg));
		}
	}

}
