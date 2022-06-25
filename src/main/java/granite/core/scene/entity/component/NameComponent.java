package granite.core.scene.entity.component;

import lombok.ToString;

import java.util.UUID;


@ToString
public class NameComponent extends Component {

  public String value;

  public NameComponent(String name) {
    super(ComponentType.Name);
    this.value = name;
  }

  public NameComponent() {
    this("entity_" + UUID.randomUUID().toString());
  }

  @Override
  public NameComponent copy() {
    return new NameComponent(value);
  }

}
