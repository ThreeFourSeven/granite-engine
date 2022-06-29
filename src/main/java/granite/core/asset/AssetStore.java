package granite.core.asset;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.UUID;

public class AssetStore {

  protected final HashMap<UUID, Asset> assets = new HashMap<>();

  public AssetReference register(Asset asset) {
    assets.put(asset.id, asset);
    return new AssetReference(this, asset.id);
  }

}
