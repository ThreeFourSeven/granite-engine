package granite.core.data.mesh;

import lombok.Getter;

@Getter
public class MeshIndexBuffer {

  private final int[] indices;

  public MeshIndexBuffer(int[] indices) {
    this.indices = indices;
  }

}
