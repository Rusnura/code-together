package ru.tumasoff.codetogether.models;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class OutputMessage {
  private String username;
  private String text;

  private String time;

  public OutputMessage(Message message) {
    this.username = message.getUsername();
    this.text = message.getText();
    time = new SimpleDateFormat("HH:mm").format(new Date());
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getTime() {
    return time;
  }

  public void setTime(LocalDateTime time) {
    this.time = new SimpleDateFormat("HH:mm").format(time);
  }
}
