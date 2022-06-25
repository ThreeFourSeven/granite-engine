package granite.core.graphics.gl;

import granite.core.graphics.Graphics;
import granite.core.math.Transform;
import granite.core.scene.Scene;
import granite.core.scene.entity.Entity;
import granite.core.scene.entity.component.ComponentType;
import granite.core.scene.entity.component.MeshComponent;
import granite.core.scene.entity.component.TransformComponent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import granite.core.graphics.GraphicsOptions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Gl extends Graphics {

  private static final int versionMajor = 4;
  private static final int versionMinor = 6;

  public Gl(GraphicsOptions options) {
    super(options);
  }

  public Gl() {
  }

  @Override
  public void initialize() {
    GL.createCapabilities();
    GL46.glEnable(GL46.GL_MULTISAMPLE);
  }

  @Override
  public void setVersionHint() {
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, versionMajor);
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, versionMinor);
  }

  @Override
  public void setupFrame() {
    GL46.glClearColor(0, 0, 0, 0);
    GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT | GL46.GL_STENCIL_BUFFER_BIT);
  }

  @Override
  public void setViewport(int x, int y, int width, int height) {
    GL46.glViewport(0, 0, width, height);
  }

  @Override
  public void render(Scene scene) {
    Collection<Entity> entities = scene.getEntities().values();
    for(Entity entity : entities) {
      if(scene.entityHasComponent(entity, ComponentType.Transform)) {
        TransformComponent T = scene.getEntityComponentAs(entity, ComponentType.Transform, TransformComponent.class);

      }
    }
  }

}
