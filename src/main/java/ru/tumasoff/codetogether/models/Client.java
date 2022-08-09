package ru.tumasoff.codetogether.models;

import org.springframework.util.ObjectUtils;

import java.util.Objects;

public class Client {
  private final String username;

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

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Client))
      return false;

    return Objects.equals(username, ((Client) obj).username);
  }
}
