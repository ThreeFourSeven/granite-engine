package granite.core.scene.entity.system;

import granite.core.scene.entity.Entity;
import granite.core.scene.entity.component.Component;
import granite.core.scene.entity.component.ComponentType;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public abstract class EntitySystem {

  protected final UUID id;
  protected final EntityFilter filter;

  public EntitySystem(EntityFilter filter) {
    this.id = UUID.randomUUID();
    this.filter = filter;
  }

  protected void initialize(Entity entity, Map<ComponentType, Component> components) {}

  protected void loop(Entity entity, Map<ComponentType, Component> components) {}

  protected void destroy(Entity entity, Map<ComponentType, Component> components) {}

  public void initializeEntity(Entity entity, Map<ComponentType, Component> components) {
    initialize(entity, components);
  }

  public void loopEntity(Entity entity, Map<ComponentType, Component> components) {
    loop(entity, components);
  }

  public void destroyEntity(Entity entity, Map<ComponentType, Component> components) {
    destroy(entity, components);
  }

}
