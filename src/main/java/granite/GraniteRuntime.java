package granite;

import granite.audio.AudioRuntime;
import granite.client.ClientRuntime;
import granite.core.command.Command;
import lombok.Getter;
import granite.core.asset.Asset;
import granite.core.logger.Logger;

public class GraniteRuntime {

  private final Logger logger = new Logger(GraniteRuntime.class);

  public final AudioRuntime audioRuntime = new AudioRuntime();
  public final ClientRuntime clientRuntime = new ClientRuntime();

  private final Thread audioThread = new Thread(audioRuntime);
  private final Thread clientThread = new Thread(clientRuntime);

  @Getter
  private volatile boolean running = false;

  protected void initialize() {
    Granite.loadConfig();
    Command.createEventListeners();
  }

  public void run() {
    logger.debug("Starting granite runtime");
    initialize();
    running = true;
    audioThread.start();
    clientThread.start();
    logger.debug("Started granite runtime");

    while (running) {
      Thread.onSpinWait();
    }

    try {
      audioThread.join();
      clientThread.join();
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getLocalizedMessage());
    }

    Asset.saveText(Asset.systemLocation("data/log/log.txt"), Logger.getLogFileConents());
    Asset.saveText(Asset.systemLocation("data/log/info.txt"), Logger.getInfoLogFileConents());
    Asset.saveText(Asset.systemLocation("data/log/debug.txt"), Logger.getDebugLogFileConents());
    Asset.saveText(Asset.systemLocation("data/log/warning.txt"), Logger.getWarningLogFileConents());
    Asset.saveText(Asset.systemLocation("data/log/error.txt"), Logger.getErrorLogFileConents());
    logger.debug("Stopped granite runtime");
  }

  public void stop() {
    running = false;
  }

}
