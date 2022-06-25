package granite.core.graphics.shader;

import lombok.Getter;

@Getter
public class Shader {

  protected final String source;
  protected final ShaderType type;

  public Shader(String source, ShaderType type) {
    this.source = source;
    this.type = type;
  }

}
