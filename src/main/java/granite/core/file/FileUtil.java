package granite.core.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import granite.GraniteConfig;
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
        logger.warningf("Failed to create directory at %s", path);
        logger.warning(e.getLocalizedMessage());
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
    T value = Asset.loadObject(location, defaultValue, tClass);
    if(value == defaultValue)
      Asset.saveObject(location, value);

    logger.debugf("Synchronized object with file %s, end value %s", path, value.toString());
    return value;
  }

}
