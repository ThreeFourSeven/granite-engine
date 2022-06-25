package granite.core.logger;

import granite.Granite;
import granite.core.data.Pair;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Logger {

  public static final HashSet<LogOutputs> outputs = new HashSet<>(List.of(LogOutputs.values()));
  public static volatile List<Pair<String, LogLevel>> logs = new ArrayList<>();

  protected final String className;

  public <T> Logger(Class<T> classType) {
    this.className = classType.getCanonicalName();
  }

  protected Logger logMessage(String message, LogLevel level) {
    if(outputs.contains(LogOutputs.System)) {
      if(level == LogLevel.Error) {
        System.err.println(message);
      } else {
        System.out.println(message);
      }
    }

    logs.add(Pair.from(message + '\n', level));

    return this;
  }

  protected Logger log(String message, LogLevel level) {
    String msg;
    if(Granite.config.debug) {
      String t = Timestamp.from(Instant.now()).toLocalDateTime().toString();
      msg = String.format("%s - %s [%s] %s", t, className, level.toString(), message);
    } else {
      msg = String.format("%s [%s] %s", className, level.toString(), message);
    }
    return logMessage(msg, level);
  }

  public Logger info(String message) {
    return log(message, LogLevel.Info);
  }

  public Logger debug(String message) {
    if(!Granite.config.debug) return this;
    return log(message, LogLevel.Debug);
  }

  public Logger warning(String message) {
    return log(message, LogLevel.Warning);
  }

  public Logger error(String message) {
    return log(message, LogLevel.Error);
  }

  public Logger infof(String message, Object... args) {
    return info(String.format(message, args));
  }

  public Logger debugf(String message, Object... args) {
    return debug(String.format(message, args));
  }

  public Logger warningf(String message, Object... args) {
    return warning(String.format(message, args));
  }

  public Logger errorf(String message, Object... args) {
    return error(String.format(message, args));
  }

  public static void enableOutput(LogOutputs output) {
    outputs.add(output);
  }

  public static void disableOutput(LogOutputs output) {
    outputs.remove(output);
  }

  public static String getLogFileConents() {
    return logs.stream().map(pair -> pair.first).collect(Collectors.joining());
  }

  public static String getInfoLogFileConents() {
    return logs.stream().filter(pair -> pair.second == LogLevel.Info)
      .map(pair -> pair.first).collect(Collectors.joining());
  }

  public static String getDebugLogFileConents() {
    return logs.stream().filter(pair -> pair.second == LogLevel.Debug)
      .map(pair -> pair.first).collect(Collectors.joining());
  }

  public static String getWarningLogFileConents() {
    return logs.stream().filter(pair -> pair.second == LogLevel.Warning)
      .map(pair -> pair.first).collect(Collectors.joining());
  }

  public static String getErrorLogFileConents() {
    return logs.stream().filter(pair -> pair.second == LogLevel.Error)
      .map(pair -> pair.first).collect(Collectors.joining());
  }

}
