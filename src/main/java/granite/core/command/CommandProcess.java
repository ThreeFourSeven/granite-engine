package granite.core.command;

@FunctionalInterface
public interface CommandProcess {
  void execute(Object... args);
}
