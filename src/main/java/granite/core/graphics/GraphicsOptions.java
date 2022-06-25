package granite.core.graphics;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GraphicsOptions {

  protected boolean vsync = false;
  protected boolean msaa = true;
  protected int msaaSamples = 4;

}
