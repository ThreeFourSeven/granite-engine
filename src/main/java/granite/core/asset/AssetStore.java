package granite.core.asset;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.UUID;

public class AssetStore {

  private static final ObjectMapper mapper = new ObjectMapper();

  protected final HashMap<UUID, Asset> assets = new HashMap<>();

  private void save() {
    for(Asset asset : assets.values()) {
      String path = asset.location.value + ".asset";
      AssetLocation location = Asset.systemLocation(path);
      Asset.saveJson(location, mapper.valueToTree(asset));
    }
  }

  public void register(Asset asset) {
    assets.put(asset.id, asset);
    save();
  }

}
