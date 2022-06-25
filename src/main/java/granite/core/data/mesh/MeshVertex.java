package granite.core.data.mesh;

import lombok.Getter;

@Getter
public class MeshVertex {

  private final float[] values;
  private final MeshVertexSchema schema;

  public MeshVertex(MeshVertexSchema schema) {
    this.schema = schema;
    this.values = new float[schema.getSize()];
  }

}
