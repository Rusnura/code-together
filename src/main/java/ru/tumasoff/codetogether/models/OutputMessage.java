package ru.tumasoff.codetogether.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OutputMessage {
  String type;
  private String roomId;
  private String username;
  private int startCursorPosition;
  private int endCursorPosition;
  private String keyType;
  private String key;
  private String time;

  public OutputMessage() {
  }

  public OutputMessage(Message message) {
    this.type = "TYPE";
    this.roomId = message.getRoomId();
    this.username = message.getUsername();
    this.startCursorPosition = message.getStartCursorPosition();
    this.endCursorPosition = message.getEndCursorPosition();
    this.key = message.getKey();
    this.keyType = message.getKeyType();
    time = new SimpleDateFormat("HH:mm").format(new Date());
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getStartCursorPosition() {
    return startCursorPosition;
  }

  public void setStartCursorPosition(int startCursorPosition) {
    this.startCursorPosition = startCursorPosition;
  }

  public int getEndCursorPosition() {
    return endCursorPosition;
  }

  public void setEndCursorPosition(int endCursorPosition) {
    this.endCursorPosition = endCursorPosition;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getTime() {
    return time;
  }

  public void setTime(Date date) {
    this.time = new SimpleDateFormat("HH:mm").format(date);
  }

  public String getKeyType() {
    return keyType;
  }

  public void setKeyType(String keyType) {
    this.keyType = keyType;
  }
}
