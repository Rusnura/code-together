package ru.tumasoff.codetogether.dao;

import org.springframework.stereotype.Component;
import ru.tumasoff.codetogether.models.Room;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomSetRepository {
  private final Map<UUID, Room> rooms = new ConcurrentHashMap<>();

  public void save(Room room) {
    if (room.getId() == null)
      throw new IllegalArgumentException("Cannot save room without ID");

    rooms.put(room.getId(), room);
  }

  public Optional<Room> findById(UUID roomId) {
    return Optional.of(rooms.get(roomId));
  }

  public void exists() {

  }
}
