package ru.tumasoff.codetogether.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.tumasoff.codetogether.config.WebSocketConfiguration;
import ru.tumasoff.codetogether.models.Message;
import ru.tumasoff.codetogether.models.OutputMessage;

@Controller
public class MessageController {
  @Autowired
  SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/chat/{roomId}")
  public void send(Message message) {
    OutputMessage outputMessage = new OutputMessage(message);
    messagingTemplate.convertAndSend(WebSocketConfiguration.TOPIC_PREFIX + "/" + message.getRoomId(), outputMessage);
  }
}
