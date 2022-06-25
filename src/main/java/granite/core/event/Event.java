package granite.core.event;

import lombok.Getter;
import granite.Granite;

import java.time.Instant;

@Getter
public abstract class Event {

  protected final EventType type;
  protected long timestampMs;

  protected Event(EventType type) {
    this.type = type;
    this.timestampMs = Instant.now().toEpochMilli();
    Granite.runtime.clientRuntime.events.capture(this);
  }

  @Override
  public String toString() {
    return "Event{" +
      "type=" + type +
      ", timestampMs=" + timestampMs +
      '}';
  }
}
