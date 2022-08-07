package ru.tumasoff.codetogether.models;

public class Message {
  private final String roomId;
  private final String username;
  private final String text;

  public Message(String type, String roomId, String username, String text) {
    this.roomId = roomId;
    this.username = username;
    this.text = text;
  }
  public String getRoomId() {
    return roomId;
  }

  public String getUsername() {
    return username;
  }

  public String getText() {
    return text;
  }
}
