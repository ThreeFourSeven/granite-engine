package granite.core.data.mesh;

import lombok.Getter;

@Getter
public class MeshData {

  private final MeshVertexBuffer vertexBuffer;
  private final MeshIndexBuffer indexBuffer;

  public MeshData(MeshVertexBuffer vertexBuffer, MeshIndexBuffer indexBuffer) {
    this.vertexBuffer = vertexBuffer;
    this.indexBuffer = indexBuffer;
  }

}
