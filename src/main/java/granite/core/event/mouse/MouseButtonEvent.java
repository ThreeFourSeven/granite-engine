package granite.core.event.mouse;

import granite.core.event.Event;
import granite.core.event.EventType;
import lombok.ToString;

@ToString
public class MouseButtonEvent extends Event {

  public final String button;
  public final int code;
  public final MouseButtonEventType type;
  public final long downTime;

  public MouseButtonEvent(String button, int code, MouseButtonEventType type, long downTime) {
    super(EventType.MouseButton);
    this.button = button;
    this.code = code;
    this.type = type;
    this.downTime = downTime;
  }

}
