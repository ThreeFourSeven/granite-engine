package granite.core.asset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Getter
public class AssetLocation {

  protected final String value;
  protected AssetLocationType type;

  public AssetLocation(String value, AssetLocationType type) {
    this.value = value;
    this.type = type;
  }

  public AssetLocation() {
    this(null, null);
  }

  @JsonIgnore
  public InputStream getInputStream() throws Exception {
    switch(type) {
      case System: return new FileInputStream(value);
      case Jar: return AssetLocation.class.getClassLoader().getResourceAsStream(value);
      case Network:
      default: return null;
    }
  }

  @JsonIgnore
  public OutputStream getOutputStream() throws Exception {
    switch (type) {
      case System: return new FileOutputStream(value);
      case Network:
      case Jar:
      default: return null;
    }
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.format("%s:%s", type.toString(), value);
  }
}
