package granite.core.asset;

import granite.core.logger.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class AISceneAsset extends Asset {

  private static final Logger logger = new Logger(AISceneAsset.class);

  public AISceneAsset(AssetLocation location) {
    super(AssetType.Scene, location);
  }

  public AISceneAsset() {
  }

  @Override
  public AIScene getValue() {
    return (AIScene)super.getValue();
  }

  @Override
  protected AIScene read(InputStream stream) {
    AIScene scene = null;
    try {
      byte[] bytes = stream.readAllBytes();
      if(bytes != null) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        int flags = Assimp.aiProcess_JoinIdenticalVertices |
          Assimp.aiProcess_Triangulate |
          Assimp.aiProcess_ValidateDataStructure;
        AIScene s = Assimp.aiImportFileFromMemory(buffer, flags, "");
        if(s != null)
          scene = s;
      }
    } catch (Exception e) {
      e.printStackTrace();

    }
    return scene;
  }

  @Override
  protected void write(OutputStream stream) {

  }
}
