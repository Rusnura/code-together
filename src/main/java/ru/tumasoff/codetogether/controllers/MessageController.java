package ru.tumasoff.codetogether.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import ru.tumasoff.codetogether.config.WebSocketConfiguration;
import ru.tumasoff.codetogether.models.Client;
import ru.tumasoff.codetogether.models.Message;
import ru.tumasoff.codetogether.models.OutputMessage;
import ru.tumasoff.codetogether.models.Room;
import ru.tumasoff.codetogether.services.ClientService;
import ru.tumasoff.codetogether.services.RoomService;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class MessageController {
  private final SimpMessagingTemplate messagingTemplate;
  private final RoomService roomService;
  private final ClientService clientService;
  private final ObjectMapper objectMapper;

  @MessageMapping("/room/{roomId}")
  public void send(Message message) throws Exception {
    Optional<Room> roomOpt = roomService.findById(message.getRoomId());
    if (roomOpt.isEmpty())
      return;

    Optional<Client> clientOpt = clientService.findByUsername(message.getRoomId(), message.getUsername());
    if (clientOpt.isEmpty())
      return;

    Room room = roomOpt.get();
    Client client = clientOpt.get();

    String pressedKey = message.getKey();
    ObjectNode response = objectMapper.createObjectNode();
    response.put("username", message.getUsername());
    response.put("roomId", message.getRoomId());
    synchronized (this) {
      if (pressedKey == null) { // click message - navigation stuff
        response.put("type", "navigation");
        response.put("startCursorPosition", message.getStartCursorPosition());
        response.put("endCursorPosition", message.getEndCursorPosition());
        client.setSelectionStartPosition(message.getStartCursorPosition());
        client.setSelectionEndPosition(message.getEndCursorPosition());
      } else if (pressedKey.length() == 1 || "Enter".equals(pressedKey)) { // It's a char pressed?
        response.put("type", "insert");
        String k = (pressedKey.length() == 1) ? pressedKey : "\n";
        response.put("key", k);
        int start = client.getSelectionStartPosition();
        int end = client.getSelectionEndPosition();
        String before = room.getText().substring(0, start);
        String after = room.getText().substring(end);
        String text = before + k + after;
        room.setText(text);
        response.put("text", text);
//        client.setSelectionStartPosition(message.getStartCursorPosition());
//        client.setSelectionEndPosition(message.getEndCursorPosition());
      } else { // 1st: it's navigation stuff?
        response.put("type", "navigation");
        response.put("startCursorPosition", message.getStartCursorPosition());
        response.put("endCursorPosition", message.getEndCursorPosition());
        client.setSelectionStartPosition(message.getStartCursorPosition());
        client.setSelectionEndPosition(message.getEndCursorPosition());
      }
    }

    messagingTemplate.convertAndSend(WebSocketConfiguration.TOPIC_PREFIX + "/" + message.getRoomId(), response);
  }

  @SubscribeMapping("/room/{roomId}")
  public void subscribeEvent(@DestinationVariable("roomId") String roomId,
                             SimpMessageHeaderAccessor accessor) {
    List<String> usernameHeaders = accessor.getNativeHeader("username");
    if (usernameHeaders == null || usernameHeaders.size() != 1)
      throw new IllegalArgumentException("Username native header isn't illegal (headers size != 1)!");

    String username = usernameHeaders.get(0);
    if (username == null)
      throw new IllegalArgumentException("Username native header isn't illegal (header is null)!");

    if (roomService.findById(roomId).isEmpty())
      roomService.create(new Room(roomId));

    Client client = new Client(username);
    clientService.create(roomId, client);

    Optional<Room> roomOpt = roomService.findById(roomId);
    if (roomOpt.isEmpty())
      return;
    Room room = roomOpt.get();
    OutputMessage connectionMessage = new OutputMessage();
    connectionMessage.setType("connect");
    connectionMessage.setRoomId(room.getId());
    connectionMessage.setTime(new Date());
    connectionMessage.setUsername(username);

    ObjectNode roomInfo = objectMapper.createObjectNode();
    roomInfo.put("text", room.getText());

    ArrayNode clients = objectMapper.createArrayNode();
    room.getClients().values().forEach(c -> {
      ObjectNode cc = objectMapper.createObjectNode();
      cc.put("username", c.getUsername());
      cc.put("startCursorPosition", c.getSelectionStartPosition());
      cc.put("endCursorPosition", c.getSelectionStartPosition());
      clients.add(cc);
    });
    roomInfo.set("clients", clients);
    messagingTemplate.convertAndSend(WebSocketConfiguration.TOPIC_PREFIX + "/" + roomId, connectionMessage);
    messagingTemplate.convertAndSend(WebSocketConfiguration.TOPIC_PREFIX + "/" + roomId + "/status", roomInfo);
  }

  public MessageController(SimpMessagingTemplate messagingTemplate,
                           RoomService roomService,
                           ClientService clientService,
                           ObjectMapper objectMapper) {
    this.messagingTemplate = messagingTemplate;
    this.roomService = roomService;
    this.clientService = clientService;
    this.objectMapper = objectMapper;
  }
}
