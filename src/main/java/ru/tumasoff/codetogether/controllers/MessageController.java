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
import ru.tumasoff.codetogether.services.TypeService;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class MessageController {
  private final SimpMessagingTemplate messagingTemplate;
  private final RoomService roomService;
  private final ClientService clientService;
  private final ObjectMapper objectMapper;
  private final TypeService typeService;

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

    ObjectNode response = objectMapper.createObjectNode();
    synchronized (this) {
      String pressedKey = message.getKey();
      response.put("username", message.getUsername());
      response.put("roomId", message.getRoomId());
      typeService.process(room, client, pressedKey, message, response);
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
                           ObjectMapper objectMapper,
                           TypeService typeService) {
    this.messagingTemplate = messagingTemplate;
    this.roomService = roomService;
    this.clientService = clientService;
    this.objectMapper = objectMapper;
    this.typeService = typeService;
  }
}
