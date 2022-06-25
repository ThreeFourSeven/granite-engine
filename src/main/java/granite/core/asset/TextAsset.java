package granite.core.asset;

import granite.core.logger.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TextAsset extends Asset {

  protected static final Logger logger = new Logger(TextAsset.class);

  public TextAsset(AssetLocation location) {
    super(AssetType.Text, location);
  }

  public TextAsset() {
  }

  @Override
  public String getValue() {
    return super.getValue().toString();
  }

  @Override
  protected String read(InputStream stream) {
    try {
      byte[] bytes = stream.readAllBytes();
      return new String(bytes);
    } catch (Exception e) {
      logger.warningf("Read failed with asset %s from uri %s", id.toString(), location.toString());
      return null;
    }
  }

  @Override
  protected void write(OutputStream stream) {
    byte[] bytes = value.toString().getBytes(StandardCharsets.UTF_8);
    try {
      stream.write(bytes);
    } catch (Exception e) {
      logger.warningf(e.getLocalizedMessage());
    }
  }

}
