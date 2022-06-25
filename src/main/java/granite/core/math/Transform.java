package granite.core.math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.UUID;

@Getter
@ToString
public class Transform {

  protected final UUID id;
  protected final float[] matrix;
  protected final Vector3f position;
  protected final Vector3f scale;
  protected final Vector3f rotation;

  @JsonIgnore
  protected Transform parent = null;

  public Transform() {
    this.matrix = new float[16];
    this.position = new Vector3f();
    this.scale = new Vector3f(1, 1, 1);
    this.rotation = new Vector3f();
    this.id = UUID.randomUUID();
  }

  public Transform(Transform transform) {
    this.id = UUID.randomUUID();
    this.matrix = new float[16];
    this.position = new Vector3f(transform.position);
    this.scale = new Vector3f(transform.scale);
    this.rotation = new Vector3f(transform.rotation);
    this.parent = null;
    System.arraycopy(transform.matrix, 0, this.matrix, 0, 16);
  }

  public Transform setParent(Transform transform) {
    this.parent = transform;
    return recalculateMatrix();
  }

  public Transform removeParent() {
    parent = null;
    return recalculateMatrix();
  }

  public Transform move(float x, float y, float z) {
    position.x += x;
    position.y += y;
    position.z += z;
    return recalculateMatrix();
  }

  public Transform move(Vector3f v) {
    return move(v.x, v.y, v.z);
  }

  public Transform incrementScale(float x, float y, float z) {
    scale.x += x;
    scale.y += y;
    scale.z += z;
    return recalculateMatrix();
  }

  public Transform incrementScale(Vector3f v) {
    return incrementScale(v.x, v.y, v.z);
  }

  public Transform rotate(float x, float y, float z) {
    rotation.x += x;
    rotation.y += y;
    rotation.z += z;
    return recalculateMatrix();
  }

  public Transform rotate(Vector3f v) {
    return rotate(v.x, v.y, v.z);
  }

  public Transform lerp(float x0, float y0, float z0, float x1, float y1, float z1, float fraction) {
    return setPosition((x1 - x0) * fraction + x0, (z1 - z0) * fraction + z0, (z1 - z0) * fraction + z0);
  }

  public Transform setPosition(float x, float y, float z) {
    position.set(x, y, z);
    if(position.x != x || position.y != y || position.z != z)
      recalculateMatrix();
    return this;
  }

  public Transform setPosition(Vector3f v) {
    return setPosition(v.x, v.y, v.z);
  }

  public Transform setScale(float x, float y, float z) {
    scale.set(x, y, z);
    if(scale.x != x || scale.y != y || scale.z != z)
      recalculateMatrix();
    return this;
  }

  public Transform setScale(Vector3f v) {
    return setScale(v.x, v.y, v.z);
  }

  public Transform setRotation(float x, float y, float z) {
    rotation.set(x, y, z);
    if(rotation.x != x || rotation.y != y || rotation.z != z)
      recalculateMatrix();
    return this;
  }

  public Transform setRotation(Vector3f v) {
    return setRotation(v.x, v.y, v.z);
  }

  protected Transform recalculateMatrix() {
    Matrix4f newMatrix = new Matrix4f().identity();
    if(parent != null)
      newMatrix.translate(parent.position)
        .scale(parent.scale)
        .rotate(new Quaternionf().rotateXYZ(parent.rotation.x, parent.rotation.y, parent.rotation.z));

    newMatrix.translate(position)
      .scale(scale)
      .rotate(new Quaternionf().rotateXYZ(rotation.x, rotation.y, rotation.z));

    float[] newMatrixArray = newMatrix.get(new float[16]);
    System.arraycopy(newMatrixArray, 0, matrix, 0, 16);
    return this;
  }

}
