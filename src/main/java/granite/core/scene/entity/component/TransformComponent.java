package granite.core.scene.entity.component;

import granite.core.math.Transform;
import lombok.ToString;

@ToString
public class TransformComponent extends Component {

  public final Transform transform;
  public int bufferIndex = -1;

  public TransformComponent(Transform transform) {
    super(ComponentType.Transform);
    this.transform = transform;
  }

  public TransformComponent() {
    this(new Transform());
  }

  @Override
  public TransformComponent copy() {
    return new TransformComponent(new Transform(transform));
  }

}
