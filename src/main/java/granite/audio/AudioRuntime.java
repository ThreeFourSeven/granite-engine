package granite.audio;

import granite.core.logger.Logger;
import granite.Granite;

public class AudioRuntime implements Runnable {

  private final Logger logger = new Logger(AudioRuntime.class);

  @Override
  public void run() {
    logger.debug("Starting audio runtime");
    initialize();
    logger.debug("Started audio runtime");
    while(Granite.runtime.isRunning()) {
      loop();
    }
    destroy();
    logger.debug("Stopped audio runtime");
  }

  private void initialize() {

  }

  private void loop() {

  }

  private void destroy() {

  }

}
