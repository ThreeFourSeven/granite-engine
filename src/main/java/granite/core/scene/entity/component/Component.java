package granite.core.scene.entity.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = NameComponent.class, name = "Name"),
  @JsonSubTypes.Type(value = MeshComponent.class, name = "Mesh"),
  @JsonSubTypes.Type(value = TransformComponent.class, name = "Transform"),
})
@Getter
@ToString
public abstract class Component {

  protected final ComponentType type;

  protected Component(ComponentType type) {
    this.type = type;
  }

  @JsonIgnore
  public abstract Component copy();

}
