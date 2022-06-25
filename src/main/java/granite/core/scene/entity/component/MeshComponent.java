package granite.core.scene.entity.component;


import lombok.ToString;

@ToString
public class MeshComponent extends Component {

  public MeshComponent() {
    super(ComponentType.Mesh);
  }

  @Override
  public MeshComponent copy() {
    return new MeshComponent();
  }

}
