package granite.core.event.key;

import granite.core.event.Event;
import granite.core.event.EventType;
import lombok.ToString;

@ToString
public class KeyEvent extends Event {

  public final String key;
  public final int code;
  public final KeyEventType type;
  public final long downTime;

  public KeyEvent(String key, int code, KeyEventType type, long downTime) {
    super(EventType.Key);
    this.downTime = downTime;
    this.code = code;
    this.key = key;
    this.type = type;
  }

}
