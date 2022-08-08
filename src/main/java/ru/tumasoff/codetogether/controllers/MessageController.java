package ru.tumasoff.codetogether.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import ru.tumasoff.codetogether.config.WebSocketConfiguration;
import ru.tumasoff.codetogether.models.Message;
import ru.tumasoff.codetogether.models.OutputMessage;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
  @Autowired
  SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/room/{roomId}")
  public void send(Message message) {
    OutputMessage outputMessage = new OutputMessage(message);
    messagingTemplate.convertAndSend(WebSocketConfiguration.TOPIC_PREFIX + "/" + message.getRoomId(), outputMessage);
  }

  @SubscribeMapping("/room/{roomId}")
  public void subscribeEvent(@DestinationVariable("roomId") String roomId,
                             SimpMessageHeaderAccessor accessor) {
    List<String> usernameHeaders = accessor.getNativeHeader("username");
    if (usernameHeaders == null || usernameHeaders.size() != 1) {
      throw new IllegalArgumentException("Username native header isn't illegal (headers size != 1)!");
    }
    String username = usernameHeaders.get(0);
    if (username == null) {
      throw new IllegalArgumentException("Username native header isn't illegal (header is null)!");
    }

    OutputMessage outputMessage = new OutputMessage();
    outputMessage.setType("CONNECT");
    outputMessage.setRoomId(roomId);
    outputMessage.setTime(new Date());
    outputMessage.setUsername(username);
    messagingTemplate.convertAndSend(WebSocketConfiguration.TOPIC_PREFIX + "/" + roomId, outputMessage);
  }
}
