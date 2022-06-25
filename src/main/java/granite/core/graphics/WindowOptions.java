package granite.core.graphics;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WindowOptions {

  protected boolean bordered = true;
  protected boolean resizable = true;
  protected int width = 1600;
  protected int height = 900;

}
