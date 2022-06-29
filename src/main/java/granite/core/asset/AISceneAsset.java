package granite.core.asset;

import granite.core.logger.Logger;
import granite.core.scene.SceneTemplate;
import granite.core.scene.entity.Entity;
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

  public SceneTemplate createSceneTemplate() {
    SceneTemplate template = new SceneTemplate();
    Entity root = new Entity();
    template.entities.put(root.getId(), root);

    return template;
  }

  private SceneTemplate addAINodeToTemplate(SceneTemplate template) {
    return template;
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
        try(AIScene s = Assimp.aiImportFileFromMemory(buffer, flags, "")) {
          if(s != null)
            scene = s;
        }
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
