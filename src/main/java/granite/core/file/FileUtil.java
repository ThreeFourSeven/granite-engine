package granite.core.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import granite.core.asset.Asset;
import granite.core.asset.AssetLocation;
import granite.core.asset.JsonAsset;
import granite.core.logger.Logger;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

  private static final Logger logger = new Logger(FileUtil.class);
  private static final ObjectMapper mapper = new ObjectMapper();

  public static void createDirectory(String path) {
    logger.debugf("Creating directory at %s", path);
    try {
      Files.createDirectory(Path.of(path));
    } catch (Exception e) {
      if (!(e instanceof FileAlreadyExistsException)) {
        e.printStackTrace();
        logger.warning(e.getLocalizedMessage());
        logger.warningf("Failed to create directory at %s", path);
      }
    }
  }

  public static void createDirectories(String... paths) {
    for(String path : paths)
      createDirectory(path);
  }

  public static <T> T syncObjectWithFile(String path, T defaultValue, Class<T> tClass) {
    logger.debugf("Synchronizing object with file %s, using default value %s", path, defaultValue.toString());
    AssetLocation location = Asset.systemLocation(path);
    JsonAsset ja = Asset.loadJson(location);
    JsonNode json = null;

    try {
      json = mapper.valueToTree(defaultValue);
    } catch (Exception e) {
      e.printStackTrace();
      logger.warningf("Failed to convert object to json %s while synchronizing object with file %s ", tClass.getCanonicalName(), path);
    }
    T value = defaultValue;

    if(ja.getValue() == null && json != null)
      ja = Asset.saveJson(location, json);

    try {
      T newValue = mapper.treeToValue(ja.getValue(), tClass);
      if(newValue != null)
        value = newValue;
    } catch (Exception e) {
      e.printStackTrace();
      logger.warningf("Failed to convert json to type %s while synchronizing object with file %s ", tClass.getCanonicalName(), path);
    }

    logger.debugf("Synchronized object with file %s, end value %s", path, value.toString());
    return value;
  }

  public static <T> void writeObjectToFile(String path, T defaultValue, Class<T> tClass) {
    logger.debugf("Writing object to file %s, using default value %s", path, defaultValue.toString());
    AssetLocation location = Asset.systemLocation(path);
    JsonNode json = null;

    try {
      json = mapper.valueToTree(defaultValue);
    } catch (Exception e) {
      e.printStackTrace();
      logger.warningf("Failed to convert object to json %s while writing object to file %s ", tClass.getCanonicalName(), path);
    }

    Asset.saveJson(location, json);

    logger.debugf("Wrote object %s to file %s", path, defaultValue.toString());
  }

}
