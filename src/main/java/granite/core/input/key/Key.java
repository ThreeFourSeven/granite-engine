package granite.core.input.key;

import granite.core.event.key.KeyEvent;
import granite.core.event.key.KeyEventType;
import granite.core.graphics.Window;
import granite.core.input.InputCodes;
import granite.core.logger.Logger;
import org.lwjgl.glfw.GLFW;

import java.time.Instant;
import java.util.HashMap;

public class Key {

  private static final Logger logger = new Logger(Key.class);

  private final HashMap<String, Integer> codes = new HashMap<>();
  private final HashMap<Integer, Boolean> wasDown = new HashMap<>();
  private final HashMap<Integer, Boolean> isDown = new HashMap<>();
  private final HashMap<Integer, Long> downTimestamp = new HashMap<>();
  private final HashMap<Integer, Long> downDuration = new HashMap<>();

  public int getCode(String key) {
    return codes.getOrDefault(key, -1);
  }

  public boolean isDown(String key) {
    return isDown.getOrDefault(getCode(key), false);
  }

  public boolean wasDown(String key) {
    return wasDown.getOrDefault(getCode(key), false);
  }

  public boolean isClicked(String key) {
    return isDown(key) && !wasDown(key);
  }

  public boolean isReleased(String key) {
    return !isDown(key) && wasDown(key);
  }

  public long getDownTimeMs(String key) {
    return downDuration.getOrDefault(getCode(key), 0L);
  }

  public void loop() {
    for(String key : codes.keySet()) {
      int code = getCode(key);
      boolean isDown = isDown(key);
      boolean isReleased = isReleased(key);
      boolean isClicked = isClicked(key);

      if(isClicked)
        new KeyEvent(key, code, KeyEventType.Clicked, getDownTimeMs(key));

      if(isDown) {
        long now = Instant.now().toEpochMilli();
        if(!downTimestamp.containsKey(code))
          downTimestamp.put(code, now);

        long timestamp = downTimestamp.get(code);
        downDuration.put(code, now - timestamp);
        new KeyEvent(key, code, KeyEventType.Down, getDownTimeMs(key));
      }

      if(isReleased) {
        new KeyEvent(key, code, KeyEventType.Released, getDownTimeMs(key));
        downTimestamp.remove(code);
        downDuration.put(code, 0L);
      }

      wasDown.put(code, isDown);
    }
  }

  public void bindCallbacks(Window window) {
    GLFW.glfwSetKeyCallback(window.getId(), (wid, key, scanCode, action, mods) -> {
      isDown.put(key, action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT);
    });
  }

  public void setCodes(InputCodes inputCodes) {
    codes.clear();
    codes.putAll(inputCodes.key);
  }

}
