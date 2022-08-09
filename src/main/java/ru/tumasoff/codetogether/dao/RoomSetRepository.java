package ru.tumasoff.codetogether.dao;

import org.springframework.stereotype.Component;
import ru.tumasoff.codetogether.models.Room;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomSetRepository {
  private final Map<String, Room> rooms = new ConcurrentHashMap<>();

  public void save(Room room) {
    if (room.getId() == null)
      throw new IllegalArgumentException("Cannot save room without ID");

    if (rooms.get(room.getId()) == null) {
      synchronized (RoomSetRepository.class) {
        rooms.putIfAbsent(room.getId(), room);
      }
    }
  }

  public Optional<Room> findById(String roomId) {
    return Optional.ofNullable(rooms.get(roomId));
  }
}
