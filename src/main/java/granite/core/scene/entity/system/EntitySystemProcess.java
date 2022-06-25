package granite.core.scene.entity.system;

import granite.core.scene.entity.Entity;
import granite.core.scene.entity.component.Component;
import granite.core.scene.entity.component.ComponentType;

import java.util.Map;

@FunctionalInterface
public interface EntitySystemProcess {
  void process(Entity entity, Map<ComponentType, Component> components);
}
