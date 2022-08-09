package ru.tumasoff.codetogether.services;

import org.springframework.stereotype.Service;
import ru.tumasoff.codetogether.dao.RoomSetRepository;
import ru.tumasoff.codetogether.models.Room;
import java.util.Optional;

@Service
public class RoomService {
  private final RoomSetRepository roomRepo;

  public void create(Room room) {
    roomRepo.save(room);
  }

  public Optional<Room> findById(String id) {
    return roomRepo.findById(id);
  }

  public RoomService(RoomSetRepository roomRepo) {
    this.roomRepo = roomRepo;
  }
}
