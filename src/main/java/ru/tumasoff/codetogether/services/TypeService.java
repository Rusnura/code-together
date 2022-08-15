package ru.tumasoff.codetogether.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import ru.tumasoff.codetogether.models.Client;
import ru.tumasoff.codetogether.models.Message;
import ru.tumasoff.codetogether.models.Room;

import java.util.List;

@Service
public class TypeService {
  private static final List<String> NAVIGATION_KEYS = List.of(new String[] {
    "MouseClick","ArrowUp","ArrowDown","ArrowRight","ArrowLeft","Home","End"
  });

  private static final String ENTER_KEY = "Enter";
  private static final String BACKSPACE_KEY = "Backspace";
  private static final String DELETE_KEY = "Delete";


  public void process(Room room, Client client, String pressedKey, /* FIXME: Refactor later */Message message, ObjectNode response) {
    if (pressedKey == null)
      pressedKey = "MouseClick";

    if (pressedKey.length() == 1) { // is char pressed?
      doProcessInsert(room, client, pressedKey, response);
    }

    if (BACKSPACE_KEY.equals(pressedKey)) {
      doProcessBackspace(room, message, client, response);
      return;
    }

    if (ENTER_KEY.equals(pressedKey)) {
      doProcessInsert(room, client, "\n", response);
      return;
    }

    if (NAVIGATION_KEYS.contains(pressedKey)) {
      doProcessNavigation(client, message, response);
      return;
    }

    if (DELETE_KEY.equals(pressedKey)) {
      doProcessDelete(room, client, response);
      return;
    }
  }

  private void doProcessInsert(Room room, Client client, String key, ObjectNode response) {
    response.put("type", "insert");
    response.put("key", key);
    int start = client.getSelectionStartPosition();
    int end = client.getSelectionEndPosition();
    String before = room.getText().substring(0, start);
    String after = room.getText().substring(end);
    String text = before + key + after;
    room.setText(text);
    response.put("text", text);
    // client.setSelectionStartPosition(message.getStartCursorPosition());
    // client.setSelectionEndPosition(message.getEndCursorPosition());
  }

  private void doProcessBackspace(Room room, Message message, Client client, ObjectNode response) {
    response.put("type", "backspace");
    int start = client.getSelectionStartPosition();
    int end = client.getSelectionEndPosition();

    int newCursorStartPosition, newCursorEndPosition;
    String before, after, text;
    if (start == end) { // Remove one symbol
      if (start == 0)
        return;
      before = room.getText().substring(0, start - 1);
      newCursorStartPosition = start - 1;
      newCursorEndPosition = start - 1;
    } else {
      before = room.getText().substring(0, start);
      newCursorStartPosition = start;
      newCursorEndPosition = start;
    }
    after = room.getText().substring(end);
    text = before + after;
    room.setText(text);
    response.put("text", text);

    client.setSelectionStartPosition(newCursorStartPosition);
    client.setSelectionEndPosition(newCursorEndPosition);
  }

  private void doProcessDelete(Room room, Client client, ObjectNode response) {
    response.put("type", "delete");
    int start = client.getSelectionStartPosition();
    int end = client.getSelectionEndPosition();

    int newCursorStartPosition, newCursorEndPosition;
    String before, after, text;
    if (start == end) { // Remove one symbol
      if (room.getText().length() < end + 1)
        return;

      before = room.getText().substring(0, start);
      after = room.getText().substring(end + 1);
    } else {
      before = room.getText().substring(0, start);
      after = room.getText().substring(end);
    }
    newCursorStartPosition = start;
    newCursorEndPosition = start;

    text = before + after;
    room.setText(text);
    response.put("text", text);

    client.setSelectionStartPosition(newCursorStartPosition);
    client.setSelectionEndPosition(newCursorEndPosition);
  }
  private void doProcessNavigation(Client client, Message message, ObjectNode response) {
    response.put("type", "navigation");
    response.put("startCursorPosition", message.getStartCursorPosition());
    response.put("endCursorPosition", message.getEndCursorPosition());
    client.setSelectionStartPosition(message.getStartCursorPosition());
    client.setSelectionEndPosition(message.getEndCursorPosition());
  }
}
