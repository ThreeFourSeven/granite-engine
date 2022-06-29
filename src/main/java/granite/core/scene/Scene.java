package granite.core.scene;

import com.fasterxml.jackson.annotation.JsonIgnore;
import granite.core.asset.AssetStore;
import granite.core.logger.Logger;
import granite.core.scene.entity.component.Component;
import granite.core.scene.entity.component.ComponentType;
import granite.core.scene.entity.Entity;
import granite.core.scene.entity.component.TransformComponent;
import granite.core.scene.entity.system.EntitySystem;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Scene {

  private static final Logger logger = new Logger(Scene.class);

  @Getter
  protected final UUID id = UUID.randomUUID();

  @Getter
  protected final Map<UUID, Entity> entities = new HashMap<>();

  @JsonIgnore
  protected final Map<UUID, EntitySystem> entitySystems = new HashMap<>();
  protected final Map<UUID, UUID> entityEntitySystems = new HashMap<>();
  protected final Map<UUID, Set<UUID>> entitySystemEntities = new HashMap<>();

  protected final Map<UUID, Map<ComponentType, Component>> entityComponents = new HashMap<>();
  protected final Map<UUID, Set<UUID>> entityChildren = new HashMap<>();
  protected final Map<UUID, UUID> entityParents = new HashMap<>();

  protected final AssetStore assetStore = new AssetStore();

  @Getter
  protected final Entity rootEntity = createEntity((Entity)null, new TransformComponent());

  public Scene() {
  }

  public void initialize() {
    for(UUID systemId : entitySystemEntities.keySet()) {
      EntitySystem system = getEntitySystem(systemId);
      for(UUID entityId : entitySystemEntities.get(systemId)) {
         Entity entity = getEntity(entityId);
         if(!entity.isInitialized()) {
           system.initializeEntity(entity, getEntityComponents(entity));
           entity.setInitialized(true);
         }
      }
    }
  }

  public void loop() {
    for(UUID systemId : entitySystemEntities.keySet()) {
      EntitySystem system = getEntitySystem(systemId);
      for(UUID entityId : entitySystemEntities.get(systemId)) {
        Entity entity = getEntity(entityId);
        system.loopEntity(entity, getEntityComponents(entity));
      }
    }
  }

  public void destroy() {
    for(UUID systemId : entitySystemEntities.keySet()) {
      EntitySystem system = getEntitySystem(systemId);
      for(UUID entityId : entitySystemEntities.get(systemId)) {
        Entity entity = getEntity(entityId);
        entity.setInitialized(false);
        system.destroyEntity(entity, getEntityComponents(entity));
      }
    }
  }

  public Collection<Entity> getAllEntities() {
    return entities.values();
  }

  public Entity getEntity(UUID id) {
    return entities.getOrDefault(id, null);
  }

  public EntitySystem getEntitySystem(UUID id) {
    return entitySystems.getOrDefault(id, null);
  }

  public Entity getEntityParent(Entity entity) {
    return getEntity(entityParents.getOrDefault(entity.getId(), null));
  }

  public Collection<Entity> getEntityChildren(Entity entity) {
    Set<UUID> childrenIds = entityChildren.getOrDefault(entity.getId(), null);
    return childrenIds.stream().map(this::getEntity).filter(Objects::nonNull).collect(Collectors.toList());
  }

  public Map<ComponentType, Component> getEntityComponents(Entity entity) {
    return entityComponents.getOrDefault(entity.getId(), new HashMap<>());
  }

  public Component getEntityComponent(Entity entity, ComponentType type) {
    return getEntityComponents(entity).getOrDefault(type, null);
  }

  public <T extends Component> T getEntityComponentAs(Entity entity, ComponentType type, Class<T> tClass) {
    return (T)getEntityComponent(entity, type);
  }

  public Collection<Map<ComponentType, Component>> getAllComponentsOfTypes(Set<ComponentType> types) {
    Collection<Map<ComponentType, Component>> components = new ArrayList<>();
    for(Map<ComponentType, Component> componentMap : entityComponents.values()) {
      boolean match = types.stream()
        .map(componentMap::containsKey)
        .reduce((a, b) -> a && b).orElse(false);
      if (match) {
        HashMap<ComponentType, Component> c = new HashMap<>();
        for(ComponentType type : types)
          c.put(type, componentMap.get(type));
        components.add(c);
      }
    }
    return components;
  }

  public Collection<Map<ComponentType, Component>> getAllComponentsOfTypes(ComponentType... types) {
    return getAllComponentsOfTypes(Set.of(types));
  }

  public Collection<Component> getAllComponentsOfType(ComponentType type) {
    Collection<Component> components = new ArrayList<>();
    for(Map<ComponentType, Component> componentMap : entityComponents.values())
      if(componentMap.containsKey(type))
        components.add(componentMap.get(type));
    return components;
  }

  public boolean entityHasComponent(Entity entity, ComponentType type) {
    return getEntityComponents(entity).containsKey(type);
  }

  public void addEntityToParent(Entity child, Entity parent) {
    if(parent == null) return;
    UUID pId = parent.getId();
    UUID cId = child.getId();
    if(entityParents.containsKey(cId))
      entityChildren.get(entityParents.get(cId)).remove(cId);

    entityParents.put(cId, pId);
    if(!entityChildren.containsKey(pId))
      entityChildren.put(pId, new HashSet<>());
    entityChildren.get(pId).add(cId);
    if(entityHasComponent(child, ComponentType.Transform) && entityHasComponent(parent, ComponentType.Transform)) {
      TransformComponent childT = (TransformComponent)getEntityComponent(child, ComponentType.Transform);
      TransformComponent parentT = (TransformComponent)getEntityComponent(parent, ComponentType.Transform);
      childT.transform.setParent(parentT.transform);
    }
  }

  protected void attachComponentToEntity(Entity entity, Component component) {
    UUID eId = entity.getId();
    if(!entityComponents.containsKey(eId))
      entityComponents.put(eId, new HashMap<>());
    entityComponents.get(eId).put(component.getType(), component);
  }

  protected void attachComponentsToEntity(Entity entity, Collection<Component> components) {
    UUID eId = entity.getId();
    if(!entityComponents.containsKey(eId))
      entityComponents.put(eId, new HashMap<>());
    for(Component component : components)
      attachComponentToEntity(entity, component);
  }

  public void dettachComponentFromEntity(Entity entity, ComponentType type) {
    if(entities.containsKey(entity.getId()))
      entityComponents.get(entity.getId()).remove(type);
  }

  public Entity addEntity(Entity entity, Entity parent, Collection<Component> components) {
    entities.put(entity.getId(), entity);
    attachComponentsToEntity(entity, components);
    addEntityToSystems(entity);
    addEntityToParent(entity, parent);
    return entity;
  }

  public void removeEntity(Entity entity) {
    if(entities.containsKey(entity.getId())) {
      Set<UUID> toRemove = new HashSet<>();
      walkEntityTree(entity.getId(), e -> {
        UUID id = e.getId();
        entitySystemEntities.get(entityEntitySystems.get(id)).remove(id);
        entityEntitySystems.remove(id);
        entityComponents.remove(id);
        entityParents.remove(id);
        toRemove.add(id);
      });
      for(UUID id : toRemove)
        entityChildren.remove(id);
    }
  }

  public Entity addEntity(Entity entity, Entity parent, Component... components) {
    return addEntity(entity, parent, List.of(components));
  }

  public Entity addEntity(Entity entity, Collection<Component> components) {
    return addEntity(entity, rootEntity, components);
  }

  public Entity addEntity(Entity entity, Component... components) {
    return addEntity(entity, rootEntity, components);
  }

  public Entity createEntity(Entity parent, Component... components) {
    return addEntity(new Entity(), parent, components);
  }

  public Entity createEntity(Entity parent, Collection<Component> components) {
    return addEntity(new Entity(), parent, components);
  }

  public Entity createEntity(Component... components) {
    return createEntity(rootEntity, components);
  }

  public Entity createEntity(Collection<Component> components) {
    return createEntity(rootEntity, components);
  }

  public EntitySystem addEntitySystem(EntitySystem system) {
    entitySystems.put(system.getId(), system);
    addEntitiesToSystem(system);
    return system;
  }

  protected void addEntitiesToSystem(EntitySystem system) {
    for(Entity entity : entities.values())
      addEntityToSystems(entity);
  }

  protected void addEntityToSystem(Entity entity, EntitySystem system) {
    if(!entitySystemEntities.containsKey(system.getId()))
      entitySystemEntities.put(system.getId(), new HashSet<>());
    entitySystemEntities.get(system.getId()).add(entity.getId());
    entityEntitySystems.put(entity.getId(), system.getId());
  }

  protected void addEntityToSystems(Entity entity) {
    Map<ComponentType, Component> components = getEntityComponents(entity);
    for(EntitySystem system : entitySystems.values())
      if(system.getFilter().matches(components))
        addEntityToSystem(entity, system);
  }

  protected static void walkEntityTree(UUID entityId, Consumer<Entity> consumer, Map<UUID, Entity> entities, Map<UUID, Set<UUID>> entityChildren) {
    if(entities.containsKey(entityId)) {
      consumer.accept(entities.get(entityId));
      if (entityChildren.containsKey(entityId)) {
        Set<UUID> childrenIds = entityChildren.get(entityId);
        for (UUID childId : childrenIds)
          walkEntityTree(childId, consumer, entities, entityChildren);
      }
    }
  }

  public void walkEntityTree(UUID entityId, Consumer<Entity> consumer) {
    Scene.walkEntityTree(entityId, consumer, entities, entityChildren);
  }

  public Entity addFromTemplate(Entity parent, SceneTemplate template) {
    Entity templateRoot = new Entity();

    Scene.walkEntityTree(
      templateRoot.getId(),
      entity -> addEntity(
          new Entity(), parent,
          template.entityComponents
            .getOrDefault(templateRoot.getId(), new HashMap<>())
            .values().stream().map(Component::copy)
            .collect(Collectors.toList())
      ),
      template.entities, template.entityChildren
    );

    return templateRoot;
  }

  public Entity addFromTemplate(SceneTemplate template) {
    return addFromTemplate(rootEntity, template);
  }

  public SceneTemplate createTemplateFromEntity(Entity root) {
    SceneTemplate template = new SceneTemplate();

    walkEntityTree(
      root.getId(),
      entity -> {
        template.entities.put(entity.getId(), entity);
        template.entityChildren.put(entity.getId(), entityChildren.getOrDefault(entity.getId(), new HashSet<>()));
        Map<ComponentType, Component> entityComponents = getEntityComponents(entity);
        Map<ComponentType, Component> components = new HashMap<>();
        for(ComponentType type : entityComponents.keySet())
          components.put(type, entityComponents.get(type).copy());
        template.entityComponents.put(entity.getId(), components);
      }
    );

    return template;
  }

}
