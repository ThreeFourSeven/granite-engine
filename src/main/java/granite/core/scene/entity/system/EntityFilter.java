package granite.core.scene.entity.system;

import granite.core.scene.entity.component.Component;
import granite.core.scene.entity.component.ComponentType;
import granite.core.scene.entity.component.NameComponent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityFilter {

  protected final HashSet<ComponentType> componentFilter = new HashSet<>();
  protected String nameFilter = "";

  public EntityFilter setComponentFilter(Set<ComponentType> componentFilter) {
    this.componentFilter.clear();
    this.componentFilter.addAll(componentFilter);
    return this;
  }

  public EntityFilter setNameFilter(String name) {
    nameFilter = name;
    return this;
  }

  public boolean matches(Map<ComponentType, Component> components) {
    boolean nameFilterMatch = true;

    if(nameFilter != null && components.containsKey(ComponentType.Name)) {
      NameComponent nc = (NameComponent)components.get(ComponentType.Name);
      nameFilterMatch = nc.value.contains(nameFilter);
    }

    boolean componentFilterMatch = true;
    if(componentFilter.size() > 0) {
      Set<ComponentType> types = components.keySet();
      componentFilterMatch = componentFilter.stream()
        .map(types::contains)
        .reduce((a, b) -> a && b).orElse(false);
    }

    return nameFilterMatch && componentFilterMatch;
  }

}
