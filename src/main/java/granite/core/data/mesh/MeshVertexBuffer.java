package granite.core.data.mesh;

import lombok.Getter;

import java.nio.FloatBuffer;

@Getter
public class MeshVertexBuffer {

  private final MeshVertex[] vertices;
  private final MeshVertexSchema schema;

  public MeshVertexBuffer(MeshVertexSchema schema, int vertexCount) {
    this.schema = schema;
    vertices = new MeshVertex[vertexCount];
    for(int i = 0; i < vertexCount; i++)
      vertices[i] = new MeshVertex(schema);
  }

  public void populateFloatBuffer(FloatBuffer buffer) {
    for(MeshVertex vertex : vertices)
      for(float v : vertex.getValues())
        buffer.put(v);
  }

}
