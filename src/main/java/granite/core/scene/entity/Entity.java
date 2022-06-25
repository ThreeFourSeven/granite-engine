package granite.core.scene.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
public class Entity {

  protected final UUID id = UUID.randomUUID();

  @Setter
  protected boolean initialized = false;

}
