package granite;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import granite.core.asset.Asset;
import granite.core.asset.AssetStore;
import granite.core.asset.JsonAsset;
import granite.core.logger.Logger;

public class Granite {

  private static final Logger logger = new Logger(Granite.class);

  public static volatile GraniteConfig config = new GraniteConfig();
  public static volatile GraniteRuntime runtime = new GraniteRuntime();

  public static void loadConfig() {
    JsonAsset ja = Asset.loadJson(Asset.jarLocation("granite.json"));
    JsonNode json = ja.getValue();
    try {
      GraniteConfig c = new ObjectMapper().treeToValue(json, GraniteConfig.class);
      if(c != null)
        config = c;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getLocalizedMessage());
    }
  }

  public static void main(String[] args) {
    Granite.runtime.run();
  }

}
