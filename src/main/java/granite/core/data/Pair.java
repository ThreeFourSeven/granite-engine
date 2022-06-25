package granite.core.data;

public class Pair<F, S> {

  public final F first;
  public final S second;

  protected Pair(F first, S second) {
    this.first = first;
    this.second = second;
  }

  public static <V, K> Pair<V, K> from(V first, K second) {
    return new Pair<>(first, second);
  }

}
