package granite.core.event.mouse;

import lombok.ToString;
import granite.core.event.Event;
import granite.core.event.EventType;

@ToString
public class MouseScrollEvent extends Event {

  public final float x;
  public final float y;
  public final float scrollX;
  public final float scrollY;

  public MouseScrollEvent(float x, float y, float scrollX, float scrollY) {
    super(EventType.MouseScroll);
    this.x = x;
    this.y = y;
    this.scrollX = scrollX;
    this.scrollY = scrollY;
  }
}
