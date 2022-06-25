package granite.core.scene.entity.system;

import granite.core.logger.Logger;
import granite.core.scene.entity.Entity;
import granite.core.scene.entity.component.Component;
import granite.core.scene.entity.component.ComponentType;

import java.util.Map;

public class DebugSystem extends EntitySystem {

  private static final Logger logger = new Logger(DebugSystem.class);

  private static final EntityFilter filter = new EntityFilter();

  public DebugSystem() {
    super(filter);
  }

  @Override
  protected void initialize(Entity entity, Map<ComponentType, Component> components) {
    logger.debugf("Initializing entity %s with components %s", entity.toString(), components.toString());
  }

  @Override
  protected void loop(Entity entity, Map<ComponentType, Component> components) {
    logger.debugf("Looping entity %s with components %s", entity.toString(), components.toString());
  }

  @Override
  protected void destroy(Entity entity, Map<ComponentType, Component> components) {
    logger.debugf("Destroyed entity %s with components %s", entity.toString(), components.toString());
  }
}
