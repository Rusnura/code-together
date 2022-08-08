package ru.tumasoff.codetogether.models;

public class Message {
  private final String roomId;
  private final String username;
  private final int startCursorPosition;
  private final int endCursorPosition;
  private final String key;

  public Message(String roomId,
                 String username,
                 int startCursorPosition,
                 int endCursorPosition,
                 String key) {
    this.roomId = roomId;
    this.username = username;
    this.startCursorPosition = startCursorPosition;
    this.endCursorPosition = endCursorPosition;
    this.key = key;
  }
  public String getRoomId() {
    return roomId;
  }

  public String getUsername() {
    return username;
  }

  public int getStartCursorPosition() {
    return startCursorPosition;
  }

  public int getEndCursorPosition() {
    return endCursorPosition;
  }

  public String getKey() {
    return key;
  }
}
