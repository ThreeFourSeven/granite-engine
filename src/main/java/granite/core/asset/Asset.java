package granite.core.asset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import granite.Granite;
import lombok.Getter;
import lombok.Setter;
import granite.core.logger.Logger;
import org.lwjgl.assimp.AIScene;

import java.io.*;
import java.util.UUID;
import java.util.function.Supplier;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = AISceneAsset.class, name = "Scene"),
  @JsonSubTypes.Type(value = JsonAsset.class, name = "Json"),
  @JsonSubTypes.Type(value = TextAsset.class, name = "Text"),
})
@Getter
public abstract class Asset {

  @JsonIgnore
  protected static final Logger logger = new Logger(Asset.class);

  protected final UUID id;
  protected final AssetType type;

  @Setter
  protected AssetLocation location;

  @JsonIgnore
  @Setter
  protected Object value = null;

  public Asset(AssetType type, AssetLocation location) {
    this.type = type;
    id = UUID.randomUUID();
    this.location = location;
  }

  public Asset() {
    this(null, null);
  }

  public void load() {
    logger.debugf("Loading asset %s from %s", id.toString(), location.toString());

    if(location != null) {
      try {
        InputStream is = location.getInputStream();
        if(is != null)
          value = read(is);
        if(value == null)
          logger.warningf("Failed to load asset %s read null value from uri %s", id.toString(), location.toString());
      } catch (Exception e) {
        logger.warning(e.getLocalizedMessage());
      }
    } else {
      logger.warningf("Asset %s does not have a uri", id.toString());
    }

    logger.debugf("Loaded asset %s from %s", id.toString(), location.toString());
  }

  public void save() {
    logger.debugf("Saving asset %s from %s", id.toString(), location.toString());

    if(value == null) {
      logger.warningf("Failed to save asset %s value is null", id.toString());
    } else if(location != null) {
      try {
        OutputStream os = location.getOutputStream();
        if(os != null)
          write(os);
      } catch (Exception e) {
        logger.warningf("Failed to save asset %s uri is invalid %s", id.toString(), location.toString());
      }
    } else {
      logger.warningf("Save %s does not have a uri", id.toString());
    }

    logger.debugf("Saved asset %s to %s", id.toString(), location.toString());
  }

  protected abstract Object read(InputStream stream);
  protected abstract void write(OutputStream stream);

  public static AssetLocation jarLocation(String path) {
    return new AssetLocation(path, AssetLocationType.Jar);
  }

  public static AssetLocation networkLocation(String url) {
    return new AssetLocation(url, AssetLocationType.Network);
  }

  public static AssetLocation systemLocation(String path) {
    return new AssetLocation(path, AssetLocationType.System);
  }


  public static <V extends Asset> V load(Supplier<V> constructor, AssetLocation location) {
    V asset = constructor.get();
    asset.setLocation(location);
    asset.load();
    return asset;
  }

  public static TextAsset loadText(AssetLocation location) {
    return load(TextAsset::new, location);
  }

  public static String loadString(AssetLocation location) {
    return loadText(location).getValue().toString();
  }

  public static JsonAsset loadJson(AssetLocation location) {
    return load(JsonAsset::new, location);
  }

  public static AISceneAsset loadScene(AssetLocation location) {
    return load(AISceneAsset::new, location);
  }

  public static <T> T loadObject(AssetLocation location, T defaultValue, Class<T> tClass) {
    T value = defaultValue;
    JsonAsset js = loadJson(location);
    JsonNode json = js.getValue();
    if(json != null) {
      try {
        T v = new ObjectMapper().treeToValue(json, tClass);
        if(v != null)
          value = v;
      } catch (Exception e) {
        e.printStackTrace();
        logger.warning(e.getLocalizedMessage());
        logger.warningf("Failed to convert JsonNode %s to object %s", json.toString(), tClass.getCanonicalName());
      }
    }
    try {

      JsonNode j = new ObjectMapper().valueToTree(value);
      if(j != null)
        json = j;
    } catch (Exception e) {
      e.printStackTrace();
      logger.warning(e.getLocalizedMessage());
      logger.warningf("Failed to convert %s to a JsonNode", value.toString());
    }
    return value;
  }

  public static <V extends Asset> V save(Supplier<V> constructor, AssetLocation location, Object value) {
    V asset = constructor.get();
    asset.setLocation(location);
    asset.setValue(value);
    asset.save();
    return asset;
  }

  public static TextAsset saveText(AssetLocation location, String text) {
    return save(TextAsset::new, location, text);
  }

  public static JsonAsset saveJson(AssetLocation location, JsonNode value) {
    return save(JsonAsset::new, location, value);
  }

  public static <T> JsonAsset saveObject(AssetLocation location, T value) {
    JsonNode json = JsonNodeFactory.instance.objectNode();
    try {
      JsonNode j = new ObjectMapper().valueToTree(value);
      if(j != null)
        json = j;
    } catch (Exception e) {
      e.printStackTrace();
      logger.warning(e.getLocalizedMessage());
      logger.warningf("Failed to convert %s to a JsonNode", value.toString());
    }
    return saveJson(location, json);
  }

  public static AISceneAsset saveScene(AssetLocation location, AIScene value) {
    return save(AISceneAsset::new, location, value);
  }

}
