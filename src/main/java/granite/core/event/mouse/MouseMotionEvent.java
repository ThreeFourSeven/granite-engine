package granite.core.event.mouse;

import granite.core.event.Event;
import granite.core.event.EventType;
import lombok.ToString;

@ToString
public class MouseMotionEvent extends Event {

  public final float x;
  public final float y;
  public final float dx;
  public final float dy;

  public MouseMotionEvent(float x, float y, float dx, float dy) {
    super(EventType.MouseMotion);
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
  }

}
