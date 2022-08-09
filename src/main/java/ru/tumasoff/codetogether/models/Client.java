package ru.tumasoff.codetogether.models;

import java.util.Objects;

public class Client {
  private final String username;
  private int selectionStartPosition = 0;
  private int selectionEndPosition = 0;

  public Client(String username) {
    Objects.requireNonNull(username, "Session id cannot be null!");
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  @Override
  public int hashCode() {
    return username.hashCode();
  }

  public int getSelectionStartPosition() {
    return selectionStartPosition;
  }

  public void setSelectionStartPosition(int selectionStartPosition) {
    this.selectionStartPosition = selectionStartPosition;
  }

  public int getSelectionEndPosition() {
    return selectionEndPosition;
  }

  public void setSelectionEndPosition(int selectionEndPosition) {
    this.selectionEndPosition = selectionEndPosition;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Client))
      return false;

    return Objects.equals(username, ((Client) obj).username);
  }
}
