package granite.core.command;

import granite.core.data.Pair;
import granite.core.event.EventType;
import granite.core.event.key.KeyEvent;
import granite.core.event.key.KeyEventType;
import granite.core.event.listener.EventListener;
import granite.core.event.mouse.MouseButtonEvent;
import granite.core.event.mouse.MouseButtonEventType;
import granite.core.logger.Logger;
import lombok.Getter;

import java.util.*;
import java.util.function.Supplier;

@Getter
public class Command {

  private static final Logger logger = new Logger(Command.class);

  protected final CommandProcess process;
  protected final String name;
  protected final HashSet<String> aliases = new HashSet<>();
  protected final HashMap<String, Object[]> keyBindings = new HashMap<>();
  protected final HashMap<String, Object[]> mouseButtonBindings = new HashMap<>();
  protected final List<Pair<Supplier<Boolean>, Object[]>> predicateBindings = new ArrayList<>();

  protected static final List<Command> commands = new ArrayList<>();

  protected Command(CommandProcess process, String name) {
    this.process = process;
    this.name = name;
    addAlias(name);
  }

  public Command addAlias(String alias) {
    aliases.add(alias);
    return this;
  }

  public Command addAliases(String... aliases) {
    for(String alias : aliases)
      addAlias(alias);
    return this;
  }

  public Command addKeyBinding(String key, Object... args) {
    keyBindings.put(key, args);
    return this;
  }

  public Command addMouseButtonBinding(String button, Object... args) {
    mouseButtonBindings.put(button, args);
    return this;
  }

  public Command addPredicateBinding(Supplier<Boolean> predicate, Object... args) {
    predicateBindings.add(Pair.from(predicate, args));
    return this;
  }

  public static void createEventListeners() {
    new EventListener(EventType.Key, e -> {
      KeyEvent event = (KeyEvent)e;
      if(event.type == KeyEventType.Clicked) {
        for (Command command : commands) {
          if (command.keyBindings.containsKey(event.key)) {
            Object[] args = command.keyBindings.get(event.key);
            logger.debugf("Executing command %s with args %s", command.name, Arrays.toString(args));
            command.process.execute(args);
          }
        }
      }
    });

    new EventListener(EventType.MouseButton, e -> {
      MouseButtonEvent event = (MouseButtonEvent)e;
      if(event.type == MouseButtonEventType.Clicked) {
        for (Command command : commands) {
          if (command.mouseButtonBindings.containsKey(event.button)) {
            Object[] args = command.mouseButtonBindings.get(event.button);
            logger.debugf("Executing command %s with args %s", command.name, Arrays.toString(args));
            command.process.execute(args);
          }
        }
      }
    });
  }

  public static Command addCommand(String name, CommandProcess process) {
    Command command = new Command(process, name);
    commands.add(command);
    return command;
  }

  public static void loop() {
    for(Command command : commands) {
      for(Pair<Supplier<Boolean>, Object[]> pair : command.predicateBindings) {
        if(pair.first.get()) {
          logger.debugf("Executing command %s with args %s", command.name, Arrays.toString(pair.second));
          command.process.execute(pair.second);
        }
      }
    }
  }

  public static void execute(String commandName, Object... args) {
    for(Command command : commands) {
      if(command.aliases.contains(commandName)) {
        logger.debugf("Executing command %s with args %s", command.name, Arrays.toString(args));
        command.process.execute(args);
      }
    }
  }

}
