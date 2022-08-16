package ru.tumasoff.codetogether.models;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Room {
  private final String id;
  private final Map<String, Client> clients = new ConcurrentHashMap<>();
  private StringBuffer textBuffer = new StringBuffer("");

  public Room(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public Map<String, Client> getClients() {
    return clients;
  }

  public StringBuffer getTextBuffer() {
    return textBuffer;
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
