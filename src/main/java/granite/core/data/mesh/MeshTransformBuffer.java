package granite.core.data.mesh;

import lombok.Getter;

import java.nio.FloatBuffer;

@Getter
public class MeshTransformBuffer {

  private static final int MATRIX_SIZE = 16;
  private final float[] values;

  public MeshTransformBuffer(int transformCount) {
    this.values = new float[MATRIX_SIZE * transformCount];
  }

  public void setTransform(int index, float[] matrix) {
    System.arraycopy(matrix, 0, values, index * MATRIX_SIZE, MATRIX_SIZE);
  }

  public void populateFloatBuffer(FloatBuffer buffer) {
    for(float v : values)
      buffer.put(v);
  }

}
