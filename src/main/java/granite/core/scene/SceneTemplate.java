package granite.core.scene;

import granite.core.scene.entity.Entity;
import granite.core.scene.entity.component.Component;
import granite.core.scene.entity.component.ComponentType;

import java.util.*;

public class SceneTemplate {

  public final UUID id = UUID.randomUUID();
  public final Map<UUID, Entity> entities = new HashMap<>();
  public final Map<UUID, Map<ComponentType, Component>> entityComponents = new HashMap<>();
  public final Map<UUID, Set<UUID>> entityChildren = new HashMap<>();

}
