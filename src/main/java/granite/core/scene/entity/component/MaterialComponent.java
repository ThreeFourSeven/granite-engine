package granite.core.scene.entity.component;

public class MaterialComponent extends Component {

  public MaterialComponent() {
    super(ComponentType.Material);
  }

  @Override
  public Component copy() {
    return new MaterialComponent();
  }
}
