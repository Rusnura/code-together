package ru.tumasoff.codetogether.models;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class OutputMessage {
  private String from;
  private String text;

  private String time;

  public OutputMessage(Message message) {
    this.from = message.getFrom();
    this.text = message.getText();
    time = new SimpleDateFormat("HH:mm").format(new Date());
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
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
