package granite.core.math;

public class MathUtil {

  public static int sum(int... values) {
    int s = 0;
    for(int v : values)
      s += v;
    return s;
  }

  public static long sum(long... values) {
    int s = 0;
    for(long v : values)
      s += v;
    return s;
  }

  public static float sum(float... values) {
    int s = 0;
    for(float v : values)
      s += v;
    return s;
  }

  public static double sum(double... values) {
    int s = 0;
    for(double v : values)
      s += v;
    return s;
  }

}
