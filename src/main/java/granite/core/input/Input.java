package granite.core.input;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import granite.core.asset.Asset;
import granite.core.asset.JsonAsset;
import granite.core.graphics.Window;
import granite.core.input.mouse.Mouse;
import granite.core.input.key.Key;
import granite.core.logger.Logger;

public class Input {

  private static final Logger logger = new Logger(Input.class);

  public final Key key = new Key();
  public final Mouse mouse = new Mouse();

  public void loop() {
    key.loop();
    mouse.loop();
  }

  public void bindCallbacks(Window window) {
    key.bindCallbacks(window);
    mouse.bindCallbacks(window);
  }

  public void loadCodes() {
    JsonAsset ja = Asset.loadJson(Asset.jarLocation("input_codes.json"));
    JsonNode json = ja.getValue();
    try {
      InputCodes codes = new ObjectMapper().treeToValue(json, InputCodes.class);
      if(codes != null) {
        key.setCodes(codes);
        mouse.setCodes(codes);
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getLocalizedMessage());
    }
  }

}
