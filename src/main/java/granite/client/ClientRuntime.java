package granite.client;

import granite.core.asset.Asset;
import granite.core.asset.AISceneAsset;
import granite.core.asset.AssetReference;
import granite.core.asset.TextAsset;
import granite.core.command.Command;
import granite.core.graphics.WindowOptions;
import granite.core.math.Transform;
import granite.core.scene.Scene;
import granite.core.scene.SceneTemplate;
import granite.core.scene.entity.Entity;
import granite.core.scene.entity.component.ComponentType;
import granite.core.scene.entity.component.NameComponent;
import granite.core.scene.entity.component.TransformComponent;
import granite.core.scene.entity.system.DebugSystem;
import org.lwjgl.glfw.GLFW;
import granite.core.event.Events;
import granite.core.file.FileUtil;
import granite.core.graphics.Graphics;
import granite.core.graphics.GraphicsOptions;
import granite.core.graphics.Window;
import granite.core.graphics.gl.Gl;
import granite.core.input.Input;
import granite.core.logger.Logger;
import granite.Granite;

import java.util.Collection;
import java.util.stream.Collectors;


public class ClientRuntime implements Runnable {

  private final Logger logger = new Logger(ClientRuntime.class);

  public final Input input = new Input();
  public final Events events = new Events();

  public Graphics graphics;
  public Window window;

  //TEMP
  private final Scene scene = new Scene();

  @Override
  public void run() {
    logger.debug("Starting client runtime");
    initialize();
    logger.debug("Started client runtime");
    while(Granite.runtime.isRunning()) {
      loop();
    }
    destroy();
    logger.debug("Stopped client runtime");
  }

  private void initialize() {
    GLFW.glfwInit();

    FileUtil.createDirectories("data/", "data/config/", "data/log/");

    GraphicsOptions graphicsOptions = FileUtil.syncObjectWithFile("data/config/graphics.json", new GraphicsOptions(), GraphicsOptions.class);
    WindowOptions windowOptions = FileUtil.syncObjectWithFile("data/config/window.json", new WindowOptions(), WindowOptions.class);
    graphics = new Gl(graphicsOptions);
    window = Window.create("Granite - Client", windowOptions, graphics);

    input.loadCodes();
    input.bindCallbacks(window);

    Command.addCommand("stop", args -> Granite.runtime.stop())
      .addAliases("exit", "shutdown")
      .addKeyBinding("escape")
      .addPredicateBinding(() -> window.shouldClose());

    //TEMP

    scene.addEntitySystem(new DebugSystem());

    Entity test = scene.createEntity(new NameComponent("Test Entity"), new TransformComponent(new Transform().setPosition(1, 1, 1)));
    scene.createEntity(test, new NameComponent("Test Entity 1"), new TransformComponent());
    scene.createEntity(test, new NameComponent("Test Entity 2"), new TransformComponent());

    scene.initialize();

    SceneTemplate sceneTemplate = scene.createTemplateFromEntity(scene.getRootEntity());

    Asset.saveObject(Asset.systemLocation("data/scene.json"), sceneTemplate);

  }

  private void loop() {
    Command.loop();
    events.loop();
    input.loop();
    GLFW.glfwPollEvents();

    window.setupFrame();
    scene.loop();
    graphics.render(scene);
    window.renderFrame();
  }

  private void destroy() {
    scene.destroy();
    events.clearConsumers();
    window.destroy();
    GLFW.glfwTerminate();
  }

}
