package granite.core.input.mouse;

import granite.core.event.mouse.MouseButtonEventType;
import granite.core.event.mouse.MouseMotionEvent;
import granite.core.event.mouse.MouseScrollEvent;
import org.lwjgl.glfw.GLFW;
import granite.core.event.mouse.MouseButtonEvent;
import granite.core.graphics.Window;
import granite.core.input.InputCodes;
import granite.core.logger.Logger;

import java.time.Instant;
import java.util.HashMap;

public class Mouse {
  
  private static final Logger logger = new Logger(Mouse.class);

  private final HashMap<String, Integer> codes = new HashMap<>();
  private final HashMap<Integer, Boolean> wasDown = new HashMap<>();
  private final HashMap<Integer, Boolean> isDown = new HashMap<>();
  private final HashMap<Integer, Long> downTimestamp = new HashMap<>();
  private final HashMap<Integer, Long> downDuration = new HashMap<>();
  private double x, y, px, py, dx, dy;
  private double scrollX, scrollY;

  public int getCode(String button) {
    return codes.getOrDefault(button, -1);
  }

  public boolean isDown(String button) {
    return isDown.getOrDefault(getCode(button), false);
  }

  public boolean wasDown(String button) {
    return wasDown.getOrDefault(getCode(button), false);
  }

  public boolean isClicked(String button) {
    return isDown(button) && !wasDown(button);
  }

  public boolean isReleased(String button) {
    return !isDown(button) && wasDown(button);
  }

  public long getDownTimeMs(String button) {
    return downDuration.getOrDefault(getCode(button), 0L);
  }

  public float getX() {
    return (float)x;
  }

  public float getY() {
    return (float)y;
  }

  public float getDX() {
    return (float)dx;
  }

  public float getDY() {
    return (float)dy;
  }

  public float getScrollX() {
    return (float)scrollX;
  }

  public float getScrollY() {
    return (float)scrollY;
  }

  public void loop() {
    for(String key : codes.keySet()) {
      int code = getCode(key);
      boolean isDown = isDown(key);
      boolean isReleased = isReleased(key);
      boolean isClicked = isClicked(key);

      if(isClicked)
        new MouseButtonEvent(key, code, MouseButtonEventType.Clicked, getDownTimeMs(key));

      if(isDown) {
        long now = Instant.now().toEpochMilli();
        if(!downTimestamp.containsKey(code))
          downTimestamp.put(code, now);

        long timestamp = downTimestamp.get(code);
        downDuration.put(code, now - timestamp);
        new MouseButtonEvent(key, code, MouseButtonEventType.Down, getDownTimeMs(key));
      }

      if(isReleased) {
        new MouseButtonEvent(key, code, MouseButtonEventType.Released, getDownTimeMs(key));
        downTimestamp.remove(code);
        downDuration.put(code, 0L);
      }

      if(Math.abs(dx) > 0 || Math.abs(dy) > 0)
        new MouseMotionEvent(getX(), getY(), getDX(), getDY());

      if(Math.abs(scrollX) > 0 || Math.abs(scrollY) > 0)
        new MouseScrollEvent(getX(), getY(), getScrollX(), getScrollY());

      wasDown.put(code, isDown);
      dx = 0;
      dy = 0;
      scrollX = 0;
      scrollY = 0;
    }
  }

  public void bindCallbacks(Window window) {

    GLFW.glfwSetMouseButtonCallback(window.getId(), (wid, button, action, mods) ->
      isDown.put(button, action == GLFW.GLFW_PRESS)
    );

    GLFW.glfwSetCursorPosCallback(window.getId(), (wid, nx, ny) -> {
      px = x;
      py = y;
      x = nx;
      y = ny;
      dx = x - px;
      dy = y - py;
    });

    GLFW.glfwSetScrollCallback(window.getId(), (wid, sx, sy) -> {
      scrollX = sx;
      scrollY = sy;
    });
  }

  public void setCodes(InputCodes inputCodes) {
    codes.clear();
    codes.putAll(inputCodes.mouse);
  }
}
