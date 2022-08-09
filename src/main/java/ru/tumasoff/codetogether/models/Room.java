package ru.tumasoff.codetogether.models;

import java.util.Objects;
import java.util.UUID;

public class Room {
  private UUID id;

  public Room(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Room))
      return false;

    return Objects.equals(id, ((Room) obj).id);
  }
}
