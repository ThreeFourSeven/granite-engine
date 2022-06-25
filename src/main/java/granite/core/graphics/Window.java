package granite.core.graphics;

import lombok.Getter;
import org.lwjgl.glfw.GLFW;

@Getter
public class Window {

  protected final long id;
  protected Graphics graphics;
  protected int width, height;
  protected String title;

  protected Window(long id, Graphics graphics, int width, int height, String title) {
    this.id = id;
    this.graphics = graphics;
    this.width = width;
    this.height = height;
    this.title = title;
  }

  public void setTitle(String title) {
    this.title = title;
    GLFW.glfwSetWindowTitle(id, title);
  }

  public void setupFrame() {
    graphics.setupFrame();
  }

  public void renderFrame() {
    GLFW.glfwSwapBuffers(id);
  }

  public void show() {
    GLFW.glfwShowWindow(id);
  }

  public void hide() {
    GLFW.glfwHideWindow(id);
  }

  public boolean shouldClose() {
    return GLFW.glfwWindowShouldClose(id);
  }

  public void destroy() {
    GLFW.glfwDestroyWindow(id);
  }

  public static Window create(String title, WindowOptions options, Graphics graphics) {
    GLFW.glfwDefaultWindowHints();
    graphics.setVersionHint();
    if(graphics.options.msaa)
      GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, graphics.options.msaaSamples);
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, options.resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, options.bordered ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    long id = GLFW.glfwCreateWindow(options.width, options.height, title, 0, 0);
    GLFW.glfwMakeContextCurrent(id);
    graphics.initialize();
    GLFW.glfwSwapInterval(graphics.options.vsync ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    graphics.setViewport(0, 0, options.width, options.height);
    return new Window(id, graphics, options.width, options.height, title);
  }

}
