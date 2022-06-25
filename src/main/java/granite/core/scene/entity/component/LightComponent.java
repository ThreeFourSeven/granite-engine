package granite.core.scene.entity.component;

public class LightComponent extends Component {

  public LightComponent() {
    super(ComponentType.Light);
  }

  @Override
  public Component copy() {
    return new LightComponent();
  }
}
