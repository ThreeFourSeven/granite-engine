package granite.core.event.listener;

import granite.core.event.Event;
import granite.core.event.EventType;
import granite.core.logger.Logger;
import lombok.Getter;
import granite.Granite;

import java.util.UUID;
import java.util.function.Consumer;

@Getter
public class EventListener {

  private static final Logger logger = new Logger(EventListener.class);

  private final UUID id;
  private final EventType type;
  private final Consumer<Event> consumer;

  public EventListener(EventType type, Consumer<Event> consumer, boolean register) {
    this.type = type;
    this.consumer = consumer;
    this.id = UUID.randomUUID();
    if(register)
      register();
  }

  public EventListener(EventType type, Consumer<Event> consumer) {
    this(type, consumer, true);
  }

  private void consume(Event event) {
    if(event.getType() == type) {
      logger.debugf("Event listener %s consuming event %s", id.toString(), event.toString());
      consumer.accept(event);
    }
  }

  public void register() {
    logger.debugf("Registering event listener %s for events of type %s", id.toString(), type.toString());
    Granite.runtime.clientRuntime.events.addConsumer(id.toString(), this::consume);
  }

  public void remove() {
    logger.debugf("Removing event listener %s for events of type %s", id.toString(), type.toString());
    Granite.runtime.clientRuntime.events.removeConsumer(id.toString());
  }

}
