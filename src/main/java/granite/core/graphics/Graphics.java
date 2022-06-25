package granite.core.graphics;

import granite.core.scene.Scene;
import lombok.Getter;

@Getter
public abstract class Graphics {

  protected GraphicsOptions options;

  public Graphics(GraphicsOptions options) {
    this.options = options;
  }

  public Graphics() {
    this(new GraphicsOptions());
  }

  public abstract void initialize();
  public abstract void setVersionHint();
  public abstract void setupFrame();
  public abstract void setViewport(int x, int y, int width, int height);
  public abstract void render(Scene scene);

}
