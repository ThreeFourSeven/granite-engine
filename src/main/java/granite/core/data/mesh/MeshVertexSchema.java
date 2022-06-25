package granite.core.data.mesh;

import granite.core.math.MathUtil;
import lombok.Getter;

@Getter
public class MeshVertexSchema {

  private final int[] segmentSizes;
  private final int size;

  public MeshVertexSchema(int[] segmentSizes) {
    this.segmentSizes = segmentSizes;
    this.size = MathUtil.sum(segmentSizes);
  }

  public MeshVertexSchema() {
    this(new int[]{});
  }
}
