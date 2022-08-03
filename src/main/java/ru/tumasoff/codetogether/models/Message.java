package ru.tumasoff.codetogether.models;

public class Message {
  private final String type;
  private final String roomId;
  private final String from;
  private final String text;

  public Message(String type, String roomId, String from, String text) {
    this.type = type;
    this.roomId = roomId;
    this.from = from;
    this.text = text;
  }

  public String getType() {
    return type;
  }

  public String getRoomId() {
    return roomId;
  }

  public String getFrom() {
    return from;
  }

  public String getText() {
    return text;
  }
}
