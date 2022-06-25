package granite.core.asset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import granite.core.logger.Logger;
import granite.Granite;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class JsonAsset extends Asset {

  protected static final ObjectMapper mapper = new ObjectMapper();
  protected static final Logger logger = new Logger(JsonAsset.class);

  public JsonAsset(AssetLocation location) {
    super(AssetType.Json, location);
  }

  public JsonAsset() {
  }

  @Override
  public JsonNode getValue() {
    return (JsonNode)super.getValue();
  }

  @Override
  protected JsonNode read(InputStream stream) {
    try {
      byte[] bytes = stream.readAllBytes();
      return mapper.readTree(bytes);
    } catch (Exception e) {
      logger.warningf("Read failed with asset %s from uri %s", id.toString(), location.toString());
      return null;
    }
  }

  @Override
  protected void write(OutputStream stream) {
    String json = Granite.config.debug ? ((JsonNode)value).toPrettyString() : value.toString();
    byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
    try {
      stream.write(bytes);
    } catch (Exception e) {
      logger.warningf(e.getLocalizedMessage());
    }
  }

}
