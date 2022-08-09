package ru.tumasoff.codetogether.services;

import org.springframework.stereotype.Service;
import ru.tumasoff.codetogether.dao.ClientSetRepository;
import ru.tumasoff.codetogether.models.Client;
import ru.tumasoff.codetogether.models.Room;
import java.util.Optional;

@Service
public class ClientService {
  private final ClientSetRepository clientRepo;
  private final RoomService roomService;

  public void create(String roomId, Client client) {
    Optional<Room> roomOpt = roomService.findById(roomId);
    roomOpt
      .map(Room::getClients)
      .ifPresent(map -> map.putIfAbsent(client.getUsername(), client));
  }

  public Optional<Client> findByUsername(String roomId, String username) {
    Optional<Room> roomOpt = roomService.findById(roomId);
    if (roomOpt.isEmpty())
      return Optional.empty();

    Room room = roomOpt.get();
    return Optional.ofNullable(
      room
      .getClients()
      .get(username)
    );
  }

  public ClientService(ClientSetRepository clientRepo, RoomService roomService) {
    this.clientRepo = clientRepo;
    this.roomService = roomService;
  }
}
