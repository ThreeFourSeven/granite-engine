package granite.core.event;

import granite.core.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Events {

  private static final Logger logger = new Logger(Events.class);

  private final HashMap<String, Consumer<Event>> consumers = new HashMap<>();
  private final List<Event> unprocessedEvents = new ArrayList<>();
  private final HashMap<EventType, List<Event>> events = new HashMap<>();


  protected void capture(Event event) {
    logger.debugf("Capturing event %s", event.type.toString());
    unprocessedEvents.add(event);
  }

  protected void processEvent(Event event) {
    logger.debugf("Processing event %s", event.toString());
    consumers.values().forEach(consumer -> consumer.accept(event));
    if(!events.containsKey(event.type))
      events.put(event.type, new ArrayList<>());
    events.get(event.type).add(event);
  }

  public void addConsumer(String key, Consumer<Event> consumer) {
    consumers.put(key, consumer);
  }

  public void addConsumer(Consumer<Event> consumer) {
    addConsumer(UUID.randomUUID().toString(), consumer);
  }

  public void removeConsumer(String key) {
    consumers.remove(key);
  }

  public void clearConsumers() {
    consumers.clear();
  }

  public void loop() {
    events.clear();
    unprocessedEvents.forEach(this::processEvent);
    unprocessedEvents.clear();
  }

}
